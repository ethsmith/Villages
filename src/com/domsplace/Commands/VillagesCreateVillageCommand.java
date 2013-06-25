package com.domsplace.Commands;

import com.domsplace.Objects.Village;
import com.domsplace.Utils.VillageEconomyUtils;
import com.domsplace.Utils.VillageUtils;
import com.domsplace.Utils.VillageVillagesUtils;
import com.domsplace.VillageBase;
import com.domsplace.VillagesPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VillagesCreateVillageCommand extends VillageBase implements CommandExecutor {
    
    private VillagesPlugin plugin;
    
    public VillagesCreateVillageCommand (VillagesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("createvillage")) {
            if(!(cs instanceof Player)) {
                VillageUtils.msgPlayer(cs, gK("playeronly"));
                VillageUtils.msgPlayer(cs, VillageUtils.getCommandDescription(cmd.getName()));
                return true;
            }
            
            if(args.length < 1) {
                VillageUtils.msgPlayer(cs, gK("entervillagename"));
                VillageUtils.msgPlayer(cs, VillageUtils.getCommandDescription(cmd.getName()));
                return true;
            }
            
            Player sender = (Player) cs;
            
            //Ensure player has enough cash
            if(VillageUtils.useEconomy) {
                //Get Money player has
                double cash = VillageEconomyUtils.economy.getBalance(sender.getName());
                double townCost = VillageVillagesUtils.CreateVillageCost();
                if(cash < townCost) {
                    VillageUtils.msgPlayer(cs, gK("notenoughmoney", townCost));
                    return true;
                }
            }
            
            //Make sure name is valid
            String name = args[0];
            if(!name.matches("^[a-zA-Z0-9]*$")) {
                VillageUtils.msgPlayer(cs, gK("invalidvillagename"));
                return true;
            }
            
            //Check town isnt a reserved name//
            String[] reservedNames = {
                "deposit",
                "kick",
                "withdraw",
                "spawn",
                "close",
                "leave"
            };
            
            for(String s : reservedNames) {
                if(name.equalsIgnoreCase(s)) {
                    VillageUtils.msgPlayer(cs, gK("villagenameused"));
                    return true;
                }
            }
            
            //Check town doesnt exist/
            Village oldtown = VillageVillagesUtils.getVillageExact(name);
            if(oldtown != null) {
                VillageUtils.msgPlayer(cs, gK("villagenameused"));
                return true;
            }
            
            //Check user isn't in a town already
            oldtown = VillageVillagesUtils.getPlayerVillage(sender);
            if(oldtown != null) {
                VillageUtils.msgPlayer(cs, gK("alreadyinvillage"));
                return true;
            }
            
            //Check Surrounding areas for towns
            if(VillageVillagesUtils.isChunkInATownsArea(sender.getLocation().getChunk())) {
                VillageUtils.msgPlayer(cs, gK("createvillageoverlap"));
                return true;
            }
            
            //All Good, make town and charge player//
            if(VillageUtils.useEconomy) {
                //Get Money player has
                double cash = VillageEconomyUtils.economy.getBalance(sender.getName());
                double townCost = VillageVillagesUtils.CreateVillageCost();
                VillageEconomyUtils.economy.withdrawPlayer(sender.getName(), townCost);
            }
            
            Village newtown = new Village(name);
            newtown.setCreatedDate(VillageUtils.getNow());
            newtown.setDescription("Welcome to " + name + "!");
            newtown.setMayor(sender);
            newtown.setMoney(0);
            newtown.setTownSpawn(sender.getLocation().getChunk());
            newtown.setTownSize(1);
            
            VillageUtils.broadcast(gK("createdvillage", newtown).replaceAll("%p%", sender.getName()));
            VillageVillagesUtils.Villages.add(newtown);
            VillageVillagesUtils.SaveAllVillages();
            
            sender.teleport(newtown.getSpawnBlock().getBlock().getLocation());
            return true;
        }
        
        return false;
    }
}

package com.domsplace.Villages.Commands;

import com.domsplace.Villages.DataManagers.ConfigManager;
import com.domsplace.Villages.Events.VillageCreatedEvent;
import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Utils.VillageEconomyUtils;
import com.domsplace.Villages.Utils.Utils;
import com.domsplace.Villages.Utils.VillageUtils;
import static com.domsplace.Villages.Bases.Base.gK;
import com.domsplace.Villages.Bases.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateVillageCommand extends CommandBase {
    public CreateVillageCommand () {
        super("createvillage");
    }

    @Override
    public boolean cmd(CommandSender cs, Command cmd, String label, String[] args) {
        if(!(cs instanceof Player)) {
            Utils.msgPlayer(cs, gK("playeronly"));
            Utils.msgPlayer(cs, Utils.getCommandDescription(cmd.getName()));
            return true;
        }

        if(args.length < 1) {
            Utils.msgPlayer(cs, gK("entervillagename"));
            Utils.msgPlayer(cs, Utils.getCommandDescription(cmd.getName()));
            return true;
        }

        Player sender = (Player) cs;

        if(!VillageUtils.isVillageWorld(sender.getWorld())) {
            Utils.msgPlayer(cs, gK("notinthisworld"));
            return true;
        }

        //Ensure player has enough cash
        if(Utils.useEconomy) {
            //Get Money player has
            double cash = VillageEconomyUtils.economy.getBalance(sender.getName());
            double townCost = VillageUtils.CreateVillageCost();
            if(cash < townCost) {
                Utils.msgPlayer(cs, gK("notenoughmoney", townCost));
                return true;
            }
        }

        //Make sure name is valid
        String name = args[0];
        if(!name.matches("^[a-zA-Z0-9]*$")) {
            Utils.msgPlayer(cs, gK("invalidvillagename"));
            return true;
        }

        if(name.length() > 12) {
            Utils.msgPlayer(cs, gK("invalidvillagename"));
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
                Utils.msgPlayer(cs, gK("villagenameused"));
                return true;
            }
        }

        //Check town doesnt exist/
        Village oldtown = VillageUtils.getVillageExact(name);
        if(oldtown != null) {
            Utils.msgPlayer(cs, gK("villagenameused"));
            return true;
        }

        //Check user isn't in a town already
        oldtown = VillageUtils.getPlayerVillage(sender);
        if(oldtown != null) {
            Utils.msgPlayer(cs, gK("alreadyinvillage"));
            return true;
        }

        //Check Surrounding areas for towns
        if(VillageUtils.isChunkInATownsArea(sender.getLocation().getChunk())) {
            Utils.msgPlayer(cs, gK("createvillageoverlap"));
            return true;
        }

        if(VillageUtils.isChunkInRegion(sender.getLocation().getChunk())) {
            Utils.msgPlayer(cs, gK("createvillageregionoverlap"));
            return true;
        }

        //All Good, make town and charge player//
        if(Utils.useEconomy) {
            //Get Money player has
            double cash = VillageEconomyUtils.economy.getBalance(sender.getName());
            double townCost = VillageUtils.CreateVillageCost();
            VillageEconomyUtils.economy.withdrawPlayer(sender.getName(), townCost);
        }

        Village newtown = new Village(name);
        newtown.setCreatedDate(Utils.getNow());
        newtown.setDescription("Welcome to " + name + "!");
        newtown.setMayor(sender);
        newtown.setMoney(0);
        newtown.setTownSpawn(sender.getLocation().getChunk());
        newtown.setTownSize(getConfig().getInt("defaultsize"));

        //Fire Event
        VillageCreatedEvent event = new VillageCreatedEvent(sender, newtown);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()) {
            return true;
        }

        Utils.broadcast(gK("createdvillage", newtown).replaceAll("%p%", sender.getName()));
        VillageUtils.Villages.add(newtown);
        VillageUtils.SaveAllVillages();

        sender.teleport(newtown.getSpawnBlock().getBlock().getLocation());
        return true;
    }
}

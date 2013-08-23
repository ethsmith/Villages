package com.domsplace.Commands;

import com.domsplace.Objects.Village;
import com.domsplace.Utils.VillageUtils;
import com.domsplace.Utils.VillageVillagesUtils;
import com.domsplace.VillageBase;
import com.domsplace.VillagesPlugin;
import java.util.HashMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VillagesVillageInviteCommand extends VillageBase implements CommandExecutor {
    
    private VillagesPlugin plugin;
    
    public VillagesVillageInviteCommand (VillagesPlugin plugin) {
        VillageVillagesUtils.townInvites = new HashMap<Player, Village>();
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("villageinvite")) {
            if(!(cs instanceof Player)) {
                VillageUtils.msgPlayer(cs, gK("playeronly"));
                return true;
            }
            
            if(args.length < 1) {
                VillageUtils.msgPlayer(cs, gK("enterplayer"));
                VillageUtils.msgPlayer(cs, VillageUtils.getCommandDescription(cmd.getName()));
                return true;
            }
            
            Player sender = (Player) cs;
            
            //Get The Senders Village
            Village village = VillageVillagesUtils.getPlayerVillage(sender);
            if(village == null) {
                VillageUtils.msgPlayer(cs, gK("notinvillage"));
                return true;
            }
            
            Player p = VillageUtils.getPlayer(cs, args[0]);
            if(p == null) {
                VillageUtils.msgPlayer(cs, ChatError + args[0] + " isn't online.");
                return true;
            }
            
            Village tp = VillageVillagesUtils.getPlayerVillage(p);
            if(tp != null) {
                VillageUtils.msgPlayer(cs, ChatError + p.getDisplayName() + " is already in a Village.");
                return true;
            }
            
            VillageVillagesUtils.townInvites.put(p, village);
            VillageUtils.msgPlayer(p, gK("villageinvite", village).replaceAll("%p%", sender.getName()));
            VillageUtils.msgPlayer(p, ChatDefault + "Type " + ChatImportant + "/villageaccept" + ChatDefault + " or " + ChatImportant + "/villagedeny");
            VillageUtils.msgPlayer(sender, gK("residentinvited", p));
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("villageaccept")) {
            if(!(cs instanceof Player)) {
                VillageUtils.msgPlayer(cs, gK("playeronly"));
                return true;
            }
            
            Player sender = (Player) cs;
            
            if(!VillageVillagesUtils.townInvites.containsKey(sender)) {
                VillageUtils.msgPlayer(cs, gK("noinvite"));
                return true;
            }
            
            Village v = VillageVillagesUtils.townInvites.get(sender);
            if(v == null) {
                VillageUtils.msgPlayer(cs, gK("noinvite"));
                return true;
            }
            
            Village vs = VillageVillagesUtils.getPlayerVillage(sender);
            if(vs != null) {
                VillageUtils.msgPlayer(cs, gK("alreadyinvillage"));
                return true;
            }
            
            v.addResident(sender);
            v.SendMessage(gK("joinedvillage", sender));
            sender.teleport(v.getSpawnBlock());
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("villagedeny")) {
            if(!(cs instanceof Player)) {
                VillageUtils.msgPlayer(cs, gK("playeronly"));
                return true;
            }
            
            Player sender = (Player) cs;
            
            if(!VillageVillagesUtils.townInvites.containsKey(sender)) {
                VillageUtils.msgPlayer(cs, gK("noinvite"));
                return true;
            }
            
            Village v = VillageVillagesUtils.townInvites.get(sender);
            if(v == null) {
                VillageUtils.msgPlayer(cs, gK("noinvite"));
                return true;
            }
            
            sender.sendMessage(ChatDefault + "Denied the invite.");
            VillageVillagesUtils.townInvites.remove(sender);
            return true;
        }
        
        return false;
    }
}

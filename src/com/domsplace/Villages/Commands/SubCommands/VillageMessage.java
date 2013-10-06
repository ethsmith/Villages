package com.domsplace.Villages.Commands.SubCommands;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageMessage extends SubCommand {
    public VillageMessage() {
        super("village", "msg");
        this.addOption("message");
        this.setPermission("message");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        //Make sure it's a player running the command
        if(!isPlayer(sender)) {
            sk(sender, "playeronly");
            return false;
        }
        
        if(args.length < 1) {
            sk(sender, "entermessage");
            return false;
        }
        
        Village v = Village.getPlayersVillage(Resident.getResident(getPlayer(sender)));
        if(v == null) {
            sk(sender, "notinvillage");
            return true;
        }
        
        //Check if Muted (SELBans)
        if(isMuted(getPlayer(sender))) {
            sk(sender, "muted");
            return true;
        }
        
        String message = getVillagePrefix(v) + sender.getName() + ": " + ChatColor.WHITE;
        for(int i = 0; i < args.length; i++) {
            message += args[i];
            if(i < (args.length - 1)) message += " ";
        }
        
        v.broadcast(message);
        return true;
    }
}

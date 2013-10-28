package com.domsplace.Villages.Commands.SubCommands;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageLookup extends SubCommand {
    public VillageLookup() {
        super("village", "lookup");
        this.setPermission("lookup");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sk(sender, "enterplayer");
            return false;
        }
        
        Resident target = Resident.guessResident(args[0]);
        Village v = Village.getPlayersVillage(target);
        if(v == null) {
            sk(sender, "playernotinvillage", target);
            return true;
        }
        sk(sender, "playervillage", target, v);
        return true;
    }
}

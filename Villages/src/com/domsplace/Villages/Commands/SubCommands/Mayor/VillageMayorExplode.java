package com.domsplace.Villages.Commands.SubCommands.Mayor;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageMayorExplode extends SubCommand {
    public VillageMayorExplode() {
        super("village", "mayor", "explode");
        this.setPermission("mayor.explode");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {sk(sender, "playeronly");return true;}
        
        Resident r = Resident.getResident(getPlayer(sender));
        Village v = Village.getPlayersVillage(r);
        if(v == null) {sk(sender, "notinvillage");return true;}
        if(!v.isMayor(r)) {sk(sender, "onlymayorexplode"); return true;}
        
        if(args.length < 1) {
            sk(sender, "villageexplodeconfirm");
            return true;
        }
        
        if(!args[0].equalsIgnoreCase("YES")) return false;
        
        v.explode();
        sk(sender, "villageexploded");
        return true;
    }
}

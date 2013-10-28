package com.domsplace.Villages.Commands.SubCommands.Mayor;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageMayorSetMayor extends SubCommand {
    public VillageMayorSetMayor() {
        super("village", "mayor", "set", "mayor");
        this.setPermission("mayor.setmayor");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {sk(sender, "playeronly");return true;}
        
        Resident r = Resident.getResident(getPlayer(sender));
        Village v = Village.getPlayersVillage(r);
        if(v == null) {sk(sender, "notinvillage");return true;}
        if(!v.isMayor(r)) {sk(sender, "onlymayorsetmayor"); return true;}
        
        if(args.length < 1) {
            sk(sender, "newmayorname");
            return true;
        }
        
        Resident target = Resident.guessResident(args[0]);
        if(target == null) {
            sk(sender, "playernotfound");
            return true;
        }
        
        if(!v.isResident(target)) {
            sk(sender, "notresident", target);
            return true;
        }
        
        if(v.isMayor(target)) {
            sk(sender, "playeralreadymayor");
            return true;
        }
        
        v.setMayor(target);
        v.broadcast(gk("newmayor", target));
        DataManager.saveAll();
        return true;
    }
}

package com.domsplace.Villages.Commands.SubCommands.Mayor;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Objects.Region;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageMayorSetSpawn extends SubCommand {
    public VillageMayorSetSpawn() {
        super("village", "mayor", "set", "spawn");
        this.setPermission("mayor.setspawn");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {sk(sender, "playeronly");return true;}
        
        Resident r = Resident.getResident(getPlayer(sender));
        Village v = Village.getPlayersVillage(r);
        if(v == null) {sk(sender, "notinvillage");return true;}
        if(!v.isMayor(r)) {sk(sender, "onlymayor"); return true;}
        
        Region re = Region.getRegion(getPlayer(sender).getLocation());
        if(re == null) return true;
        
        if(!v.isRegionOverlappingVillage(re)) {
            sk(sender, "notinvillage");
            return true;
        }
        
        v.setSpawn(re);
        sk(sender, "setvillagespawn");
        DataManager.saveAll();
        return true;
    }
}

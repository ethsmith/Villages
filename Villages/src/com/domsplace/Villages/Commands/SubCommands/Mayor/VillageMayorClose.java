package com.domsplace.Villages.Commands.SubCommands.Mayor;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Enums.DeleteCause;
import com.domsplace.Villages.Events.VillageDeletedEvent;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageMayorClose extends SubCommand {
    public VillageMayorClose() {
        super("village", "mayor", "close");
        this.setPermission("mayor.close");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {sk(sender, "playeronly");return true;}
        
        Resident r = Resident.getResident(getPlayer(sender));
        Village v = Village.getPlayersVillage(r);
        if(v == null) {sk(sender, "notinvillage");return true;}
        if(!v.isMayor(r)) {sk(sender, "closevillagenotmayor"); return true;}
        
        bk("villageclosed", v);
        
        //Fire Event
        VillageDeletedEvent event = new VillageDeletedEvent(v, DeleteCause.MAYOR_CLOSE, r);
        event.fireEvent();
        if(event.isCancelled()) return true;
        
        Village.deleteVillage(v);
        DataManager.saveAll();
        return true;
    }
}

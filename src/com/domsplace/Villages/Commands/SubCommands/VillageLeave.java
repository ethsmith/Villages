package com.domsplace.Villages.Commands.SubCommands;

import static com.domsplace.Villages.Bases.Base.getPlayer;
import static com.domsplace.Villages.Bases.Base.isPlayer;
import static com.domsplace.Villages.Bases.Base.sk;
import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Events.ResidentRemovedEvent;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageLeave extends SubCommand {
    public VillageLeave() {
        super("village", "leave");
        this.setPermission("leave");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {sk(sender, "playeronly");return true;}
        
        Resident r = Resident.getResident(getPlayer(sender));
        Village v = Village.getPlayersVillage(r);
        if(v == null) {sk(sender, "notinvillage");return true;}
        
        if(v.isMayor(r)) {
            sk(sender, "leavevillagemayor");
            return true;
        }
        
        ResidentRemovedEvent event = new ResidentRemovedEvent(r, v);
        event.fireEvent();
        if(event.isCancelled()) return true;
        
        v.broadcast(gk("leftvillage", r));
        v.removeResident(r);
        return true;
    }
}

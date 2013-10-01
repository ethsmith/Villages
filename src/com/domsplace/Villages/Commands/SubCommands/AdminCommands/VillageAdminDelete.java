package com.domsplace.Villages.Commands.SubCommands.AdminCommands;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Enums.DeleteCause;
import com.domsplace.Villages.Events.VillageDeletedEvent;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageAdminDelete extends SubCommand {  
    public VillageAdminDelete() {
        super("village", "admin", "delete");
        this.setPermission("admin.delete");
        this.addOption(SubCommand.VILLAGES_OPTION);
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sk(sender, "neednamedelete");
            return false;
        }
        
        Village v = Village.getVillage(args[0]);
        if(v == null) {
            sk(sender, "villagedoesntexist");
            return true;
        }
        
        //Fire Event
        VillageDeletedEvent event = new VillageDeletedEvent(v, DeleteCause.ADMIN_DELETE);
        event.fireEvent();
        if(event.isCancelled()) return true;
        
        sk(sender, "villagedelete", v);
        Village.deleteVillage(v);
        return true;
    }
}

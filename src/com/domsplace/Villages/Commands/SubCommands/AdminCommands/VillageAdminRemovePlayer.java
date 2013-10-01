package com.domsplace.Villages.Commands.SubCommands.AdminCommands;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Events.ResidentRemovedEvent;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageAdminRemovePlayer extends SubCommand {
    public VillageAdminRemovePlayer() {
        super("village", "admin", "remove", "player");
        this.setPermission("admin.removeplayer");
        this.addOption(SubCommand.PLAYERS_OPTION);
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sk(sender, "enterplayer");
            return false;
        }
        
        Resident player = Resident.guessResident(args[0]);
        if(player == null) {
            sk(sender, "playernotfound");
            return true;
        }
        
        Village v = Village.getPlayersVillage(player);
        if(v != null) {
            sk(sender, "playernotinvillage");
            return true;
        }
        
        if(v.isMayor(player)) {
            sk(sender, "cantkickmayor");
            return true;
        }
        
        ResidentRemovedEvent event = new ResidentRemovedEvent(player, v);
        event.fireEvent();
        if(event.isCancelled()) return true;
        
        v.removeResident(player);
        sk(sender, "playerremovedfromvillage", player, v);
        DataManager.saveAll();
        return true;
    }
}

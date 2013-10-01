package com.domsplace.Villages.Commands.SubCommands;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageDeny extends SubCommand {
    public VillageDeny() {
        super("village", "deny");
        this.addOption(SubCommand.PLAYERS_OPTION);
        this.setPermission("deny");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {
            sk(sender, "playeronly");
            return true;
        }
        
        Resident r = Resident.getResident(getPlayer(sender));
        if(r == null) {
            sk(sender, "noinvite");
            return true;
        }
        
        Map<Resident, Village> invites = VillageInvite.VILLAGE_INVITES;
        if(invites == null) {
            sk(sender, "noinvite");
            return true;
        }
        
        if(!invites.containsKey(r))  {
            sk(sender, "noinvite");
            return true;
        }
        
        Village v = invites.get(r);
        if(v == null) {
            sk(sender, "noinvite");
            return true;
        }
        
        sk(sender, "deniedinvite");
        invites.remove(r);
        return true;
    }
}

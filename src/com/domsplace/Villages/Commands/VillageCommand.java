package com.domsplace.Villages.Commands;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageCommand extends BukkitCommand {
    public VillageCommand() {
        super("village");
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) return false;
        
        Resident r = Resident.getResident(getPlayer(sender));
        Village v = Village.getPlayersVillage(r);
        if(v == null) {
            sk(sender, "notinvillage");
            return true;
        }
        
        return this.fakeExecute(sender, "village info");
    }
}

package com.domsplace.Villages.Commands.SubCommands.Bank;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageBankOpen extends SubCommand {
    public VillageBankOpen() {
        super("village", "bank", "open");
        this.setPermission("bank.open");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(!getConfig().getBoolean("features.banks.item", true)) {
            sk(sender, "itembankdisabled");
            return true;
        }
        if(!isPlayer(sender)) {
            sk(sender, "playeronly");
            return false;
        }
        
        Resident r = Resident.getResident(getPlayer(sender));
        Village v = Village.getPlayersVillage(r);
        if(v == null) {sk(sender, "notinvillage");return true;}
        
        getPlayer(sender).openInventory(v.getBank().getGUI());
        return true;
    }
}

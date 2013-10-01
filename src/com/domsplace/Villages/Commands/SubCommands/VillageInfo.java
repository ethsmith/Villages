package com.domsplace.Villages.Commands.SubCommands;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageInfo extends SubCommand {
    public VillageInfo() {
        super("village", "info");
        this.addOption(SubCommand.VILLAGES_OPTION);
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        Village v;
        
        if(args.length < 1 && !isPlayer(sender)) {
            sk(sender, "entervillagename");
            return false;
        } else if(args.length < 1) {
            v = Village.getPlayersVillage(Resident.getResident(getPlayer(sender)));
        } else {
            v = Village.getVillage(args[0]);
        }
        
        if(v == null) {
            sk(sender, "cantfindvillage");
            return true;
        }
        
        List<String> messages = new ArrayList<String>();
        messages.add(ChatImportant + "Info for " + ChatDefault + v.getName());
        messages.add("\tDescription: " + ChatColor.ITALIC + v.getDescription());
        messages.add("\tMayor: " + ChatColor.ITALIC + v.getMayor().getName());
        messages.add("\tResidents: " + ChatColor.ITALIC + listToString(v.getResidentsAsString()));
        messages.add("\tSpawn: " + ChatColor.ITALIC + v.getSpawn().toString());
        messages.add("\tSize: " + ChatColor.ITALIC + v.getRegions().size());
        
        if(getConfig().getBoolean("features.plots", false)) messages.add("\tAvailable Plots: " + (v.getAvailablePlots().size()));
        
        if(Base.useVault) messages.add("\tWealth: " + ChatColor.ITALIC + getMoney(v.getBank().getWealth()));
        
        sendMessage(sender, messages);
        return true;
    }
}

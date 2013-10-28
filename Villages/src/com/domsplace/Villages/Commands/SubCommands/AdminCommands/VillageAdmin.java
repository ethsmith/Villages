package com.domsplace.Villages.Commands.SubCommands.AdminCommands;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageAdmin  extends SubCommand {  
    public VillageAdmin() {
        super("village", "admin");
        this.setPermission("admin.command");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        sendMessage(sender, new String[]{
            ChatColor.BLUE + getPlugin().getName() + ChatColor.GRAY + " Version " + DataManager.PLUGIN_MANAGER.getVersion(),
            ChatColor.GRAY + "\tProgrammed by: " + ChatColor.LIGHT_PURPLE + "Dom",
            ChatColor.GRAY + "\tTested by: " + ChatColor.AQUA + "Jordan",
            ChatColor.GRAY + "\tWebsite: " + ChatColor.GOLD + "http://domsplace.com/",
            ChatColor.GRAY + "\tForums: " + ChatColor.GREEN + "http://oxafemble.me/"
        });
        return true;
    }
}
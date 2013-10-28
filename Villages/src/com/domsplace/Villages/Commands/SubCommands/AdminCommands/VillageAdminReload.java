package com.domsplace.Villages.Commands.SubCommands.AdminCommands;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageAdminReload extends SubCommand {  
    public VillageAdminReload() {
        super("village", "admin", "reload");
        this.setPermission("admin.reload");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        sendMessage(sender, ChatImportant + "Reloading Configuration...");
        if(!DataManager.loadAll()) {sendMessage(sender, ChatError + "Failed to reload data!"); return true;}
        sendMessage(sender, "Reloaded Configuration!");
        return true;
    }
}

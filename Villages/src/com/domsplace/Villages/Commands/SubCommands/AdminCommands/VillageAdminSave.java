package com.domsplace.Villages.Commands.SubCommands.AdminCommands;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageAdminSave extends SubCommand {  
    public VillageAdminSave() {
        super("village", "admin", "save");
        this.setPermission("admin.save");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        sendMessage(sender, ChatImportant + "Saving Data...");
        if(!DataManager.saveAll()) {sendMessage(sender, ChatError + "Failed to save data!"); return true;}
        sendMessage(sender, "Saved Data!");
        return true;
    }
}

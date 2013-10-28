package com.domsplace.Villages.Commands.SubCommands;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageHelp extends SubCommand {
    public VillageHelp() {
        super("village", "help");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        return bkcmd.commandFailed(sender, cmd, label, new String[]{});
    }
}

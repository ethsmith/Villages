package com.domsplace.Villages.Bases;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

public class CommandBase extends BasesBase implements CommandExecutor {
    private static final List<CommandBase> COMMANDS = new ArrayList<CommandBase>();
    
    protected static PluginCommand registerCommand(CommandBase command) {
        CommandBase.getCommands().add(command);
        PluginCommand cmd = getPlugin().getCommand(command.getCommand());
        cmd.setExecutor(command);
        return cmd;
    }
    
    protected static List<CommandBase> getCommands() {
        return CommandBase.COMMANDS;
    }
    
    public static void setAllPermissionMessages(String message) {
        for(CommandBase cmd : CommandBase.getCommands()) {
            cmd.setPermissionMessage(message);
        }
    }
    
    public static void updateAllPermissionMessages() {
        for(CommandBase cmd : CommandBase.getCommands()) {
            cmd.updatePermissionMessage();
        }
    }
    
    public static PluginCommand getRegisteredCommand(String command) {
        for(CommandBase cmd : CommandBase.getCommands()) {
            if(!cmd.getCommand().equalsIgnoreCase(command)) continue;
            return cmd.getCmd();
        }
        return null;
    }
    
    //Instance
    private String command;
    private PluginCommand cmd;
    
    protected CommandBase(String cmd) {
        this.command = cmd;
        this.cmd = CommandBase.registerCommand(this);
        this.commandRegistered();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase(this.getCommand())) {
            if(cmd(sender, cmd, label, args)) {
                commandDone(sender, cmd, label, args);
                return true;
            }
            
            return commandFailed(sender, cmd, label, args);
        }
        
        commandInvalid(sender, cmd, label, args);
        return false;
    }
    
    public String getCommand() {
        return this.command;
    }
    
    public PluginCommand getCmd() {
        return this.cmd;
    }
    
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        return false;
    }
    
    public void commandDone(CommandSender sender, Command cmd, String label, String[] args) {
    }
    
    public boolean commandFailed(CommandSender sender, Command cmd, String label, String[] args) {
        return false;
    }
    
    public void commandInvalid(CommandSender sender, Command cmd, String label, String[] args) {
    }
    
    public void commandRegistered() {
        debug("Regisistered " + this.getCommand() + ".");
    }
    
    protected void setPermissionMessage(String message) {
        this.getCmd().setPermissionMessage(message);
    }
    
    public void updatePermissionMessage() {
        this.setPermissionMessage(gK("nopermission"));
    }
}

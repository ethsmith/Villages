package com.domsplace.Villages.Bases;

import com.domsplace.Villages.Objects.SubCommand;
import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Utils.VillageUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

public class CommandBase extends BaseBase implements CommandExecutor, TabCompleter  {
    private static final List<CommandBase> COMMANDS = new ArrayList<CommandBase>();
    
    protected static PluginCommand registerCommand(CommandBase command) {
        CommandBase.getCommands().add(command);
        PluginCommand cmd = getPlugin().getCommand(command.getCommand());
        cmd.setExecutor(command);
        cmd.setTabCompleter(command);
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
    private List<SubCommand> subCommands;
    
    protected CommandBase(String cmd) {
        this.command = cmd;
        this.subCommands = new ArrayList<SubCommand>();
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase(this.getCommand())) {
            List<String> tab = this.commandTab(sender, cmd, label, args);
            if(tab != null) {
                return tab;
            }
            
            return this.commandTabFailed(sender, cmd, label, args);
        }
        return null;
    }
    
    public String getCommand() {
        return this.command;
    }
    
    public PluginCommand getCmd() {
        return this.cmd;
    }
    
    public List<SubCommand> getSubCommands() {
        return new ArrayList<SubCommand>(this.subCommands);
    }
    
    public List<SubCommand> getSubCommands(int level, String[] args) {
        int currentLevel = 0;
        if(currentLevel == level) {
            return this.getSubCommands();
        }
        
        List<SubCommand> subs = new ArrayList<SubCommand>();
        for(SubCommand sc : this.subCommands) {
            if(!sc.getCommand().equalsIgnoreCase(args[currentLevel])) continue;
            subs.addAll(sc.getSubCommands(level, currentLevel, args));
        }
        
        return subs;
    }
    
    public void addSubCommand(SubCommand cmd) {
        this.subCommands.add(cmd);
    }
    
    public void removeSubCommand(SubCommand cmd) {
        this.subCommands.remove(cmd);
    }
    
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        return false;
    }
    
    public boolean commandFailed(CommandSender sender, Command cmd, String label, String[] args) {
        msgPlayer(sender, ChatImportant + "Help:");
        String c = ChatDefault + "\t";
        if(isPlayer(sender)) c += "/";
        c += label + " ";
        for(SubCommand sc : this.getSubCommands()) {
            sc.print(c, sender);
        }
        return true;
    }
    
    public void commandDone(CommandSender sender, Command cmd, String label, String[] args) {
    }
    
    public void commandInvalid(CommandSender sender, Command cmd, String label, String[] args) {
    }
    
    public void commandRegistered() {
        debug("Regisistered Command \"" + this.getCommand() + "\"");
    }
    
    public MemorySection getPluginMemorySection() {
        return (MemorySection) DataManagerBase.PLUGIN_MANAGER.PluginYML.get("commands." + this.getCmd().getName());
    }
    
    public List<String> commandTab(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> validArgs = new ArrayList<String>();
        for(SubCommand sc : this.getSubCommands(args.length - 1, args)) {
            if(sc.compare(SubCommand.PLAYER)) {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    if(!canSee(sender, p)) continue;
                    validArgs.add(p.getName());
                }
            } else if(sc.compare(SubCommand.VILLAGE)) {
                for(Village v : VillageUtils.getVillages()) {
                    validArgs.add(v.getName());
                }
            } else {
                validArgs.add(sc.getCommand());
            }
        }
        
        List<String> returnArgs = new ArrayList<String>();
        for(String s : validArgs) {
            if(!s.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) continue;
            returnArgs.add(s);
        }
        if(returnArgs.isEmpty()) return validArgs;
        return returnArgs;
    }
    
    public List<String> commandTabFailed(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }
    
    protected void setPermissionMessage(String message) {
        this.getCmd().setPermissionMessage(message);
    }
    
    public void updatePermissionMessage() {
        this.setPermissionMessage(gK("nopermission"));
    }
}

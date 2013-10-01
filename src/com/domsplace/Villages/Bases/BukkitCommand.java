package com.domsplace.Villages.Bases;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

public class BukkitCommand extends Base implements CommandExecutor, TabCompleter {
    private static final List<BukkitCommand> COMMANDS = new ArrayList<BukkitCommand>();
    
    private static PluginCommand registerCommand(BukkitCommand command) {
        PluginCommand cmd = getPlugin().getCommand(command.getCommand());
        cmd.setExecutor(command);
        cmd.setPermission(colorise(Base.getPermissionMessage()));
        cmd.setTabCompleter(command);
        COMMANDS.add(command);
        return cmd;
    }
    
    public static List<BukkitCommand> getCommands() {return new ArrayList<BukkitCommand>(COMMANDS);}

    public static BukkitCommand getCommand(String command) {
        for(BukkitCommand bc : COMMANDS) {
            if(!bc.getCommand().equalsIgnoreCase(command)) continue;
            return bc;
        }
        return null;
    }
    
    //Instance
    private String command;
    private PluginCommand cmd;
    private List<SubCommand> subCommands;
    
    public BukkitCommand(String command) {
        this.command = command;
        this.cmd = BukkitCommand.registerCommand(this);
        this.subCommands = new ArrayList<SubCommand>();
        registerCommand(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase(this.command)) {
            if(!hasPermission(sender, cmd.getPermission())) return noPermission(sender, cmd, label, args);
            if(!inVillageWorld(sender)) {
                sk(sender, "notinthisworld");
                return true;
            }
            
            SubCommand sc = getSubCommand(args, sender);
            boolean result;
            if(sc != null) {
                result = sc.transExecute(this, sender, cmd, label, args);
            } else {
                result = this.cmd(sender, cmd, label, args);
            }
            if(!result) return commandFailed(sender, cmd, label, args); 
            return commandSuccess(sender, cmd, label, args);
        }
        
        return badCommand(sender, cmd, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase(this.getCommand())) {
            List<String> tab = this.tab(sender, cmd, label, args);
            if(tab != null) {
                return tabSuccess(sender, cmd, label, args, tab);
            }
            
            return this.tabFailed(sender, cmd, label, args);
        }
        return badTab(sender, cmd, label, args);
    }
    
    public String getCommand() { return this.command; }
    public PluginCommand getCmd() {return this.cmd;}
    public List<SubCommand> getSubCommands() {return this.subCommands;}
    
    public boolean badCommand(CommandSender sender, Command cmd, String label, String[] args) {return false;}
    public boolean commandSuccess(CommandSender sender, Command cmd, String label, String[] args) {return true;}
    public boolean commandFailed(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> options = new ArrayList<String>();
        String lbl = "";
        if(args.length < 2) {
            for(SubCommand sc : this.subCommands) {
                if(!hasPermission(sender, sc.getPermission())) continue;
                options.add(sc.getCommand());
            }
            if(args.length > 0) lbl = args[args.length - 1];
        } else {
            lbl = args[args.length - 1];
            String[] c = new String[args.length - 1];
            for(int i = 0; i < c.length; i++) {
                c[i] = args[i];
            }
            
            SubCommand sc = this.getSubCommand(c, sender);
            if(sc != null) options.addAll(sc.getOptions());
        }
        
        List<String> matches = new ArrayList<String>();
        for(String s : options) {
            if(!s.toLowerCase().startsWith(lbl.toLowerCase())) continue;
            matches.add(s);
        }
        
        if(matches.size() < 1) matches = options;
        
        String base = "";
        if(isPlayer(base)) base += "/";
        base += label + " ";
        int max = args.length - 1;
        if(max < 0) max = 0;
        for(int i = 0; i < max; i++) {
            base += args[i] + " ";
        }
        
        List<String> messages = new ArrayList<String>();
        messages.add(ChatImportant + "Command Help:");
        for(String s : matches) {
            messages.add(ChatDefault + "\t" + base + s);
        }
        
        sendMessage(sender, messages);
        return true;
    }
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {return false;}
    
    public List<String> tab(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> options = new ArrayList<String>();
        String lbl = "";
        if(args.length < 2) {
            for(SubCommand sc : this.subCommands) {
                if(!hasPermission(sender, sc.getPermission())) continue;
                options.add(sc.getCommand());
            }
            if(args.length > 0) lbl = args[args.length - 1];
        } else {
            lbl = args[args.length - 1];
            String[] c = new String[args.length - 1];
            for(int i = 0; i < c.length; i++) {
                c[i] = args[i];
            }
            
            SubCommand sc = this.getSubCommand(c, sender);
            if(sc != null) options.addAll(sc.getOptions());
        }
        
        List<String> matches = new ArrayList<String>();
        for(String s : options) {
            if(!s.toLowerCase().startsWith(lbl.toLowerCase())) continue;
            matches.add(s);
        }
        
        if(matches.size() < 1) return options;
        return matches;
    }
    public List<String> tabFailed(CommandSender sender, Command cmd, String label, String[] args) {return null;}
    public List<String> badTab(CommandSender sender, Command cmd, String label, String[] args) {return null;}
    public List<String> tabSuccess(CommandSender sender, Command cmd, String label, String[] args, List<String> successValue) {return successValue;}
    
    public boolean noPermission(CommandSender sender, Command cmd, String label, String[] args) {
        cmd.setPermissionMessage(colorise(Base.getPermissionMessage()));
        sender.sendMessage(cmd.getPermissionMessage());
        return true;
    }
    
    public void addSubCommand(SubCommand cmd) {this.subCommands.add(cmd);}
    
    public SubCommand getSubCommand(String[] args, CommandSender sender) {
        if(args.length < 1) return null;
        SubCommand bestMatch = null;
        
        for(SubCommand sc : this.subCommands) {
            if(!hasPermission(sender, sc.getPermission())) continue;
            int m = sc.getMatches(args);
            if(m == 0) continue;
            if(bestMatch != null && m <= bestMatch.getMatches(args)) continue;
            bestMatch = sc;
        }
        
        return bestMatch;
    }
    
    public boolean fakeExecute(CommandSender sender, String commandLine) {
        if(commandLine.startsWith("/")) commandLine = commandLine.replaceFirst("/", "");
        
        String[] s = commandLine.split(" ");
        if(s.length < 1) return false;
        
        String lbl = s[0];
        String[] args = new String[0];
        if(s.length > 1) {
            args = new String[s.length - 1];
            
            for(int i = 1; i < s.length; i++) {
                args[i-1] = s[i];
            }
        }
        
        return this.onCommand(sender, cmd, lbl, args);
    }

    public void removeSubCommand(SubCommand aThis) {
        this.subCommands.remove(aThis);
    }
}

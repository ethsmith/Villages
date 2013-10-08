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
        cmd.setPermissionMessage(colorise(Base.getPermissionMessage()));
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
    private List<SubCommandOption> subOptions;
    
    public BukkitCommand(String command) {
        this.command = command;
        this.cmd = BukkitCommand.registerCommand(this);
        this.subCommands = new ArrayList<SubCommand>();
        this.subOptions = new ArrayList<SubCommandOption>();
        registerCommand(this);
    }
    
    public String getCommand() { return this.command; }
    public PluginCommand getCmd() {return this.cmd;}
    public List<SubCommand> getSubCommands() {return this.subCommands;}
    public List<SubCommandOption> getSubCommandOptions() {return new ArrayList<SubCommandOption>(this.subOptions);}
    
    public void addSubCommandOption(SubCommandOption o) {this.subOptions.add(o);}
    public void removeSubCommandOption(SubCommandOption o) {this.subOptions.remove(o);}
    
    public void addSubCommand(SubCommand cmd) {this.subCommands.add(cmd);}
    public void removeSubCommand(SubCommand aThis) {this.subCommands.remove(aThis);}

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
    
    public boolean badCommand(CommandSender sender, Command cmd, String label, String[] args) {return false;}
    public boolean commandSuccess(CommandSender sender, Command cmd, String label, String[] args) {return true;}
    public boolean commandFailed(CommandSender sender, Command cmd, String label, String[] args) {
        //TODO: Add Command Help
        return false;
    }
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {return false;}
    
    public List<String> tab(CommandSender sender, Command cmd, String label, String[] args) {return this.getArgumentGuesses(args);}
    public List<String> tabFailed(CommandSender sender, Command cmd, String label, String[] args) {return null;}
    public List<String> badTab(CommandSender sender, Command cmd, String label, String[] args) {return null;}
    public List<String> tabSuccess(CommandSender sender, Command cmd, String label, String[] args, List<String> successValue) {return successValue;}
    
    public boolean noPermission(CommandSender sender, Command cmd, String label, String[] args) {
        cmd.setPermissionMessage(colorise(Base.getPermissionMessage()));
        sender.sendMessage(cmd.getPermissionMessage());
        return true;
    }
    
    public SubCommand getSubCommand(String[] args, CommandSender sender) {
        if(args.length < 1) return null;
        SubCommand bestMatch = null;
        
        for(SubCommand sc : this.subCommands) {
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
    
    public List<String> getArgumentGuesses(String[] args) {
        List<String> options = new ArrayList<String>();
        if(args.length == 0) {
            for(SubCommandOption sco : this.subOptions) {
                options.addAll(sco.getOptionsFormatted());
            }
        } else if(args.length == 1) {
            for(SubCommandOption sco : this.subOptions) {
                for(String s : sco.getOptionsFormatted()) {
                    if(!s.toLowerCase().startsWith(args[0].toLowerCase())) continue;
                    options.add(s);
                }
            }
        } else if(args.length > 1) {            
            List<String> matches = new ArrayList<String>();
            
            for(SubCommandOption sco : this.subOptions) {
                if(!sco.getOption().toLowerCase().startsWith(args[0].toLowerCase())) continue;
                matches.addAll(sco.tryFetch(args, 1));
            }
            
            if(args[args.length - 1].replaceAll(" ", "").equalsIgnoreCase("")) return matches;
            
            List<String> closeMatch = new ArrayList<String>();
            
            for(String match : matches) {
                if(match.toLowerCase().startsWith(args[args.length-1].toLowerCase())) closeMatch.add(match);
            }
            
            options.addAll(closeMatch);
        }
        return options;
    }
}

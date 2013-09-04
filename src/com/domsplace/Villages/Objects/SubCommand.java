package com.domsplace.Villages.Objects;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.CommandBase;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;

public class SubCommand extends Base {
    public static final SubCommand PLAYER = new SubCommand("[player]");
    public static final SubCommand VILLAGE = new SubCommand("[village]");
    
    public static SubCommand make(String cmd) {
        return new SubCommand(cmd);
    }
    
    public static SubCommand make(SubCommand cmd) {
        return new SubCommand(cmd);
    }
    
    public static SubCommand make(String cmd, String... args) {
        SubCommand c = new SubCommand(cmd);
        
        for(String s : args) {
            c.addSubCommand(SubCommand.make(s));
        }
        
        return c;
    }
    
    public static SubCommand make(String cmd, SubCommand... args) {
        SubCommand c = new SubCommand(cmd);
        
        for(SubCommand s : args) {
            c.addSubCommand(SubCommand.make(s));
        }
        
        return c;
    }
    
    public static SubCommand make(SubCommand cmd, String... args) {
        SubCommand c = new SubCommand(cmd);
        
        for(String s : args) {
            c.addSubCommand(SubCommand.make(s));
        }
        
        return c;
    }
    
    public static SubCommand make(SubCommand cmd, SubCommand... args) {
        SubCommand c = new SubCommand(cmd);
        
        for(SubCommand s : args) {
            c.addSubCommand(SubCommand.make(s));
        }
        
        return c;
    }
    
    //Instance
    private String command;
    private List<SubCommand> subCommands;
    
    public SubCommand(String command) {
        this.command = command;
        this.subCommands = new ArrayList<SubCommand>();
    }
    
    public SubCommand(SubCommand command) {
        this(command.getCommand());
        for(SubCommand c : command.getSubCommands()) {
            this.subCommands.add(new SubCommand(c));
        }
    }
    
    public List<SubCommand> getSubCommands() {
        return new ArrayList<SubCommand>(this.subCommands);
    }
    
    public List<SubCommand> getSubCommands(int currentLevel, int targetLevel, String[] args) {
        currentLevel++;
        if(currentLevel >= targetLevel) return this.getSubCommands();
        List<SubCommand> scs = new ArrayList<SubCommand>();
        for(SubCommand sc : this.subCommands) {
            if(!sc.getCommand().equalsIgnoreCase(args[currentLevel])) continue;
            scs.addAll(sc.getSubCommands(currentLevel, targetLevel, args));
        }
        return scs;
    }
    
    public void addSubCommand(SubCommand cmd) {
        this.subCommands.add(cmd);
    }
    
    public void removeSubCommand(SubCommand cmd) {
        this.subCommands.remove(cmd);
    }
    
    public String getCommand() {
        return this.command;
    }

    public void print(String start, CommandSender sender) {
        start += this.getCommand() + " ";
        msgPlayer(sender, start);
        for(SubCommand sc : this.getSubCommands()) sc.print(start, sender);
    }
    
    public boolean compare(SubCommand sc) {
        return sc.getCommand().equalsIgnoreCase(this.getCommand());
    }
}

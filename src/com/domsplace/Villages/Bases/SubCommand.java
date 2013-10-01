package com.domsplace.Villages.Bases;

import com.domsplace.Villages.Objects.Village;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubCommand extends Base {
    public static final String VILLAGES_OPTION = "[village]";
    public static final String PLAYERS_OPTION = "[player]";
    
    private String[] subs;
    private BukkitCommand cmd;
    private List<String> options;
    private String permission = "none";
    
    public SubCommand(String cmd, String... extenders) {
        this.subs = extenders;
        this.cmd = BukkitCommand.getCommand(cmd);
        this.options = new ArrayList<String>();
        if(this.cmd != null) this.cmd.addSubCommand(this);
    }
    
    public String[] getSubs() {return this.subs;}
    public BukkitCommand getCmd() {return this.cmd;}
    public String getCommand() {return this.getSubs()[this.getSubs().length - 1];}
    public String getPermission() {return "Villages." + this.permission;}
    
    public void setPermission(String permission) {this.permission = permission;}
    
    public void deRegister() {
        this.cmd.removeSubCommand(this);
    }
    
    public void addOption(String... options) {
        for(String s : options) {
            if(s.equals(VILLAGES_OPTION)) {
                this.options.addAll(Village.getVillageNames());
                continue;
            }
            
            if(s.equals(PLAYERS_OPTION)) {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    this.options.add(p.getName());
                }
                continue;
            }
            
            this.options.add(s);
        }
    }
    
    public List<String> getOptions() {return new ArrayList<String>(this.options);}

    public int getMatches(String[] args) {
        int i = 0;
        
        for(int x = 0; x < args.length; x++) {
            try {
                if(args[x].replaceAll(" ", "").equalsIgnoreCase("")) continue;
                if(!args[x].equalsIgnoreCase(this.subs[x])) {i--; continue;}
                i++;
            } catch(IndexOutOfBoundsException e) {
                break;
            }
        }
        
        return i;
    }

    public boolean transExecute(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        List<String> c = new ArrayList<String>();
        for(int i = 0; i < args.length; i++) {
            if((i < this.subs.length) && args[i].equalsIgnoreCase(this.subs[i])) continue;
            c.add(args[i]);
        }
        
        String[] cargs = Base.listToArray(c);
        
        return this.tryCmd(bkcmd, sender, cmd, label, cargs);
    }
    
    public boolean tryCmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(!hasPermission(sender, getPermission())) return bkcmd.noPermission(sender, cmd, label, args);
        return cmd(bkcmd, sender, cmd, label, args);
    }
    
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        return false;
    }
}

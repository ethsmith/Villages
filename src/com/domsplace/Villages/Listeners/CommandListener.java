package com.domsplace.Villages.Listeners;

import com.domsplace.Villages.Events.VillageCreatedEvent;
import com.domsplace.Villages.Events.VillageDeletedEvent;
import com.domsplace.Villages.Events.MayorDeathEvent;
import com.domsplace.Villages.Events.VillagePlayerAddedEvent;
import com.domsplace.Villages.Events.VillagePlayerRemovedEvent;
import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.CommandBase;
import com.domsplace.Villages.Bases.ListenerBase;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener extends ListenerBase {
    public static String formatCommand(String message, Village village, OfflinePlayer player) {
        if(village != null) {
            message = message.replaceAll("%v%", village.getName());
        }
        
        if(player != null) {
            message = message.replaceAll("%p%", player.getName());
        }
        
        return message;
    }
    
    @EventHandler
    public void onVillageCreate(VillageCreatedEvent e) {
        for(String command : Base.villageCreatedCommands) {
            command = formatCommand(command, e.getVillage(), e.getPlayer());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
    
    @EventHandler
    public void onPlayerJoinVillage(VillagePlayerAddedEvent e) {
        for(String command : Base.villagePlayerAddedCommands) {
            command = formatCommand(command, e.getVillage(), e.getPlayer());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
    
    @EventHandler
    public void onPlayerLeaveVillage(VillagePlayerRemovedEvent e) {
        for(String command : Base.villagePlayerRemovedCommands) {
            command = formatCommand(command, e.getVillage(), e.getPlayer());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
    
    @EventHandler
    public void onVillageDeleted(VillageDeletedEvent e) {
        for(String command : Base.villageDeletedCommands) {
            command = formatCommand(command, e.getVillage(), null);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
    
    @EventHandler
    public void onMayorDeath(MayorDeathEvent e) {
        for(String command : Base.villageMayorDeathCommands) {
            command = formatCommand(command, e.getVillage(), e.getPlayer());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
    
    @EventHandler(ignoreCancelled=true)
    public void commandPreprocessEvent(PlayerCommandPreprocessEvent e) {
        if(getConfig().getBoolean("use.vanish")) {
            return;
        }
        
        String tc = "v";
        
        if(!e.getMessage().toLowerCase().startsWith("/" + tc.toLowerCase())) {
                return;
        }
            
        if(!e.getMessage().toLowerCase().replaceAll("/" + tc.toLowerCase(), "").startsWith(" ")) {
            if(!e.getMessage().toLowerCase().replaceAll("/" + tc.toLowerCase(), "").equalsIgnoreCase("")) {
                return;
            }
        }
        
        //Handle the Village Command
        e.setCancelled(true);
        
        String[] args = e.getMessage().split(" ");
        List<String> fargs = new ArrayList<String>();
        for(String a : args) {
            if(a == "") {
                continue;
            }
            fargs.add(a);
        }
        
        args = new String[0];
        try {
            args = new String[fargs.size()-1];
            for(int i = 1 ; i < fargs.size(); i++) {
                args[i-1] = fargs.get(i);
            }
        } catch(Exception ex) {
            args = new String[0];
        }
        
        CommandBase.getRegisteredCommand("village").execute(e.getPlayer(), "v", args);
    }
}

package com.domsplace.Listeners;

import com.domsplace.DataManagers.VillageConfigManager;
import com.domsplace.Events.VillageCreatedEvent;
import com.domsplace.Events.VillageDeletedEvent;
import com.domsplace.Events.VillageMayorDeathEvent;
import com.domsplace.Events.VillagePlayerAddedEvent;
import com.domsplace.Events.VillagePlayerRemovedEvent;
import com.domsplace.Objects.Village;
import com.domsplace.Utils.VillageUtils;
import com.domsplace.VillageBase;
import com.domsplace.VillagesPlugin;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class VillageCommandsListener extends VillageBase implements Listener {
    
    private VillagesPlugin plugin;
    
    public VillageCommandsListener(VillagesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onVillageCreate(VillageCreatedEvent e) {
        for(String command : VillageBase.villageCreatedCommands) {
            command = formatCommand(command, e.getVillage(), e.getPlayer());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
    
    @EventHandler
    public void onPlayerJoinVillage(VillagePlayerAddedEvent e) {
        for(String command : VillageBase.villagePlayerAddedCommands) {
            command = formatCommand(command, e.getVillage(), e.getPlayer());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
    
    @EventHandler
    public void onPlayerLeaveVillage(VillagePlayerRemovedEvent e) {
        for(String command : VillageBase.villagePlayerRemovedCommands) {
            command = formatCommand(command, e.getVillage(), e.getPlayer());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
    
    @EventHandler
    public void onVillageDeleted(VillageDeletedEvent e) {
        for(String command : VillageBase.villageDeletedCommands) {
            command = formatCommand(command, e.getVillage(), null);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
    
    @EventHandler
    public void onMayorDeath(VillageMayorDeathEvent e) {
        for(String command : VillageBase.villageMayorDeathCommands) {
            command = formatCommand(command, e.getVillage(), e.getPlayer());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
    
    public static String formatCommand(String message, Village village, OfflinePlayer player) {
        if(village != null) {
            message = message.replaceAll("%v%", village.getName());
        }
        
        if(player != null) {
            message = message.replaceAll("%p%", player.getName());
        }
        
        return message;
    }
    
    @EventHandler(ignoreCancelled=true)
    public void commandPreprocessEvent(PlayerCommandPreprocessEvent e) {
        if(VillageConfigManager.config.getBoolean("use.vanish")) {
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
        
        VillageUtils.plugin.getCommand("village").execute(e.getPlayer(), "v", args);
    }
}

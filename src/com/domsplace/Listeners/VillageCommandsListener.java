package com.domsplace.Listeners;

import com.domsplace.Events.VillageCreatedEvent;
import com.domsplace.Events.VillageDeletedEvent;
import com.domsplace.Events.VillagePlayerAddedEvent;
import com.domsplace.Events.VillagePlayerRemovedEvent;
import com.domsplace.Objects.Village;
import com.domsplace.VillageBase;
import com.domsplace.VillagesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
    
    public static String formatCommand(String message, Village village, OfflinePlayer player) {
        if(village != null) {
            message = message.replaceAll("%v%", village.getName());
        }
        
        if(player != null) {
            message = message.replaceAll("%p%", player.getName());
        }
        
        return message;
    }
}

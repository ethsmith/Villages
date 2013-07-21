package com.domsplace.Events;

import com.domsplace.Objects.Village;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VillagePlayerAddedEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    private OfflinePlayer player;
    private Village village;
    private boolean isCancelled = false;
    
    public VillagePlayerAddedEvent (OfflinePlayer player, Village village) {
        this.player = player;
        this.village = village;
    }
    
    public OfflinePlayer getPlayer() {
        return this.player;
    }
    
    public Village getVillage() {
        return this.village;
    }
    
    public boolean isCancelled() {
        return isCancelled;
    }
    
    public void setCancelled(boolean result) {
        this.isCancelled = result;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
    
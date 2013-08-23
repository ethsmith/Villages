package com.domsplace.Events;

import com.domsplace.Objects.Village;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VillageDeletedEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    private Village village;
    private boolean isCancelled = false;
    
    public VillageDeletedEvent (Village village) {
        this.village = village;
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
    
package com.domsplace.Events;

import com.domsplace.Objects.Village;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VillageCreatedEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Village village;
    private boolean isCancelled = false;
    
    public VillageCreatedEvent (Player mayor, Village village) {
        this.player = mayor;
        this.village = village;
    }
    
    public Player getPlayer() {
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
    
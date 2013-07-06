package com.domsplace.Events;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VillageGriefEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Block block;
    private boolean isCancelled = false;
    
    public VillageGriefEvent (Player griefer, Block mainBlock, List<Block> blocks) {
        if(!blocks.contains(mainBlock)) {
            blocks.add(mainBlock);
        }
        
        this.player = griefer;
        this.block = mainBlock;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Block getBlock() {
        return this.block;
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

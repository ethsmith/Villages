package com.domsplace.Events;

import com.domsplace.Objects.VillageGriefType;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VillageGriefEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Block block;
    private boolean isCancelled = false;
    private VillageGriefType type;
    
    public VillageGriefEvent (Player griefer, Block mainBlock, List<Block> blocks, VillageGriefType type) {
        if(!blocks.contains(mainBlock)) {
            blocks.add(mainBlock);
        }
        
        this.player = griefer;
        this.block = mainBlock;
        this.type = type;
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

    public VillageGriefType getType() {
        return this.type;
    }
}

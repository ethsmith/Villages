package com.domsplace.Villages.Events;

import com.domsplace.Villages.Bases.CancellableEventBase;
import com.domsplace.Villages.Enums.GriefType;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class GriefEvent extends CancellableEventBase {
    private Player player;
    private Block block;
    private GriefType type;
    
    public GriefEvent (Player griefer, Block mainBlock, List<Block> blocks, GriefType type) {
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
    
    public GriefType getType() {
        return this.type;
    }
}

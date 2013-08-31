package com.domsplace.Villages.Events;

import com.domsplace.Villages.Bases.CancellableEventBase;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.OfflinePlayer;

public class VillagePlayerRemovedEvent extends CancellableEventBase {
    private OfflinePlayer player;
    private Village village;
    
    public VillagePlayerRemovedEvent (OfflinePlayer player, Village village) {
        this.player = player;
        this.village = village;
    }
    
    public OfflinePlayer getPlayer() {
        return this.player;
    }
    
    public Village getVillage() {
        return this.village;
    }
}
    
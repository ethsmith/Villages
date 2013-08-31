package com.domsplace.Villages.Events;

import com.domsplace.Villages.Bases.CancellableEventBase;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.entity.Player;

public class MayorDeathEvent extends CancellableEventBase {
    private Player player;
    private Village village;
    
    public MayorDeathEvent (Player mayor, Village village) {
        this.player = mayor;
        this.village = village;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Village getVillage() {
        return this.village;
    }
}
    
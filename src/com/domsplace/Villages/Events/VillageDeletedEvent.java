package com.domsplace.Villages.Events;

import com.domsplace.Villages.Bases.CancellableEventBase;
import com.domsplace.Villages.Objects.Village;

public class VillageDeletedEvent extends CancellableEventBase {
    private Village village;
    
    public VillageDeletedEvent (Village village) {
        this.village = village;
    }
    
    public Village getVillage() {
        return this.village;
    }
}
    
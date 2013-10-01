package com.domsplace.Villages.Events;

import com.domsplace.Villages.Bases.CancellableEvent;
import com.domsplace.Villages.Enums.DeleteCause;
import com.domsplace.Villages.Objects.Village;

public class VillageDeletedEvent extends CancellableEvent {
    private Village village;
    private DeleteCause cause;
    
    public VillageDeletedEvent(Village village, DeleteCause cause) {
       this.village = village;
       this.cause = cause;
    }
    
    public Village getVillage() {return this.village;}
    public DeleteCause getCause() {return this.cause;}
}

package com.domsplace.Villages.Events;

import com.domsplace.Villages.Bases.CancellableEvent;
import com.domsplace.Villages.Enums.DeleteCause;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;

public class VillageDeletedEvent extends CancellableEvent {
    private Village village;
    private DeleteCause cause;
    private Resident closer;
    
    public VillageDeletedEvent(Village village, DeleteCause cause, Resident closer) {
       this.village = village;
       this.cause = cause;
       this.closer = closer;
    }
    
    public Village getVillage() {return this.village;}
    public DeleteCause getCause() {return this.cause;}
    public Resident getCloser() {return this.closer;}
}

package com.domsplace.Villages.Events;

import com.domsplace.Villages.Bases.CancellableEvent;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;

public class VillageCreatedEvent extends CancellableEvent {
    private Resident resident;
    private Village village;
    
    public VillageCreatedEvent(Resident resident, Village village) {
        this.resident = resident;
        this.village = village;
    }
    
    public Resident getResident() {return this.resident;}
    public Village getVillage() {return this.village;}
}

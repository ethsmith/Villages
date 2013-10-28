package com.domsplace.Villages.Listeners;

import com.domsplace.Villages.Bases.VillageListener;
import com.domsplace.Villages.Events.PvPEvent;
import com.domsplace.Villages.Objects.Region;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.event.EventHandler;

public class VillagePvPListener extends VillageListener {
    @EventHandler
    public void handleVillageSameVillagePvP(PvPEvent e) {
        if(getConfig().getBoolean("protection.pvp.village.samevillage", true)) return;
        Region location = Region.getRegion(e.getAttacker());
        if(Village.getOverlappingVillage(location) == null) return;
        Resident attacked = Resident.getResident(e.getAttackedPlayer());
        Resident attacker = Resident.getResident(e.getAttacker());
        Village attackedVillage = Village.getPlayersVillage(attacked);
        if(attackedVillage == null) return;
        Village attackerVillage = Village.getPlayersVillage(attacker);
        if(attackerVillage == null) return;
        if(!attackerVillage.equals(attackedVillage)) return;
        e.setCancelled(true);
        sk(e.getAttacker(), "cantattacksamevillage");
    }
    
    @EventHandler
    public void handleVillageDifferentVillagePvP(PvPEvent e) {
        if(getConfig().getBoolean("protection.pvp.village.differentvillage", true)) return;
        Region location = Region.getRegion(e.getAttacker());
        if(Village.getOverlappingVillage(location) == null) return;
        Resident attacked = Resident.getResident(e.getAttackedPlayer());
        Resident attacker = Resident.getResident(e.getAttacker());
        Village attackedVillage = Village.getPlayersVillage(attacked);
        if(attackedVillage == null) return;
        Village attackerVillage = Village.getPlayersVillage(attacker);
        if(attackerVillage == null) return;
        if(attackerVillage.equals(attackedVillage)) return;
        e.setCancelled(true);
        sk(e.getAttacker(), "cantattackdifferentvillage");
    }
    
    @EventHandler
    public void handleVillageWildernessPvP(PvPEvent e) {
        if(getConfig().getBoolean("protection.pvp.village.notinvillage", true)) return;
        Region location = Region.getRegion(e.getAttacker());
        if(Village.getOverlappingVillage(location) == null) return;
        Resident attacked = Resident.getResident(e.getAttackedPlayer());
        Resident attacker = Resident.getResident(e.getAttacker());
        Village attackedVillage = Village.getPlayersVillage(attacked);
        Village attackerVillage = Village.getPlayersVillage(attacker);
        if(attackerVillage != null && attackedVillage != null) return;
        e.setCancelled(true);
        sk(e.getAttacker(), "cantattackwilderness");
    }
}

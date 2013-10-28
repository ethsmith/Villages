package com.domsplace.Villages.Listeners;

import com.domsplace.Villages.Bases.VillageListener;
import com.domsplace.Villages.Objects.Region;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class MobSpawningListener extends VillageListener {
    @EventHandler(ignoreCancelled=true)
    public void handleVillageMobSpawning(CreatureSpawnEvent e) {
        if(e.getEntity() == null) return;
        if(e.getLocation() == null) return;
        if(e.getSpawnReason() == null) return;
        if(e.getSpawnReason().equals(SpawnReason.SPAWNER_EGG)) return;
        if(e.getSpawnReason().equals(SpawnReason.BREEDING)) return;
        
        String mobspawningkey = "protection.mobspawning.village." + e.getEntity().getType().getName();
        if(getConfig().getBoolean(mobspawningkey, true)) return;
        
        Region r = Region.getRegion(e.getLocation());
        Village v = Village.getOverlappingVillage(r);
        if(v == null) return;
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled=true)
    public void handleWildernessMobSpawning(CreatureSpawnEvent e) {
        if(e.getEntity() == null) return;
        if(e.getLocation() == null) return;
        if(e.getSpawnReason() == null) return;
        if(e.getSpawnReason().equals(SpawnReason.SPAWNER_EGG)) return;
        if(e.getSpawnReason().equals(SpawnReason.BREEDING)) return;
        
        String mobspawningkey = "protection.mobspawning.wilderness." + e.getEntity().getType().getName();
        if(getConfig().getBoolean(mobspawningkey, true)) return;
        
        Region r = Region.getRegion(e.getLocation());
        Village v = Village.getOverlappingVillage(r);
        if(v != null) return;
        e.setCancelled(true);
    }
}

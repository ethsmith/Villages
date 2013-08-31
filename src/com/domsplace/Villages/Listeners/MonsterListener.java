package com.domsplace.Villages.Listeners;

import com.domsplace.Villages.Bases.ListenerBase;
import com.domsplace.Villages.Utils.VillageTypeUtils;
import com.domsplace.Villages.Utils.VillageUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MonsterListener extends ListenerBase {
    @EventHandler(ignoreCancelled=true)
    public void DisableMonsterSpawningInWilderness(CreatureSpawnEvent e) {
        if(e.getEntity() == null) return;
        if(e.isCancelled()) return;
        if(!getConfig().getBoolean("disable.mobspawning.wilderness")) return;
        if(!VillageTypeUtils.isTypeMonster(e.getEntityType())) return;
        if(!VillageUtils.isVillageWorld(e.getEntity().getWorld())) return;
        if(VillageUtils.getVillageFromChunk(e.getEntity().getLocation().getChunk()) != null) return;
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled=true)
    public void DisableMonsterSpawningInVillage(CreatureSpawnEvent e) {
        if(e.getEntity() == null) return;
        if(e.isCancelled()) return;
        if(!getConfig().getBoolean("disable.mobspawning.village")) return;
        if(!VillageTypeUtils.isTypeMonster(e.getEntityType())) return;
        if(!VillageUtils.isVillageWorld(e.getEntity().getWorld())) return;
        if(VillageUtils.getVillageFromChunk(e.getEntity().getLocation().getChunk()) == null) return;
        e.setCancelled(true);
    }
}

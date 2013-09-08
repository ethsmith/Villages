package com.domsplace.Villages.Hooks;

import com.domsplace.Villages.Bases.PluginHookBase;
import com.domsplace.Villages.Objects.Village;

import com.domsplace.Villages.Utils.VillageUtils;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class WorldGuardHook extends PluginHookBase {
    public static WorldGuardHook instance;
    
    public WorldGuardHook() {
        super("WorldGuard");
        WorldGuardHook.instance = this;
    }
    
    public WorldGuardPlugin getWorldGuard() {
        return (WorldGuardPlugin) this.getHookedPlugin();
    }

    public boolean doesVillageOverlapRegion(Village v) {
        if(!this.isHooked()) return false;
        
        com.sk89q.worldguard.bukkit.WorldGuardPlugin plugin = getWorldGuard();
        
        if(plugin == null) {
            return false;
        }
        
        Player mayor = v.getMayor().getPlayer();
        
        for(Chunk c : v.getTownArea()) {
            if(!isChunkInRegion(c)) continue;
            return true;
        }
        
        return false;
    }

    public boolean isChunkInRegion(Chunk chunk) {
        if(!this.isHooked()) return false;
        
        WorldGuardPlugin plugin = getWorldGuard();
        
        if(plugin == null) {
            return false;
        }
        
        for(com.sk89q.worldguard.protection.regions.ProtectedRegion r : plugin.getRegionManager(chunk.getWorld()).getRegions().values()) {
            debug("Checking Region " + r.getId());
            if(!VillageUtils.isCoordBetweenCoords(chunk, r)) continue;
            return true;
        }
        
        return false;
    }
    
    @Override
    public void onHook() {
        if(!getConfigManager().useWorldGuard) return;
    }
    
    @Override
    public void onUnHook() {
    }
}

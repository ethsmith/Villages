package com.domsplace.Villages.Hooks;

import com.domsplace.Villages.Bases.Base;
import static com.domsplace.Villages.Bases.Base.isCoordBetweenCoords;
import com.domsplace.Villages.Bases.PluginHook;
import com.domsplace.Villages.Objects.Region;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.block.Block;

public class WorldGuardHook extends PluginHook {
    public WorldGuardHook() {
        super("WorldGuard");
    }
    
    @Override
    public void onHook() {
        super.onHook();
        Base.useWorldGuard = true;
    }
    
    @Override
    public void onUnhook() {
        super.onUnhook();
        Base.useWorldGuard = false;
    }
    
    public WorldGuardPlugin getWorldGuard() {
        try {
            return (WorldGuardPlugin) this.getHookedPlugin();
        } catch(NoClassDefFoundError e) {
            return null;
        }
    }
    
    public boolean isOverlappingRegion(Region region) {
        return getOverlappingRegion(region) != null;
    }
    
    public ProtectedRegion getOverlappingRegion(Region region) {
        for(ProtectedRegion r : getWorldGuard().getRegionManager(region.getBukkitWorld()).getRegions().values()) {
            debug("Checking Region " + r.getId());
            if(!isCoordBetweenCoords(region, r)) continue;
            return r;
        }
        return null;
    }
    
    public static boolean isCoordBetweenCoords(Region region, ProtectedRegion r) {
        Block b1 = region.getLowBlock();
        Block b2 = region.getHighBlock();
        
        boolean s1 = isCoordBetweenCoords(b1.getX(), b1.getZ(), r.getMinimumPoint(), r.getMaximumPoint());
        if(s1) return true;
        boolean s2 = isCoordBetweenCoords(b2.getX(), b2.getZ(), r.getMinimumPoint(), r.getMaximumPoint());
        if(s2) return true;
        boolean s3 = isCoordBetweenCoords(r.getMinimumPoint(), b1, b2);
        if(s3) return true;
        boolean s4 = isCoordBetweenCoords(r.getMaximumPoint(), b1, b2);
        if(s4) return true;
        
        return false;
    }
    
    public static boolean isCoordBetweenCoords(int checkX, int checkZ, BlockVector min, BlockVector max) {
        return isCoordBetweenCoords(checkX, checkZ, min.getBlockX(), min.getBlockZ(), max.getBlockX(), max.getBlockZ());
    }
    
    public static boolean isCoordBetweenCoords(BlockVector bv, int outerX, int outerZ, int maxX, int maxZ) {
        return isCoordBetweenCoords(bv.getBlockX(), bv.getBlockZ(), outerX, outerZ, maxX, maxZ);
    }
    
    public static boolean isCoordBetweenCoords(BlockVector bv, Block b1, Block b2) {
        return isCoordBetweenCoords(bv.getBlockX(), bv.getBlockZ(), b1.getX(), b1.getZ(), b2.getX(), b2.getZ());
    }
}

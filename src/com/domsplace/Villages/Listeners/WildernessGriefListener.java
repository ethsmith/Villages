package com.domsplace.Villages.Listeners;

import com.domsplace.Villages.Events.GriefEvent;
import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.VillageListener;
import com.domsplace.Villages.Enums.GriefType;
import com.domsplace.Villages.Objects.Region;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.event.EventHandler;

public class WildernessGriefListener extends VillageListener {
    @EventHandler
    public void blockWildernessInteracting(GriefEvent e) {
        if(!e.getType().equals(GriefType.INTERACT)) return;
        if(getConfig().getBoolean("protection.grief.wilderness.use", true)) return;
        Region region = Region.getRegion(e.getBlock().getLocation());
        Village village = Village.getOverlappingVillage(region);
        if(village != null) return;
        Base.sk(e.getPlayer(), "nointeract", village);
        e.setCancelled(true);
    }
    
    @EventHandler
    public void blockWildernessBreaking(GriefEvent e) {
        if(!e.getType().equals(GriefType.BREAK)) return;
        if(getConfig().getBoolean("protection.grief.wilderness.break", true)) return;
        Region region = Region.getRegion(e.getBlock().getLocation());
        Village village = Village.getOverlappingVillage(region);
        if(village != null) return;
        Base.sk(e.getPlayer(), "nointeract", village);
        e.setCancelled(true);
    }
    
    @EventHandler
    public void blockWildernessPlacing(GriefEvent e) {
        if(!e.getType().equals(GriefType.PLACE)) return;
        if(getConfig().getBoolean("protection.grief.wilderness.place", true)) return;
        Region region = Region.getRegion(e.getBlock().getLocation());
        Village village = Village.getOverlappingVillage(region);
        if(village != null) return;
        Base.sk(e.getPlayer(), "nointeract", village);
        e.setCancelled(true);
    }
    
    @EventHandler
    public void blockWildernessMining(GriefEvent e) {
        if(!e.getType().equals(GriefType.BLOCK_DAMAGE)) return;
        if(getConfig().getBoolean("protection.grief.wilderness.mine", true)) return;
        Region region = Region.getRegion(e.getBlock().getLocation());
        Village village = Village.getOverlappingVillage(region);
        if(village != null) return;
        Base.sk(e.getPlayer(), "nointeract", village);
        e.setCancelled(true);
    }
}

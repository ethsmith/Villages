package com.domsplace.Villages.Listeners;

import com.domsplace.Villages.Bases.VillageListener;
import com.domsplace.Villages.Enums.GriefType;
import com.domsplace.Villages.Events.GriefEvent;
import com.domsplace.Villages.Objects.Plot;
import com.domsplace.Villages.Objects.Region;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.event.EventHandler;

public class PlotGriefListener extends VillageListener {
    @EventHandler(ignoreCancelled=true)
    public void blockPlotBreaking(GriefEvent e) {
        if(!e.getType().equals(GriefType.BREAK)) return;
        Region r = Region.getRegion(e.getPlayer());
        Village v = Village.getOverlappingVillage(r);
        if(v == null) return;
        Resident res = Resident.getResident(e.getPlayer());
        Plot p = v.getPlot(r);
        if(p == null) return;
        if(p.canBuild(res)) return;
        sk(e.getPlayer(), "nointeract");
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled=true)
    public void blockPlotPlacing(GriefEvent e) {
        if(!e.getType().equals(GriefType.PLACE)) return;
        Region r = Region.getRegion(e.getPlayer());
        Village v = Village.getOverlappingVillage(r);
        if(v == null) return;
        Resident res = Resident.getResident(e.getPlayer());
        Plot p = v.getPlot(r);
        if(p == null) return;
        if(p.canBuild(res)) return;
        sk(e.getPlayer(), "nointeract");
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled=true)
    public void blockPlotMining(GriefEvent e) {
        if(!e.getType().equals(GriefType.BLOCK_DAMAGE)) return;
        Region r = Region.getRegion(e.getPlayer());
        Village v = Village.getOverlappingVillage(r);
        if(v == null) return;
        Resident res = Resident.getResident(e.getPlayer());
        Plot p = v.getPlot(r);
        if(p == null) return;
        if(p.canBuild(res)) return;
        sk(e.getPlayer(), "nointeract");
        e.setCancelled(true);
    }
}

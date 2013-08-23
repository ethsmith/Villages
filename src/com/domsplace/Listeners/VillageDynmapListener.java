package com.domsplace.Listeners;

import com.domsplace.Utils.VillageDynmapUtils;
import com.domsplace.Utils.VillageUtils;
import com.domsplace.VillageBase;
import com.domsplace.VillagesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

public class VillageDynmapListener extends VillageBase implements Listener {
    
    private VillagesPlugin plugin;
    
    public BukkitTask FixDynmapMap;
    
    public VillageDynmapListener(VillagesPlugin plugin) {
        this.plugin = plugin;
        FixDynmapMap = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            public void run() {
                if(!VillageUtils.useDynmap) {
                    return;
                }
                
                VillageDynmapUtils.FixDynmapRegions();
            }
        }, 60L, 60L);
    }
}

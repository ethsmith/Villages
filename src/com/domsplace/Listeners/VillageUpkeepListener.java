package com.domsplace.Listeners;

import com.domsplace.DataManagers.VillageUpkeepManager;
import com.domsplace.Objects.Village;
import com.domsplace.Utils.VillageVillagesUtils;
import com.domsplace.VillageBase;
import com.domsplace.VillagesPlugin;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

public class VillageUpkeepListener extends VillageBase implements Listener {
    private VillagesPlugin plugin;
    
    public BukkitTask AutoCheckUpkeep;
    
    public VillageUpkeepListener(VillagesPlugin plugin) {
        this.plugin = plugin;
        
        AutoCheckUpkeep = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            public void run() {
                ArrayList<Village> villages = new ArrayList<Village>(VillageVillagesUtils.Villages);
                for(Village village : villages) {
                    VillageUpkeepManager.checkVillage(village);
                }
            }
        }, 60L, 600L);
    }
}

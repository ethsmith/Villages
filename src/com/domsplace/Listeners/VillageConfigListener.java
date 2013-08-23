package com.domsplace.Listeners;

import com.domsplace.DataManagers.VillageConfigManager;
import com.domsplace.Utils.VillageVillagesUtils;
import com.domsplace.VillageBase;
import com.domsplace.VillagesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

public class VillageConfigListener extends VillageBase implements Listener {
    
    private VillagesPlugin plugin;
    
    public BukkitTask AutoSaveConfig;
    
    public VillageConfigListener(VillagesPlugin plugin) {
        this.plugin = plugin;
        
        AutoSaveConfig = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            public void run() {
                VillageConfigManager.saveConfig();
                VillageVillagesUtils.SaveAllVillages();
            }
        }, 60L, 30000L);
    }
}

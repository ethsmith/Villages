package com.domsplace.Villages.Listeners;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.PluginHook;
import com.domsplace.Villages.Bases.VillageListener;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class ServerUnloadListener extends VillageListener {
    @EventHandler(ignoreCancelled=true)
    public void handleVillageWorldUnload(WorldUnloadEvent e) {
        if(!isVillageWorld(e.getWorld())) return;
        log("Village World \"" + e.getWorld() + "\" was unloaded! Village features from this world will be disabled.");
        Base.VillageWorlds.remove(e.getWorld());
    }
    
    @EventHandler(ignoreCancelled=true)
    public void handleVillageWorldLoad(WorldLoadEvent e) {
        List<String> worlds = Base.TryWorlds;
        boolean c = false;
        for(String s : worlds) {
            if(s.equalsIgnoreCase(e.getWorld().getName())) c = true;
        }
        
        if(!c) return;
        log("World \"" + e.getWorld().getName() + "\" was loaded and Villages will use this world now.");
        Base.VillageWorlds.add(e.getWorld());
    }
    
    @EventHandler(ignoreCancelled=true)
    public void handlePluginUnloaded(PluginDisableEvent e) {
        if(e.getPlugin() == null) return;
        PluginHook hook = PluginHook.getHookFromPlugin(e.getPlugin());
        if(hook == null) return;
        hook.unHook();
    }
    
    @EventHandler(ignoreCancelled=true)
    public void handlePluginLoaded(PluginEnableEvent e) {
        if(e.getPlugin() == null) return;
        PluginHook hook = PluginHook.getHookFromPlugin(e.getPlugin());
        if(hook == null) return;
        hook.hook();
    }
}

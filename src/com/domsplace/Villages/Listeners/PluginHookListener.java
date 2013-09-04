package com.domsplace.Villages.Listeners;

import com.domsplace.Villages.Bases.ListenerBase;
import com.domsplace.Villages.Bases.PluginHookBase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class PluginHookListener extends ListenerBase {
    @EventHandler(ignoreCancelled=true)
    public void onPluginDisable(PluginDisableEvent e) {
        if(e.getPlugin() == null) return;
        PluginHookBase plugin = PluginHookBase.unhook(e.getPlugin());
        if(plugin == null) return;
        log(plugin.getPluginName() + " was disabled! Disabling hooked features.");
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onPluginDisable(PluginEnableEvent e) {
        if(e.getPlugin() == null) return;
        PluginHookBase plugin = PluginHookBase.hook(e.getPlugin());
        if(plugin == null) return;
        log(plugin.getPluginName() + " was enabled and hooked!");
    }
}

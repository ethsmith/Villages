package com.domsplace.Villages.Bases;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;

public class ListenerBase extends BasesBase implements Listener {
    private static final List<ListenerBase> LISTENERS = new ArrayList<ListenerBase>();
    
    protected static void regsiterListener(ListenerBase listener) {
        Bukkit.getPluginManager().registerEvents(listener, getPlugin());
        ListenerBase.getListeners().add(listener);
        debug("Registered Listener: " + listener.getClass().getSimpleName());
    }
    
    public static List<ListenerBase> getListeners() {
        return ListenerBase.LISTENERS;
    }
    
    //Instance
    public ListenerBase() {
        ListenerBase.regsiterListener(this);
    }
}

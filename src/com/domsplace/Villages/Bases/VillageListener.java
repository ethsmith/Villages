package com.domsplace.Villages.Bases;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VillageListener extends Base implements Listener {
    private static final List<VillageListener> LISTENERS = new ArrayList<VillageListener>();
    
    protected static void regsiterListener(VillageListener listener) {
        Bukkit.getPluginManager().registerEvents(listener, getPlugin());
        VillageListener.getListeners().add(listener);
        debug("Registered Listener: " + listener.getClass().getSimpleName());
    }
    
    protected static void deRegsiterListener(VillageListener listener) {
        Method[] methods = listener.getClass().getMethods();
        for(Method m : methods) {
            EventHandler ev = m.getAnnotation(EventHandler.class);
            if(ev == null) continue;
            Class<?>[] params = m.getParameterTypes();
            for(Class<?> param : params) {
                if(param == null) continue;
                Class<? extends Event> e = param.asSubclass(Event.class);
                if(e == null) continue;
                try {
                    Method h = e.getMethod("getHandlerList");
                    HandlerList r = (HandlerList) h.invoke(null);
                    r.unregister(listener);
                } catch(Exception ex) {
                    continue;
                }
            }
        }
        VillageListener.getListeners().remove(listener);
        debug("De-Registered Listener: " + listener.getClass().getSimpleName());
    }
    
    public static List<VillageListener> getListeners() {
        return VillageListener.LISTENERS;
    }
    
    //Instance
    public VillageListener() {
        VillageListener.regsiterListener(this);
    }
    
    public void deRegisterListener() {
        VillageListener.deRegsiterListener(this);
    }
}

package com.domsplace.Villages.Bases;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ListenerBase extends BaseBase implements Listener {
    private static final List<ListenerBase> LISTENERS = new ArrayList<ListenerBase>();
    
    protected static void regsiterListener(ListenerBase listener) {
        Bukkit.getPluginManager().registerEvents(listener, getPlugin());
        ListenerBase.getListeners().add(listener);
        debug("Registered Listener: " + listener.getClass().getSimpleName());
    }
    
    protected static void deRegsiterListener(ListenerBase listener) {
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
        ListenerBase.getListeners().remove(listener);
        debug("De-Registered Listener: " + listener.getClass().getSimpleName());
    }
    
    public static List<ListenerBase> getListeners() {
        return ListenerBase.LISTENERS;
    }
    
    //Instance
    public ListenerBase() {
        ListenerBase.regsiterListener(this);
    }
    
    public void deRegisterListener() {
        ListenerBase.deRegsiterListener(this);
    }
}

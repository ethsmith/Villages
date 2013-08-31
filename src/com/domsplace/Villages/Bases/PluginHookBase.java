package com.domsplace.Villages.Bases;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PluginHookBase {
    //Constants
    private static final List<PluginHookBase> HOOKS = new ArrayList<PluginHookBase>();
    
    protected static void registerHook(PluginHookBase pluginHook) {
        PluginHookBase.HOOKS.add(pluginHook);
    }
    
    //Instance
    private String plugin;
    private Plugin hooked;
    
    public PluginHookBase(String plugin) {
        this.plugin = plugin;
        registerHook(this);
    }
    
    public boolean hook() {
        try {
            Plugin p  = Bukkit.getPluginManager().getPlugin(this.getPluginName());
            if(p == null) {
                throw(new Exception("Failed To Hook"));
            }
            
            this.hooked = p;
            this.onHook();
        } catch(Exception e) {
            this.hooked = null;
            this.onHookFail();
        }
        
        return this.hooked != null;
    }
    
    public Plugin getPlugin() {
        return this.hooked;
    }
    
    public Class<? extends Plugin> getHookedClass() {
        return this.getPlugin().getClass();
    }
    
    public Type getType() {
        return this.getHookedClass().getGenericSuperclass();
    }
    
    public String getPluginName() {
        return this.plugin;
    }
    
    public boolean isHooked() {
        return hooked != null;
    }
    
    public void onHook() {}
    
    public void onUnHook() {}
    
    public void onHookFail() {}
}
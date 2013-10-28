package com.domsplace.Villages.Hooks;

import com.domsplace.Villages.Bases.PluginHook;
import com.domsplace.Villages.Listeners.HeroChatListener;

public class HeroChatHook extends PluginHook {
    HeroChatListener listener;
    
    public HeroChatHook() {
        super("Herochat");
    }
    
    @Override
    public void onHook() {
        super.onHook();
        this.listener = new HeroChatListener();
    }
    
    @Override
    public void onUnhook() {
        super.onUnhook();
        
        if(this.listener == null) return;
        this.listener.deRegisterListener();
        this.listener = null;
    }
}

package com.domsplace.Villages.Hooks;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.PluginHook;
import com.domsplace.Villages.Listeners.HeroChatListener;

public class HeroChatHook extends PluginHook {
    HeroChatListener listener;
    
    public HeroChatHook() {
        super("HeroChat");
    }
    
    @Override
    public void onHook() {
        super.onHook();
        Base.useHeroChat = true;
        this.listener = new HeroChatListener();
    }
    
    @Override
    public void onUnhook() {
        super.onUnhook();
        Base.useHeroChat = false;
        
        if(this.listener == null) return;
        this.listener.deRegisterListener();
        this.listener = null;
    }
}

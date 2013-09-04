package com.domsplace.Villages.Hooks;

import com.domsplace.Villages.Bases.PluginHookBase;
import com.domsplace.Villages.Listeners.HeroChatListener;
import com.domsplace.Villages.Utils.Utils;
import com.dthielke.herochat.Herochat;

public class HeroChatHook extends PluginHookBase{
    private HeroChatListener heroChatListener;
    
    public HeroChatHook() {
        super("Herochat");
    }
    
    public Herochat getHerochat() {
        return (Herochat) this.getHookedPlugin();
    }
    
    @Override
    public void onHook() {
        if(!Utils.useHerochat) return;
        this.heroChatListener = new HeroChatListener();
    }
    
    @Override
    public void onUnHook() {
        if(this.heroChatListener != null) this.heroChatListener.deRegisterListener();
    }
}

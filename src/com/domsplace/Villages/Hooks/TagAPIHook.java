package com.domsplace.Villages.Hooks;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.PluginHook;
import com.domsplace.Villages.Listeners.TagAPIListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.kitteh.tag.TagAPI;

public class TagAPIHook extends PluginHook {
    private TagAPIListener listener;
    
    public TagAPIHook() {
        super("TagAPI");
    }
    
    public TagAPI getTagAPI() {
        return (TagAPI) this.getHookedPlugin();
    }
    
    public void refreshTags(Player p) {
        if(!this.isHooked()) return;
        try {
            TagAPI.refreshPlayer(p);
        } catch(NoClassDefFoundError e) {
        }
    }

    public void refreshTags() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            refreshTags(p);
        }
    }
    
    @Override
    public void onHook() {
        super.onHook();
        Base.useTagAPI = true;
        this.listener = new TagAPIListener();
    }
    
    @Override
    public void onUnhook() {
        super.onUnhook();
        Base.useTagAPI = false;
        
        if(this.listener == null) return;
        this.listener.deRegisterListener();
        this.listener = null;
    }
}

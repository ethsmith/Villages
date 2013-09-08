package com.domsplace.Villages.Hooks;

import com.domsplace.Villages.Bases.PluginHookBase;
import com.domsplace.Villages.Listeners.TagAPIListener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.kitteh.tag.TagAPI;

public class TagAPIHook extends PluginHookBase {
    public static TagAPIHook instance;
    private TagAPIListener tagAPIListener;
    
    public TagAPIHook() {
        super("TagAPI");
        TagAPIHook.instance = this;
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
        if(!getConfigManager().useTagAPI) return;
        this.tagAPIListener = new TagAPIListener();
    }
    
    @Override
    public void onUnHook() {
        if(this.tagAPIListener != null) this.tagAPIListener.deRegisterListener();
    }
}

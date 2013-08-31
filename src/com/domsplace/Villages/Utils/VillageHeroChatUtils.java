package com.domsplace.Villages.Utils;

import com.domsplace.Villages.Bases.UtilsBase;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class VillageHeroChatUtils extends UtilsBase {
    public static boolean isHeroChatLoaded() {
        try {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Herochat");

            if (plugin == null || !(plugin instanceof com.dthielke.herochat.Herochat)) {
                return false;
            }
            
            if(!plugin.isEnabled()) {
                return false;
            }

            return true;
        } catch(NoClassDefFoundError e) {
            return false;
        }
    }
    
    public static com.dthielke.herochat.Herochat getHeroChat() {
        try {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Herochat");

            if (plugin == null || !(plugin instanceof com.dthielke.herochat.Herochat)) {
                return null;
            }
            
            if(!plugin.isEnabled()) {
                return null;
            }

            return (com.dthielke.herochat.Herochat) plugin;
        } catch(NoClassDefFoundError e) {
            return null;
        }
    }
}

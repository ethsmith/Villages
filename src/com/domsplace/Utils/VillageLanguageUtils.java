package com.domsplace.Utils;

import com.domsplace.DataManagers.VillageLanguageManager;
import com.domsplace.Objects.Village;
import com.domsplace.VillageBase;
import org.bukkit.OfflinePlayer;

public class VillageLanguageUtils extends VillageBase {
    
    public static String getKey(String key) {
        String l = VillageLanguageManager.defaultLanguage.getString(key);
        if(VillageLanguageManager.language.contains(key)) {
            l = VillageLanguageManager.language.getString(key);
        }
        
        if(l == null) {
            l = "§cVillages message not set! Contact Server administrator! MISKEY: " + key;
            return l;
        }
        
        l = l.replaceAll("%e%", ChatError).replaceAll("%d%", ChatDefault).replaceAll("%i%", ChatImportant);
        l = VillageUtils.ColorString(l);
        
        return l;
    }
    
    public static String getKey(String key, Village village) {
        String l = getKey(key);
        
        l = l.replaceAll("%v%", village.getName());
        
        return l;
    }
    public static String getKey(String key, double money) {
        String l = getKey(key);
        
        l = l.replaceAll("%n%", VillageEconomyUtils.economy.format(money));
        
        return l;
    }
    
    public static String getKey(String key, OfflinePlayer player) {
        String l = getKey(key);
        
        l = l.replaceAll("%p%", player.getName());
        
        return l;
    }
    
    public static String getKey(String key, OfflinePlayer player, double amount) {
        String l = getKey(key, amount);
        l = l.replaceAll("%p%", player.getName());
        
        return l;
    }
}

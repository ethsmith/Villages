package com.domsplace.Villages.Utils;

import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Bases.DataManagerBase;
import com.domsplace.Villages.Bases.UtilsBase;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;

public class VillageLanguageUtils extends UtilsBase {
    
    public static String getKey(String key) {
        String l = DataManagerBase.LANGUAGE_MANAGER.defaultLanguage.getString(key);
        if(DataManagerBase.LANGUAGE_MANAGER.language.contains(key)) {
            l = DataManagerBase.LANGUAGE_MANAGER.language.getString(key);
        }
        
        if(l == null) {
            l = "§cVillages message not set! Contact Server administrator! MISKEY: " + key;
            return l;
        }
        
        l = l.replaceAll("%e%", ChatError).replaceAll("%d%", ChatDefault).replaceAll("%i%", ChatImportant);
        l = ColorString(l);
        
        return l;
    }
    
    public static String getKey(String key, Village village) {
        String l = getKey(key);
        
        l = l.replaceAll("\\$", "\\\\\\$");
        l = l.replaceAll("%v%", village.getName());
        
        return l;
    }
    public static String getKey(String key, double money) {
        String l = getKey(key);
        
        String literal = VillageEconomyUtils.economy.format(money);
        literal = literal.replaceAll("\\$", "\\\\\\$");
        
        l = l.replaceAll("%n%", literal);
        
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
    
    public static String getKey(String key, Chunk chunk) {
        String l = getKey(key);
        
        l = l.replaceAll("%x%", "" + chunk.getX());
        l = l.replaceAll("%z%", "" + chunk.getZ());
        
        return l;
    }
}

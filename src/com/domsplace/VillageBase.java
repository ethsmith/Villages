package com.domsplace;

import com.domsplace.Objects.Village;
import com.domsplace.Utils.VillageLanguageUtils;
import com.domsplace.Utils.VillageUtils;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;

public class VillageBase {
    public static String ChatError = ChatColor.RED + "";
    public static String ChatDefault = ChatColor.GRAY + "";
    public static String ChatImportant = ChatColor.BLUE + "";
    public static String ChatPrefix = "§7[§9Villages§7] ";
    public static String VillageColor = ChatColor.GREEN + "";
    public static String EnemyColor = ChatColor.DARK_RED + "";
    public static String PlayerChatPrefix = "§6[§9%v%§6] ";
    public static boolean UsePlots = true;
    
    
    public static String gK(String key) {
        return VillageLanguageUtils.getKey(key);
    }
    
    public static String gK(String key, Village village) {
        return VillageLanguageUtils.getKey(key, village);
    }
    
    public static String gK(String key, double money) {
        return VillageLanguageUtils.getKey(key, money);
    }
    
    public static String gK(String key, OfflinePlayer player) {
        return VillageLanguageUtils.getKey(key, player);
    }
    
    public static String gK(String key, OfflinePlayer player, double amount) {
        return VillageLanguageUtils.getKey(key, player, amount);
    }
    
    public static String gK(String key, Chunk c) {
        return VillageLanguageUtils.getKey(key, c);
    }
    
    public static void debug(String message) {
        VillageUtils.broadcast("§a[§bDEBUG§a] §d" + message);
    }
}

package com.domsplace.Villages.Bases;

import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Utils.VillageLanguageUtils;
import com.domsplace.Villages.Utils.Utils;
import com.domsplace.Villages.VillagesPlugin;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Base {
    public static boolean DEBUG_MODE = true;
    private static VillagesPlugin plugin;
    
    public static String ChatError = ChatColor.RED + "";
    public static String ChatDefault = ChatColor.GRAY + "";
    public static String ChatImportant = ChatColor.BLUE + "";
    public static String ChatPrefix = "§7[§9Villages§7] ";
    
    public static String VillageColor = ChatColor.GREEN + "";
    public static String EnemyColor = ChatColor.DARK_RED + "";
    
    public static String PlayerChatPrefix = "§6[§9%v%§6] ";
    
    public static boolean UsePlots = true;
    
    public static List<String> villageCreatedCommands;
    public static List<String> villageDeletedCommands;
    public static List<String> villagePlayerAddedCommands;
    public static List<String> villagePlayerRemovedCommands;
    public static List<String> villageMayorDeathCommands;
    
    public static String WildernessPrefix = ChatColor.BLUE + "Wilderness";
    
    public static String CheckUpdateURL = "http://domsplace.com/ajax/getProjectVersion.php?name=Villages";
    public static String LatestVersionURL = "http://dev.bukkit.org/bukkit-plugins/villages/";
    
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
    
    public static void debug(Object message) {
        if(!DEBUG_MODE) return;
        Utils.broadcast("§a[§bDEBUG§a] §d" + message.toString());
    }
    
    public static boolean isPlayer(Object o) {
        return (o instanceof Player) && (o != null);
    }
    
    public static void setPlugin(VillagesPlugin plugin) {
        Base.plugin = plugin;
    }
    
    public static VillagesPlugin getPlugin() {
        return Base.plugin;
    }
    
    public static File getDataFolder() {
        return getPlugin().getDataFolder();
    }
    
    public static void log(Object o) {
        getPlugin().getLogger().log(Level.INFO, o.toString());
    }
    
    public static YamlConfiguration getConfig() {
        return DataManagerBase.CONFIG_MANAGER.config;
    }
    
    public static String getStringFromURL(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                    connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        return response.toString();
    }
}

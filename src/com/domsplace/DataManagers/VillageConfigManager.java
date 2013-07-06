package com.domsplace.DataManagers;

import com.domsplace.Listeners.VillageVillagesListener;
import com.domsplace.Utils.VillageDynmapUtils;
import com.domsplace.Utils.VillageEconomyUtils;
import com.domsplace.Utils.VillageSQLUtils;
import com.domsplace.Utils.VillageUtils;
import com.domsplace.Utils.VillageVillagesUtils;
import com.domsplace.VillageBase;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class VillageConfigManager {
    public static YamlConfiguration config;
    public static File configFile;
    
    public static boolean LoadConfig() {
        try {
            if(!VillageUtils.plugin.getDataFolder().exists()){
                VillageUtils.plugin.getDataFolder().mkdir();
            }
            
            configFile = new File(VillageUtils.plugin.getDataFolder() + "/config.yml");

            if(!configFile.exists()) {
                configFile.createNewFile();
            }
            
            boolean oldSQL = VillageUtils.useSQL;
            
            config = YamlConfiguration.loadConfiguration(configFile);
            
            //Add Worlds
            if(!config.contains("Worlds")) {
                List<String> worlds = new ArrayList<String>();
                for(World w : Bukkit.getWorlds()) {
                    worlds.add(w.getName());
                }
                config.set("Worlds", worlds);
            }
            
            if(!config.contains("sql")) {
                if(!config.contains("sql.use")) {
                    config.set("sql.use", true);
                }
                
                config.set("sql.username", "root");
                config.set("sql.password", "password");
                config.set("sql.host", "localhost");
                config.set("sql.database", "minecraft");
                config.set("sql.port", "3306");
            }
            
            if(!config.contains("townborder")) {
                config.set("townborder", 3);
            }
            
            if(!config.contains("use.worldguard")) {
                config.set("use.worldguard", true);
            }
            if(!config.contains("use.dynmap")) {
                config.set("use.dynmap", true);
            }
            if(!config.contains("use.economy")) {
                if(!config.contains("economy")) {
                    config.set("use.economy", true);
                } else {
                    config.set("use.economy", config.getBoolean("use.economy"));
                }
            }
            
            if(!config.contains("colors")) {
                if(!config.contains("colors.prefix")) {
                    config.set("colors.prefix", "&7[&9Villages&7]");
                }
                if(!config.contains("colors.error")) {
                    config.set("colors.error", "&c");
                }
                if(!config.contains("colors.default")) {
                    config.set("colors.default", "&7");
                }
                if(!config.contains("colors.important")) {
                    config.set("colors.important", "&9");
                }
            }
            if(!config.contains("colors.samevillage")) {
                config.set("colors.samevillage", "&a");
            }
            if(!config.contains("colors.enemy")) {
                config.set("colors.enemy", "&4");
            }
            if(!config.contains("colors.chatprefix")) {
                config.set("colors.chatprefix", "&6[&9%v%&6] ");
            }
            
            String p = "protection.";
            if(!config.contains(p + "griefwild")) {
                config.set(p + "griefwild", true);
            }
            if(!config.contains(p + "griefvillage")) {
                config.set(p + "griefvillage", false);
            }
            if(!config.contains(p + "pvpinwilderness")) {
                config.set(p + "pvpinwilderness", true);
            }
            if(!config.contains(p + "pvpsamevillage")) {
                config.set(p + "pvpsamevillage", false);
            }
            if(!config.contains(p + "pvpdifferentvillage")) {
                config.set(p + "pvpdifferentvillage", true);
            }
            
            p = "cost.";
            if(!config.contains(p + "createvillage")) {
                config.set(p + "createvillage", 1000.00);
            }
            if(!config.contains(p + "expand")) {
                config.set(p + "expand", 2000.00);
            }
            
            p = "messages.";
            if(!config.contains(p + "entervillage")) {
                config.set(p + "entervillage", true);
            }
            if(!config.contains(p + "leavevillage")) {
                config.set(p + "leavevillage", true);
            }
            
            if(!config.contains("colors.colornames")) {
                config.set("colors.colornames", true);
            }
            if(!config.contains("colors.ingamelist")) {
                config.set("colors.ingamelist", true);
            }
            
            if(!config.contains("largebanks")) {
                config.set("largebanks", false);
            }
            
            if(!config.contains("defaultsize")) {
                config.set("defaultsize", 1);
            }
            
            if(!config.contains("use.villageplots")) {
                config.set("use.villageplots", true);
            }
            
            //Load Values
            VillageSQLUtils.sqlHost = config.getString("sql.host");
            VillageSQLUtils.sqlDB = config.getString("sql.database");
            VillageSQLUtils.sqlUser = config.getString("sql.username");
            VillageSQLUtils.sqlPass = config.getString("sql.password");
            VillageSQLUtils.sqlPort = config.getString("sql.port");
            
            VillageVillagesUtils.borderRadius = config.getInt("townborder");
            
            VillageBase.ChatError = VillageUtils.ColorString(config.getString("colors.error"));
            VillageBase.ChatPrefix = VillageUtils.ColorString(config.getString("colors.prefix")) + " ";
            VillageBase.ChatDefault = VillageUtils.ColorString(config.getString("colors.default"));
            VillageBase.ChatImportant = VillageUtils.ColorString(config.getString("colors.important"));
            VillageBase.VillageColor = VillageUtils.ColorString(config.getString("colors.samevillage"));
            VillageBase.EnemyColor = VillageUtils.ColorString(config.getString("colors.enemy"));
            VillageBase.PlayerChatPrefix = VillageUtils.ColorString(config.getString("colors.chatprefix")) + ChatColor.RESET;
            
            VillageBase.UsePlots = config.getBoolean("use.villageplots");
            
            VillageUtils.useSQL = config.getBoolean("sql.use");
            VillageUtils.useTagAPI = config.getBoolean("colors.colornames");
            VillageUtils.useWorldGuard = config.getBoolean("use.worldguard");
            VillageUtils.useEconomy = config.getBoolean("use.economy");
            VillageUtils.useDynmap = config.getBoolean("use.dynmap");
            
            VillageVillagesListener.PVPWilderness = config.getBoolean("protection.pvpinwilderness");
            VillageVillagesListener.PVPEnemyVillage = config.getBoolean("protection.pvpdifferentvillage");
            VillageVillagesListener.PVPSameVillage = config.getBoolean("protection.pvpsamevillage");
                    
            //Load add-ins
            
            /*** Try to use Economy ***/
            if(config.getBoolean("use.economy")) {
                if(!VillageEconomyUtils.setupEconomy()) {
                    VillageUtils.Error("Failed to load Vault", null);
                    VillageUtils.useEconomy = false;
                } else {
                    VillageUtils.msgConsole("Hooked into Economy.");
                }
            }
            
            /*** Try to use SQL ***/
            if(config.getBoolean("sql.use")) {
                if(!VillageSQLUtils.sqlConnect()) {
                    VillageUtils.Error("Failed to connect to SQL", null);
                    VillageUtils.useSQL = false;
                } else {
                    VillageUtils.msgConsole("Connected to SQL successfully.");
                    if(!VillageSQLManager.SetupDatabase()) {
                        VillageUtils.Error("Failed to setup SQL Database", null);
                        VillageUtils.useSQL = false;
                    }
                }
            } else if(oldSQL) {
                VillageUtils.useSQL = false;
                VillageSQLUtils.sqlClose();
            }
            
            if(config.getBoolean("sql.use") != true) {
                File dataFolder = new File(VillageUtils.plugin.getDataFolder() + "/data/");
                if(!dataFolder.exists()){
                    dataFolder.mkdir();
                }
                dataFolder = new File(VillageUtils.plugin.getDataFolder() + "/data/villages/");
                if(!dataFolder.exists()){
                    dataFolder.mkdir();
                }
            }
            
            /*** Try to use TagAPI ***/
            if(VillageUtils.useTagAPI && !VillageUtils.getTagAPI()) {
                VillageUtils.useTagAPI = false;
                VillageUtils.Error("Failed to hook into TagAPI", null);
            } else if(VillageUtils.useTagAPI) {
                VillageUtils.msgConsole("Hooked into TagAPI.");
            }
            
            /*** Try to use WorldGuard ***/
            if(VillageUtils.useWorldGuard) {
                if(VillageUtils.getWorldGuard() == null) {
                    VillageUtils.useWorldGuard = false;
                    VillageUtils.Error("Failed to hook into WorldGuard", null);
                } else {
                    VillageUtils.msgConsole("Hooked into WorldGuard.");
                }
            }
            
            /*** Try to use Dynamic Map ***/
            try {
                if(VillageDynmapUtils.markers != null) {
                    VillageDynmapUtils.UnloadDynmapRegions();
                }
            } catch(NoClassDefFoundError e) {
                
            }
            
            if(VillageUtils.useDynmap) {
                try {
                    if(!VillageDynmapUtils.canGetDynmapPlugin() || !VillageDynmapUtils.setupDynmap()) {
                        VillageUtils.useDynmap = false;
                        VillageUtils.Error("Failed to hook into Dynmap", null);
                    } else {
                        VillageUtils.msgConsole("Hooked into Dynmap.");
                    }
                } catch(NoClassDefFoundError e) {
                    VillageUtils.useDynmap = false;
                    VillageUtils.Error("Failed to hook into Dynmap", null);
                }
            }
            
            saveConfig();
            
            //Load Language files//
            VillageLanguageManager.LoadLanguage();
            
            //Refresh Colors
            VillageUtils.refreshTags();
            
            //
        } catch (Exception ex) {
            VillageUtils.Error("Failed to load config.", ex);
            return false;
        }
        return true;
    }
    
    public static void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException ex) {
            VillageUtils.Error("Failed to save config.", ex);
        }
    }
}

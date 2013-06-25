package com.domsplace.DataManagers;

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
            
            if(!config.contains("economy")) {
                config.set("economy", true);
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
            
            String p = "protection.";
            if(!config.contains(p + "griefwild")) {
                config.set(p + "griefwild", true);
            }
            if(!config.contains(p + "griefvillage")) {
                config.set(p + "griefvillage", false);
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
            VillageUtils.useEconomy = config.getBoolean("economy");
            
            //Load add-ins
            if(config.getBoolean("economy")) {
                if(!VillageEconomyUtils.setupEconomy()) {
                    VillageUtils.Error("Failed to load Vault", "Couldn't find plugin.");
                    config.set("economy", false);
                } else {
                    VillageUtils.msgConsole("Hooked into Economy.");
                }
            }
            
            if(config.getBoolean("sql.use")) {
                if(!VillageSQLUtils.sqlConnect()) {
                    VillageUtils.Error("Failed to connect to SQL", "See console for error.");
                    config.set("sql.use", false);
                } else {
                    VillageUtils.msgConsole("Connected to SQL successfully.");
                    if(!VillageSQLManager.SetupDatabase()) {
                        VillageUtils.Error("Failed to setup SQL Database", "See console for error.");
                        config.set("sql.use", false);
                    }
                }
            } else if(oldSQL) {
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
            
            saveConfig();
            
            //Load Language files//
            VillageLanguageManager.LoadLanguage();
            
        } catch (Exception ex) {
            VillageUtils.Error("Failed to load config.", ex.getLocalizedMessage());
            return false;
        }
        return true;
    }
    
    public static void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException ex) {
            VillageUtils.Error("Failed to save config.", ex.getLocalizedMessage());
        }
    }
}

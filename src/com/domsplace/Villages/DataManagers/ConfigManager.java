package com.domsplace.Villages.DataManagers;

import com.domsplace.Villages.Listeners.VillagesListener;
import com.domsplace.Villages.Utils.VillageEconomyUtils;
import com.domsplace.Villages.Utils.VillageSQLUtils;

import com.domsplace.Villages.Utils.VillageUtils;
import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.CommandBase;
import com.domsplace.Villages.Bases.DataManagerBase;
import com.domsplace.Villages.Bases.PluginHookBase;
import com.domsplace.Villages.Enums.ManagerType;
import com.domsplace.Villages.Threads.UpdateThread;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager extends DataManagerBase {
    public YamlConfiguration config;
    public File configFile;
    
    public boolean useSQL = false;
    public boolean useEconomy = false;
    public boolean useTagAPI = false;
    public boolean useWorldGuard = false;
    public boolean useHerochat = false;
    
    public ConfigManager() {
        super(ManagerType.CONFIG);
    }
    
    @Override
    public void tryLoad() throws IOException {
        if(!getDataFolder().exists()){
            getDataFolder().mkdir();
        }

        configFile = new File(getDataFolder(), "/config.yml");

        if(!configFile.exists()) {
            configFile.createNewFile();
        }

        boolean oldSQL = useSQL;

        config = YamlConfiguration.loadConfiguration(configFile);
        
        PluginHookBase.unhookAll();
        
        if(!config.contains("debug")) {
            config.set("debug", false);
        }
        Base.DEBUG_MODE = config.getBoolean("debug");
        
        debug("Debug Mode Is Enabled!");
        
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
        if(!config.contains("use.economy")) {
            if(!config.contains("economy")) {
                config.set("use.economy", true);
            } else {
                config.set("use.economy", config.getBoolean("use.economy"));
            }
        }
        if(!config.contains("use.villageplots")) {
            config.set("use.villageplots", true);
        }
        if(!config.contains("use.herochat")) {
            config.set("use.herochat", true);
        }
        if(!config.contains("use.vanish")) {
            config.set("use.vanish", false);
        }
        if(!config.contains("use.updates")) {
            config.set("use.updates", true);
        }

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
        if(!config.contains("colors.samevillage")) {
            config.set("colors.samevillage", "&a");
        }
        if(!config.contains("colors.enemy")) {
            config.set("colors.enemy", "&4");
        }
        if(!config.contains("colors.chatprefix")) {
            config.set("colors.chatprefix", "&6[&9%v%&6] ");
        }
        if(!config.contains("colors.wilderness")) {
            config.set("colors.wilderness", "&9Wilderness");
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
        
        p = "disable.";
        if(!config.contains(p + "mobspawning.village")) {
            config.set(p + "mobspawning.village", true);
        }
        if(!config.contains(p + "mobspawning.wilderness")) {
            config.set(p + "mobspawning.wilderness", false);
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

        if(!config.contains("commands.VillageCreated")) {
            List<String> createdCommands = new ArrayList<String>();
            createdCommands.add("villageadmin save");
            config.set("commands.VillageCreated", createdCommands);
        }
        if(!config.contains("commands.PlayerAdded")) {
            List<String> createdCommands = new ArrayList<String>();
            createdCommands.add("villageadmin save");
            config.set("commands.PlayerAdded", createdCommands);
        }
        if(!config.contains("commands.PlayerRemoved")) {
            List<String> createdCommands = new ArrayList<String>();
            createdCommands.add("villageadmin save");
            config.set("commands.PlayerRemoved", createdCommands);
        }
        if(!config.contains("commands.VillageDeleted")) {
            List<String> createdCommands = new ArrayList<String>();
            createdCommands.add("villageadmin save");
            config.set("commands.VillageDeleted", createdCommands);
        }
        if(!config.contains("commands.MayorKilled")) {
            List<String> createdCommands = new ArrayList<String>();
            createdCommands.add("villagebroadcast %v% Mayor %p% has died!");
            config.set("commands.MayorKilled", createdCommands);
        }

        //Load Values
        VillageSQLUtils.sqlHost = config.getString("sql.host");
        VillageSQLUtils.sqlDB = config.getString("sql.database");
        VillageSQLUtils.sqlUser = config.getString("sql.username");
        VillageSQLUtils.sqlPass = config.getString("sql.password");
        VillageSQLUtils.sqlPort = config.getString("sql.port");

        VillageUtils.borderRadius = config.getInt("townborder");

        Base.ChatError = ColorString(config.getString("colors.error"));

        if(!config.getString("colors.prefix").equalsIgnoreCase("")) {
            Base.ChatPrefix = ColorString(config.getString("colors.prefix")) + " ";
        } else {
            Base.ChatPrefix = "";
        }

        Base.ChatDefault = ColorString(config.getString("colors.default"));
        Base.ChatImportant = ColorString(config.getString("colors.important"));
        Base.VillageColor = ColorString(config.getString("colors.samevillage"));
        Base.EnemyColor = ColorString(config.getString("colors.enemy"));
        Base.PlayerChatPrefix = ColorString(config.getString("colors.chatprefix")) + ChatColor.RESET;
        Base.WildernessPrefix = ColorString(config.getString("colors.wilderness"));

        Base.UsePlots = config.getBoolean("use.villageplots");
        
        if(config.getBoolean("use.updates")) {
            UpdateThread updateThread = new UpdateThread();
        }

        useSQL = config.getBoolean("sql.use");
        useTagAPI = config.getBoolean("colors.colornames");
        useWorldGuard = config.getBoolean("use.worldguard");
        useEconomy = config.getBoolean("use.economy");
        useHerochat = config.getBoolean("use.herochat");

        VillagesListener.PVPWilderness = config.getBoolean("protection.pvpinwilderness");
        VillagesListener.PVPEnemyVillage = config.getBoolean("protection.pvpdifferentvillage");
        VillagesListener.PVPSameVillage = config.getBoolean("protection.pvpsamevillage");

        Base.villageCreatedCommands = config.getStringList("commands.VillageCreated");
        Base.villagePlayerAddedCommands = config.getStringList("commands.PlayerAdded");
        Base.villagePlayerRemovedCommands = config.getStringList("commands.PlayerRemoved");
        Base.villageDeletedCommands = config.getStringList("commands.VillageDeleted");
        Base.villageMayorDeathCommands = config.getStringList("commands.MayorKilled");

        //Load add-ins

        /*** Try to use Economy ***/
        if(config.getBoolean("use.economy")) {
            if(!VillageEconomyUtils.setupEconomy()) {
                Error("Failed to load Vault", null);
                useEconomy = false;
            } else {
                msgConsole("Hooked into Economy.");
            }
        }

        /*** Try to use SQL ***/
        if(config.getBoolean("sql.use")) {
            if(!VillageSQLUtils.sqlConnect()) {
                useSQL = false;
            } else {
                msgConsole("Connected to SQL successfully.");
                SQLManager sqlManager = new SQLManager();
                if(!sqlManager.load()) {
                    Error("Failed to setup SQL Database", null);
                    useSQL = false;
                }
            }
        } else if(oldSQL) {
            useSQL = false;
            VillageSQLUtils.sqlClose();
        }

        if(config.getBoolean("sql.use") != true) {
            File dataFolder = new File(getDataFolder(), "data");
            if(!dataFolder.exists()){
                dataFolder.mkdir();
            }
            dataFolder = new File(dataFolder, "villages");
            if(!dataFolder.exists()){
                dataFolder.mkdir();
            }
        }

        this.save();
        PluginHookBase.hookAll();
        
        //Update CommandPermission messages
        CommandBase.updateAllPermissionMessages();
    }
    
    @Override
    public void trySave() throws IOException {
        config.save(configFile);
    }
}

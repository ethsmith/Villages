    package com.domsplace.Villages.DataManagers;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.PluginHook;
import com.domsplace.Villages.Enums.ExpandMethod;
import com.domsplace.Villages.Enums.ManagerType;
import com.domsplace.Villages.Events.VillagesPluginReloadedEvent;
import com.domsplace.Villages.Objects.VillageMapRenderer;
import com.domsplace.Villages.Threads.VillageScoreboardThread;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

public class ConfigManager extends DataManager {
    private YamlConfiguration config;
    private File configFile;
    
    private VillageScoreboardThread scoreboardThread;
    
    public ConfigManager() {
        super(ManagerType.CONFIG);
    }
    
    public YamlConfiguration getCFG() {
        return config;
    }
    
    @Override
    public void tryLoad() throws IOException {
        this.configFile = new File(getDataFolder(), "config.yml");
        if(!this.configFile.exists()) configFile.createNewFile();
        this.config = YamlConfiguration.loadConfiguration(configFile);
        
        /*** GENERATE DEFAULT CONFIG ***/
        df("debug", false);
        Base.DebugMode = config.getBoolean("debug", false);
        
        //Worlds
        if(!config.contains("worlds")) {
            List<String> worlds = new ArrayList<String>();
            for(World w : Bukkit.getWorlds()) {
                worlds.add(w.getName());
            }
            
            df("worlds", worlds);
        }
        
        //SQL Settings
        df("sql.use", false);
        df("sql.host", "localhost");
        df("sql.port", "3306");
        df("sql.username", "root");
        df("sql.password", "password");
        df("sql.database", "minecraft");
        df("sql.prefix", "Villages");
        
        //Colors
        df("colors.default", "&7");
        df("colors.important", "&9");
        df("colors.error", "&c");
        df("colors.prefix.chat", true);
        df("colors.prefix.messages", "&9[&7Villages&9]");
        df("colors.prefix.village", "&9[&7%v%&9]");
        df("colors.prefix.wilderness", "Wilderness");
        df("colors.players.friend", "&a");
        df("colors.players.foe", "&4");
        
        //Protection
        df("protection.grief.village.use", true);
        df("protection.grief.village.break", false);
        df("protection.grief.village.place", false);
        df("protection.grief.village.mine", false);
        df("protection.grief.village.tnt", false);
        
        df("protection.grief.wilderness.use", true);
        df("protection.grief.wilderness.break", true);
        df("protection.grief.wilderness.place", true);
        df("protection.grief.wilderness.mine", true);
        df("protection.grief.wilderness.tnt", false);
        
        
        df("protection.pvp.village.samevillage", false);
        df("protection.pvp.village.differentvillage", true);
        df("protection.pvp.village.notinvillage", false);
        
        df("protection.pvp.wilderness.samevillage", false);
        df("protection.pvp.wilderness.differentvillage", true);
        df("protection.pvp.wilderness.notinvillage", false);
        
        String mobspawningkey = "protection.mobspawning.village.";
        for(EntityType t : EntityType.values()) {
            if(t == null || t.getName() == null) continue;
            if(!t.isAlive()) continue;
            df(mobspawningkey + t.getName(), true);
        }
        
        mobspawningkey = "protection.mobspawning.wilderness.";
        for(EntityType t : EntityType.values()) {
            if(t == null || t.getName() == null) continue;
            if(!t.isAlive()) continue;
            df(mobspawningkey + t.getName(), true);
        }
        
        //PLANNED:
        //df("protection.mobgriefing.village.creeper", false);
        //df("protection.mobgriefing.village.enderman", false);
        
        //df("protection.mobgriefing.wilderness.creeper", true);
        //df("protection.mobgriefing.wilderness.enderman", true);
        
        //Plugins
        df("plugins.worldguard", true);
        df("plugins.tagapi", true);
        df("plugins.herochat", true);
        df("plugins.vault", true);
        //df("plugins.dynmap", true); (REMOVED TEMPORARILY)
        
        //Features
        df("features.lists.topvillages", true);
        df("features.lists.villagemembers", true);
        df("features.lists.taxday", true);
        df("features.cyclespeed", 60);
        //df("features.map", true); (COMING SOON!)
        df("features.banks.item", true);
        df("features.banks.money", true);
        df("features.plots", true);
        //df("features.ranks", true); (COMING SOON!)
        df("features.updates", true);
        df("features.expand.method", "CHUNK");
        df("features.guiscreen", true);
        df("features.largemaps", true);
        
        List<String> defaultCommands = new ArrayList<String>();
        defaultCommands.add("v");
        defaultCommands.add("town");
        defaultCommands.add("city");
        df("features.aliases.village", defaultCommands);
        
        //Costs
        df("costs.createvillage", 1000);
        df("costs.expandvillage", 100);
        
        //Messages
        df("messages.names.wilderness", "Wilderness");
        
        df("messages.village.youenter", true);
        df("messages.village.friendlyenters", false);
        df("messages.village.othervillage", false);
        df("messages.village.wilderness", false);
        
        df("messages.wilderness.youenter", true);
        df("messages.wilderness.friendlyenters", false);
        df("messages.wilderness.othervillage", false);
        df("messages.wilderness.wilderness", false);    
        
        //Commands
        List<String> cmds;
        
        cmds = new ArrayList<String>();
        cmds.add("say Please Change this command in the config.yml file.");
        df("commands.village.created", cmds);
        
        cmds = new ArrayList<String>();
        cmds.add("say Please Change this command in the config.yml file.");
        df("commands.village.deleted", cmds);
        
        cmds = new ArrayList<String>();
        cmds.add("say Please Change this command in the config.yml file.");
        df("commands.village.playeradded", cmds);
        
        cmds = new ArrayList<String>();
        cmds.add("say Please Change this command in the config.yml file.");
        df("commands.village.playerremoved", cmds);
        
        cmds = new ArrayList<String>();
        cmds.add("say Please Change this command in the config.yml file.");
        df("commands.village.expand", cmds);
        
        //Fire Event
        VillagesPluginReloadedEvent event = new VillagesPluginReloadedEvent(config);
        event.fireEvent();
        
        //Load Worlds
        List<World> worlds = new ArrayList<World>();
        Base.TryWorlds = config.getStringList("worlds");
        for(String s : TryWorlds) {
            World w = Bukkit.getWorld(s);
            if(w == null) {
                log("Unknown world \"" + s + "\".");
                continue;
            }
            
            worlds.add(w);
        }
        Base.VillageWorlds = worlds;
        
        //Load SQL
        DataManager.SQL_MANAGER.setupSQL(
            gs("sql.host"), 
            gs("sql.port", "3306"),
            gs("sql.username"), 
            gs("sql.password"), 
            gs("sql.database"), 
            gs("sql.prefix")
        );
        
        if(config.getBoolean("sql.use", false)) {
            log("Opening Connection to SQL...");
            if(!DataManager.SQL_MANAGER.connect()) {
                log("Failed to Connect to SQL! Using YML instead.");
                Base.useSQL = false;
            } else {
                log("Connected to SQL!");
                Base.useSQL = true;
            }
        }
        
        //Load Colors
        Base.ChatDefault = colorise(loadColor("colors.default"));
        Base.ChatImportant = colorise(loadColor("colors.important"));
        Base.ChatError = colorise(loadColor("colors.error"));
        
        Base.ChatPrefix = loadColor("colors.prefix.messages");
        Base.VillagePrefix = loadColor("colors.prefix.village");
        Base.WildernessPrefix = loadColor("colors.prefix.wilderness");
        
        Base.FriendColor = loadColor("colors.players.friend");
        Base.EnemyColor = loadColor("colors.players.foe");
        
        Base.Wilderness = loadColor("messages.names.wilderness");
        
        if(gs("features.expand.method", "CHUNK").equalsIgnoreCase("CLASSIC")){
            Base.ExpandingMethod = ExpandMethod.CLASSIC;
        } else {
            Base.ExpandingMethod = ExpandMethod.PER_CHUNK;
        }
        
        if(config.getBoolean("features.largemaps", true)) {
            VillageMapRenderer.REGION_DIVIDE = 2;
            VillageMapRenderer.PLAYER_MULTIPLY = 1;
        } else {
            VillageMapRenderer.REGION_DIVIDE = 1;
            VillageMapRenderer.PLAYER_MULTIPLY = 2;
        }
        
        //Setup Hooking Options
        PluginHook.VAULT_HOOK.shouldHook(config.getBoolean("plugins.vault", true));
        PluginHook.WORLD_GUARD_HOOK.shouldHook(config.getBoolean("plugins.worldguard", true));
        PluginHook.HERO_CHAT_HOOK.shouldHook(config.getBoolean("plugins.herochat", true));
        PluginHook.TAG_API_HOOK.shouldHook(config.getBoolean("plugins.tagapi", true));
        
        Base.useScoreboards = 
                Base.getConfig().getBoolean("features.lists.topvillages", true) || 
                Base.getConfig().getBoolean("features.lists.villagemembers", true) ||
                Base.getConfig().getBoolean("features.lists.taxday", true);
        
        //Restart Scoreboard Thread
        if(this.scoreboardThread != null) {
            this.scoreboardThread.stopThread();
        }
        
        this.scoreboardThread = new VillageScoreboardThread();
        
        this.trySave();
    }
    
    @Override
    public void trySave() throws IOException {
        this.config.save(configFile);
    }
    
    private void df(String key, Object o) {
        if(config.contains(key)) return;
        config.set(key, o);
    }
    
    private String gs(String key) {
        return gs(key, "");
    }
    
    private String gs(String key, String dv) {
        if(!config.contains(key)) return dv;
        return config.getString(key);
    }
    
    private String loadColor(String key) {
        return gs(key, "&7");
    }
}

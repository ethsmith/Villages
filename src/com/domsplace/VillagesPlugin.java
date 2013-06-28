package com.domsplace;

import com.domsplace.Commands.*;
import com.domsplace.Listeners.*;
import com.domsplace.DataManagers.*;
import com.domsplace.Utils.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VillagesPlugin extends JavaPlugin {
    
    public static PluginManager pluginManager;
    public static boolean LoadedPlugin = false;
    
    //Commands
    public static VillagesVillageAdminCommand VillageAdminCommand;
    public static VillagesVillagesCommand VillagesCommand;
    public static VillagesVillageCommand VillageCommand;
    public static VillagesCreateVillageCommand CreateVillageCommand;
    public static VillagesVillageInviteCommand VillageInviteCommand;
    public static VillagesVillageTopCommand VillageTopCommand;
    
    //Listeners
    public static VillageConfigListener ConfigListener;
    public static VillageVillagesListener VillagesListener;
    public static VillageUpkeepListener UpkeepListener;
    public static VillageTeamListener TeamListener;
    public static VillageDynmapListener DynmapListener;
    
    @Override
    public void onEnable() {
        //Reference this plugin globally.
        VillageUtils.plugin = getVillagesPlugin();
        
        VillagePluginManager.LoadPluginYML();
        
        pluginManager = Bukkit.getPluginManager();
        
        if(!VillageConfigManager.LoadConfig()) {
            Disable();
            return;
        }
        
        if(!VillageLanguageManager.LoadLanguage()) {
            Disable();
            return;
        }
        
        if(!VillageUpkeepManager.SetupUpkeep()) {
            Disable();
            return;
        }
        
        //Load Commands
        VillageAdminCommand = new VillagesVillageAdminCommand(this);
        VillagesCommand = new VillagesVillagesCommand(this);
        VillageCommand = new VillagesVillageCommand(this);
        CreateVillageCommand = new VillagesCreateVillageCommand(this);
        VillageInviteCommand = new VillagesVillageInviteCommand(this);
        VillageTopCommand = new VillagesVillageTopCommand(this);
        
        //Load Listeners
        ConfigListener = new VillageConfigListener(this);
        VillagesListener = new VillageVillagesListener(this);
        UpkeepListener = new VillageUpkeepListener(this);
        DynmapListener = new VillageDynmapListener(this);
        
        if(VillageUtils.useTagAPI) {
            TeamListener = new VillageTeamListener(this);
        }
        
        //Register Commands
        getCommand("villageadmin").setExecutor(VillageAdminCommand);
        getCommand("villages").setExecutor(VillagesCommand);
        getCommand("village").setExecutor(VillageCommand);
        getCommand("createvillage").setExecutor(CreateVillageCommand);
        getCommand("villageinvite").setExecutor(VillageInviteCommand);
        getCommand("villageaccept").setExecutor(VillageInviteCommand);
        getCommand("villagedeny").setExecutor(VillageInviteCommand);
        getCommand("villagetop").setExecutor(VillageTopCommand);
        
        //Register Events
        pluginManager.registerEvents(ConfigListener, this);
        pluginManager.registerEvents(VillagesListener, this);
        pluginManager.registerEvents(UpkeepListener, this);
        pluginManager.registerEvents(DynmapListener, this);
        
        if(VillageUtils.useTagAPI) {
            pluginManager.registerEvents(TeamListener, this);
        }
        
        //Load in Villages
        VillageVillagesUtils.LoadAllVillages();
        
        //Setup Scoreboards
        VillageScoreboardUtils.SetupScoreboard();
        
        //Start Rendering the Dynmap
        if(VillageUtils.useDynmap) {
            VillageDynmapUtils.FixDynmapRegions();
        }
        
        //Managed to load the plugin successfully!
        LoadedPlugin = true;
        VillageUtils.broadcast(
            "Villages.villageadmin", 
            "§dLoaded " + VillagePluginManager.getName() + 
            " version " + VillagePluginManager.getVersion() + 
            " successfully."
        );
    }
    
    @Override
    public void onDisable() {
        if(!LoadedPlugin) {
            VillageUtils.Error("Failed to load plugin.", "Check console for cause.");
            Disable();
            return;
        }
        
        //Stop Threads
        ConfigListener.AutoSaveConfig.cancel();
        VillagesListener.AutoSaveVillages.cancel();
        UpkeepListener.AutoCheckUpkeep.cancel();
        DynmapListener.FixDynmapMap.cancel();
        
        //Unload Dynmap Markers
        if(VillageUtils.useDynmap && VillageDynmapUtils.markers != null) {
            VillageDynmapUtils.UnloadDynmapRegions();
        }
        
        //Save Data
        VillageVillagesUtils.SaveAllVillages();
    }
    
    public void Disable() {
        pluginManager.disablePlugin(this);
    }
    
    //Self Referencing
    public static com.domsplace.VillagesPlugin getVillagesPlugin() {
        try {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Villages");

            if (plugin == null || !(plugin instanceof com.domsplace.VillagesPlugin)) {
                return null;
            }

            return (com.domsplace.VillagesPlugin) plugin;
        } catch(NoClassDefFoundError e) {
            return null;
        }
    }
}

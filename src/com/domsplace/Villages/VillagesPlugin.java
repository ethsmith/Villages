package com.domsplace.Villages;

import com.domsplace.Villages.Commands.*;
import com.domsplace.Villages.DataManagers.*;
import com.domsplace.Villages.Listeners.*;
import com.domsplace.Villages.Utils.*;
import com.domsplace.Villages.Bases.*;
import com.domsplace.Villages.Listeners.*;
import com.domsplace.Villages.Threads.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class VillagesPlugin extends JavaPlugin {
    public static boolean loadedPlugin = false;
    
    @Override
    public void onEnable() {
        //Reference this plugin globally.
        Base.setPlugin(this);
        
        DataManagerBase.loadAll();
        
        //Load Commands
        AdminCommand VillageAdminCommand = new AdminCommand();
        VillagesCommand VillagesCommand = new VillagesCommand();
        VillageCommand VillageCommand = new VillageCommand();
        CreateVillageCommand CreateVillageCommand = new CreateVillageCommand();
        VillageInviteCommand VillageInviteCommand = new VillageInviteCommand();
        VillageAcceptCommand VillageAcceptCommand = new VillageAcceptCommand();
        VillageDenyCommand VillageDenyCommand = new VillageDenyCommand();
        VillageTopCommand VillageTopCommand = new VillageTopCommand();
        TaxDayCommand TaxDayCommand = new TaxDayCommand();
        TaxAmountCommand TaxAmountCommand = new TaxAmountCommand();
        BroadcastCommand BroadcastCommand = new BroadcastCommand();
        
        //Load Listeners
        VillagesListener VillagesListener = new VillagesListener();
        CustomEventListener CustomListener = new CustomEventListener();
        CommandListener CommandsListener = new CommandListener();
        MonsterListener MonsterListener = new MonsterListener();
        
        if(Utils.useTagAPI) {
            TagAPIListener TeamListener = new TagAPIListener();
        }
        
        if(Utils.useHerochat) {
            HeroChatListener HeroChatListener = new HeroChatListener();
        }
        
        //Load in Threads
        DynmapThread DynmapThread = new DynmapThread();
        ConfigSaveThread ConfigSaveThread = new ConfigSaveThread();
        UpkeepThread UpkeepThread = new UpkeepThread();
        
        //Load in Villages
        VillageUtils.LoadAllVillages();
        
        //Setup Scoreboards
        VillageScoreboardUtils.SetupScoreboard();
        
        //Start Rendering the Dynmap
        if(Utils.useDynmap) {
            VillageDynmapUtils.FixDynmapRegions();
        }
        
        //Update Permission Messages
        CommandBase.updateAllPermissionMessages();
        
        //Managed to load the plugin successfully!
        loadedPlugin = true;
        Utils.broadcast(
            "Villages.villageadmin", 
            "§dLoaded " + this.getName() + 
            " version " + DataManagerBase.PLUGIN_MANAGER.getVersion() + 
            " successfully."
        );
    }
    
    @Override
    public void onDisable() {
        if(!loadedPlugin) {
            Utils.Error("Failed to load plugin.", null);
            Disable();
            return;
        }
        
        //Stop Threads
        ThreadBase.stopAllThreads();
        
        //Unload Dynmap Markers
        if(Utils.useDynmap) {
            if(VillageDynmapUtils.markers != null) {
                VillageDynmapUtils.UnloadDynmapRegions();
            }
        }
        
        //Save Data
        VillageUtils.SaveAllVillages();
    }
    
    public void Disable() {
        Bukkit.getPluginManager().disablePlugin(this);
    }
}

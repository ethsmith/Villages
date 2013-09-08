package com.domsplace.Villages;

import com.domsplace.Villages.Commands.*;
import com.domsplace.Villages.Bases.*;
import com.domsplace.Villages.Hooks.*;
import com.domsplace.Villages.Listeners.*;
import com.domsplace.Villages.Threads.*;
import com.domsplace.Villages.Utils.*;
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
        PluginHookListener PluginHookListener = new PluginHookListener();
        
        //Load Hooks
        HeroChatHook HeroChatHook = new HeroChatHook();
        TagAPIHook TagAPIHook = new TagAPIHook();
        WorldGuardHook WorldGuardHook = new WorldGuardHook();
        VaultHook VaultHook = new VaultHook();
        
        //Hook
        PluginHookBase.hookAll();
        
        //Load in Threads
        ConfigSaveThread ConfigSaveThread = new ConfigSaveThread();
        UpkeepThread UpkeepThread = new UpkeepThread();
        
        //Setup Scoreboards
        VillageScoreboardUtils.SetupScoreboard();
        
        //Update Permission Messages
        CommandBase.updateAllPermissionMessages();
        
        //Managed to load the plugin successfully!
        loadedPlugin = true;
        Base.broadcast(
            "Villages.villageadmin", 
            "§dLoaded " + this.getName() + 
            " version " + DataManagerBase.PLUGIN_MANAGER.getVersion() + 
            " successfully."
        );
    }
    
    @Override
    public void onDisable() {
        if(!loadedPlugin) {
            Base.Error("Failed to load plugin.", null);
            Disable();
            return;
        }
        
        //Stop Threads
        ThreadBase.stopAllThreads();
        
        //Unhook all
        PluginHookBase.unhookAll();
        
        //Save Data
        DataManagerBase.saveAll();
    }
    
    public void Disable() {
        Bukkit.getPluginManager().disablePlugin(this);
    }
}

    package com.domsplace.Villages;

import com.domsplace.Villages.Commands.SubCommands.Bank.VillageBankWithdraw;
import com.domsplace.Villages.Commands.SubCommands.Bank.VillageBankDeposit;
import com.domsplace.Villages.Commands.SubCommands.Bank.VillageBankOpen;
import com.domsplace.Villages.Commands.SubCommands.AdminCommands.*;
import com.domsplace.Villages.Bases.*;
import com.domsplace.Villages.Commands.*;
import com.domsplace.Villages.Commands.SubCommands.*;
import com.domsplace.Villages.Commands.SubCommands.Mayor.*;
import com.domsplace.Villages.Commands.SubCommands.Plot.*;
import com.domsplace.Villages.Commands.SubCommands.Tax.VillageTaxCheck;
import com.domsplace.Villages.Listeners.*;
import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Objects.VillageMap;
import com.domsplace.Villages.Threads.*;
import org.bukkit.Bukkit;

public class VillagesPlugin extends PluginBase {
    public boolean enabled;
    
    @Override
    public void onEnable() {
        Base.setPlugin(this);
        
        if(!DataManager.loadAll()) {
            disable();
            return;
        }
        
        //Load Base Commands
        new VillageCommand();
        
        //Load SubCommands
        new VillageList();
        new VillageCreate();
        new VillageHelp();
        new VillageInfo();
        new VillageInvite();
        new VillageMessage();
        new VillageAccept();
        new VillageDeny();
        new VillageSpawn();
        new VillageLeave();
        new VillageTop();
        new VillageLookup();
        new VillageMapSubCommand();
        
        new VillageAdmin();
        new VillageAdminSave();
        new VillageAdminReload();
        new VillageAdminDelete();
        new VillageAdminAddPlayer();
        new VillageAdminRemovePlayer();
        new VillageAdminSetName();
        new VillageAdminSetMayor();
        new VillageAdminSetDescription();
        //new VillageAdminGUI();
        
        new VillageBankDeposit();
        new VillageBankOpen();
        new VillageBankWithdraw();
        
        new VillageMayor();
        new VillageMayorClose();
        new VillageMayorKick();
        new VillageMayorSetMayor();
        new VillageMayorSetDescription();
        new VillageMayorSetName();
        new VillageMayorExpand();
        new VillageMayorSetSpawn();
        new VillageMayorExplode();
        
        new VillagePlotCheck();
        new VillagePlotClaim();
        new VillagePlotSetOwner();
        new VillagePlotSetPrice();
        
        new VillageTaxCheck();
        
        //Hook
        PluginHook.hookAll();
        
        //Start Threads
        new ConfigSaveThread();
        new UpdateThread();
        new UpkeepThread();
        
        //Load Listeners
        new CustomEventListener();
        new VillageGriefListener();
        new PlotGriefListener();
        new VillagePvPListener();
        new WildernessGriefListener();
        new WildernessPvPListener();
        new MoveNotificationListener();
        new MobSpawningListener();
        new VillageInviteListener();
        new VillageScoreboardListener();
        new ServerUnloadListener();
        new VillageCommandListener();
        new VillagesChatListener();
        new CustomCommandListener();
        
        //Invoke the Map Class
        VillageMap.invoke();
        
        this.enabled = true;
        Base.debug("Enabled Villages!");
        if(Base.getConfig().getBoolean("features.guiscreen", true)) {
            System.out.println("\n" + Base.GUIScreen);
        }
    }
    
    @Override
    public void onDisable() {
        if(!enabled) {
            Base.debug("Failed to Enable Villages!");
            return;
        }
        
        //Close the GUI
        if(Base.guiManager != null) {
            Base.guiManager.close();
            Base.guiManager = null;
        }
        
        VillageThread.stopAllThreads();
        DataManager.saveAll();
        Village.deRegisterVillages(Village.getVillages());
    }
    
    public boolean enabled() {
        return this.enabled;
    }
    
    public void disable() {
        Bukkit.getPluginManager().disablePlugin(this);
    }
}

package com.domsplace.Villages.Threads;

import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.VillageThread;


public class UpdateThread extends VillageThread {
    public static String CheckUpdateURL = "http://domsplace.com/ajax/getProjectVersion.php?name=Villages";
    public static String LatestVersionURL = "http://dev.bukkit.org/bukkit-plugins/villages/";
    
    public UpdateThread() {
        super(10, 3600, true);
    }
    
    @Override
    public void run() {
        String version = "x.xx";
        debug("Checking for updates..");
        try {
            version = getStringFromURL(CheckUpdateURL);
        } catch(Exception e) {
            log("Failed to check for updates!");
        }
        
        if(version.equalsIgnoreCase("x.xx")) {
            log("Failed to check for updates!");
        }
        
        debug("ONLINE VERSION: " + version);
        debug("THIS VERSION: " + DataManager.PLUGIN_MANAGER.getVersion());
        if(version.equals(DataManager.PLUGIN_MANAGER.getVersion())) return;
        
        if(!isDouble(version)) {log("Failed to check for updates! Will keep trying..."); return;}
        
        if(getDouble(version) <= getDouble(DataManager.PLUGIN_MANAGER.getVersion())) return;
        
        log("New version available! Version: " + version);
        broadcast("Villages.admin", new String[]{
            ChatImportant + "The new Version of " + getPlugin().getName() + " is available to download!", 
            "Download " + getPlugin().getName() + " v" + version + " from: " + LatestVersionURL
        });
        this.stopThread();
    }
}

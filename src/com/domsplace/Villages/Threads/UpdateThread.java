package com.domsplace.Villages.Threads;

import com.domsplace.Villages.Bases.DataManagerBase;
import com.domsplace.Villages.Bases.ThreadBase;
import com.domsplace.Villages.Utils.Utils;

public class UpdateThread extends ThreadBase {
    public UpdateThread() {
        super(10, 1800, true);
    }
    
    @Override
    public void run() {
        String version = "x.xx";
        try {
            version = getStringFromURL(CheckUpdateURL);
        } catch(Exception e) {
            log("Failed to check for updates!");
        }
        
        if(version.equalsIgnoreCase("x.xx")) {
            log("Failed to check for updates!");
        }
        
        debug("ONLINE VERSION: " + version);
        if(version.equals(DataManagerBase.PLUGIN_MANAGER.getVersion())) return;
        debug("THIS VERSION: " + DataManagerBase.PLUGIN_MANAGER.getVersion());
        
        log("New version available! Version: " + version);
        Utils.broadcast("Villages.villageadmin", new String[]{
            "New Version of " + getPlugin().getName() + " is available to download!", 
            "Download " + getPlugin().getName() + " v" + version + " from: " + LatestVersionURL
        });
        this.stopThread();
    }
}

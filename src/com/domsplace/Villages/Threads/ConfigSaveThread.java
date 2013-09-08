package com.domsplace.Villages.Threads;

import com.domsplace.Villages.Bases.DataManagerBase;
import com.domsplace.Villages.Bases.ThreadBase;

public class ConfigSaveThread extends ThreadBase {
    public ConfigSaveThread() {
        super(3, 1500);
    }
    
    @Override
    public void run() {
        log("Saving Data...");
        DataManagerBase.CONFIG_MANAGER.save();
        saveAllData();
    }
}

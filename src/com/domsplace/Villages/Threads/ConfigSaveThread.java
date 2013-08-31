package com.domsplace.Villages.Threads;

import com.domsplace.Villages.Bases.DataManagerBase;
import com.domsplace.Villages.Bases.ThreadBase;
import com.domsplace.Villages.Utils.VillageUtils;

public class ConfigSaveThread extends ThreadBase {
    public ConfigSaveThread() {
        super(3, 1500);
    }
    
    @Override
    public void run() {
        log("Saving Data...");
        DataManagerBase.CONFIG_MANAGER.saveConfig();
        VillageUtils.SaveAllVillages();
    }
}

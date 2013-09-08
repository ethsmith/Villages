package com.domsplace.Villages.Bases;

import com.domsplace.Villages.DataManagers.ConfigManager;
import com.domsplace.Villages.DataManagers.VillageBankManager;
import com.domsplace.Villages.DataManagers.VillageManager;

public class BaseBase extends Base {
    public static ConfigManager getConfigManager() {
        return DataManagerBase.CONFIG_MANAGER;
    }
    
    public static VillageManager getVillageManager() {
        return DataManagerBase.VILLAGE_MANAGER;
    }
    
    public static VillageBankManager getVillageBankManager() {
        return DataManagerBase.VILLAGE_BANK_MANAGER;
    }
    
    public static boolean saveAllData() {
        return DataManagerBase.saveAll();
    }
}
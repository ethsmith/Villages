package com.domsplace.Villages.Bases;

import com.domsplace.Villages.DataManagers.*;
import com.domsplace.Villages.Enums.ManagerType;
import com.domsplace.Villages.Utils.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataManagerBase extends BaseBase {
    private static final List<DataManagerBase> MANAGERS = new ArrayList<DataManagerBase>();
    
    public static final PluginManager PLUGIN_MANAGER = new PluginManager();
    public static final ConfigManager CONFIG_MANAGER = new ConfigManager();
    public static final LanguageManager LANGUAGE_MANAGER = new LanguageManager();
    public static final UpkeepManager UPKEEP_MANAGER = new UpkeepManager();
    
    protected static void registerManager(DataManagerBase manager) {
        DataManagerBase.getManagers().add(manager);
    }
    
    public static List<DataManagerBase> getManagers() {
        return DataManagerBase.MANAGERS;
    }
    
    public static boolean loadAll() {
        for(DataManagerBase manager : DataManagerBase.getManagers()) {
            if(!manager.load()) return false;
        }
        
        return true;
    }
    
    //Instance
    private ManagerType type;
    
    public DataManagerBase(ManagerType type) {
        this.type = type;
        DataManagerBase.registerManager(this);
    }
    
    public ManagerType getType() {
        return this.type;
    }
    
    public boolean load() {
        try {
            this.tryLoad();
            return true;
        } catch(Exception ex) {
            Utils.Error("Failed to load " + this.getType().getType(), ex);
            return false;
        }
    }
    
    public void tryLoad() throws IOException {
        
    }
}

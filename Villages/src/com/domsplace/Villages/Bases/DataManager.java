package com.domsplace.Villages.Bases;

import com.domsplace.Villages.DataManagers.*;
import com.domsplace.Villages.Enums.ManagerType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataManager extends Base {
    private static final List<DataManager> MANAGERS = new ArrayList<DataManager>();
    
    public static final PluginManager PLUGIN_MANAGER = new PluginManager();
    public static final ConfigManager CONFIG_MANAGER = new ConfigManager();
    public static final LanguageManager LANGUAGE_MANAGER = new LanguageManager();
    public static final SQLManager SQL_MANAGER = new SQLManager();
    public static final GUIManager GUI_MANAGER = new GUIManager();
    public static final UpkeepManager UPKEEP_MANAGER = new UpkeepManager();
    public static final VillageManager VILLAGE_MANAGER = new VillageManager();
    
    private static void registerManager(DataManager manager) {
        DataManager.MANAGERS.add(manager);
    }
    
    public static List<DataManager> getManagers() {
        return new ArrayList<DataManager>(MANAGERS);
    }
    
    public static boolean loadAll() {
        for(DataManager dm : MANAGERS) {
            if(dm.load()) continue;
            return false;
        }
        
        return true;
    }
    
    public static boolean saveAll() {
        for(DataManager dm : MANAGERS) {
            if(dm.getType().equals(ManagerType.CONFIG)) continue;
            if(dm.save()) continue;
            return false;
        }
        
        return true;
    }
    
    //Instance
    private ManagerType type;
    
    public DataManager(ManagerType type) {
        this.type = type;
        
        DataManager.registerManager(this);
    }
    
    public ManagerType getType() {
        return this.type;
    }
    
    public boolean load() {
        try {
            tryLoad();
            return true;
        } catch(IOException e) {
            error("Failed to load " + this.getType().getType(), e);
            return false;
        }
    }
    
    public void tryLoad() throws IOException {
    }
    
    public boolean save() {
        try {
            trySave();
            return true;
        } catch(IOException e) {
            error("Failed to save " + this.getType().getType(), e);
            return false;
        }
    }
    
    public void trySave() throws IOException {
    }
}

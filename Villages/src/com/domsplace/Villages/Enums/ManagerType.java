package com.domsplace.Villages.Enums;

import com.domsplace.Villages.Bases.Enum;

public class ManagerType extends Enum {
    public static final ManagerType CONFIG = new ManagerType("Configuration");
    public static final ManagerType LANGUAGE = new ManagerType("Language");
    public static final ManagerType PLUGIN = new ManagerType("Plugin");
    public static final ManagerType UPKEEP = new ManagerType("Upkeep");
    public static final ManagerType SQL = new ManagerType("SQL");
    public static final ManagerType VILLAGE = new ManagerType("Village");
    public static final ManagerType VILLAGE_BANK = new ManagerType("Village Bank");
    
    //Instance
    private String type;
    
    public ManagerType(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}

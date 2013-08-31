package com.domsplace.Villages.Enums;

import com.domsplace.Villages.Bases.EnumBase;

public class ManagerType extends EnumBase {
    public static final ManagerType CONFIG = new ManagerType("Configuration");
    public static final ManagerType LANGUAGE = new ManagerType("Language");
    public static final ManagerType PLUGIN = new ManagerType("Plugin");
    public static final ManagerType UPKEEP = new ManagerType("Upkeep");
    public static final ManagerType SQL = new ManagerType("SQL");
    
    //Instance
    private String type;
    
    public ManagerType(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}

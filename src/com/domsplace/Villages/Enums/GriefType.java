package com.domsplace.Villages.Enums;

import com.domsplace.Villages.Bases.Enum;

public class GriefType extends Enum {
    public static final GriefType INTERACT = new GriefType("Interact");
    public static final GriefType BREAK = new GriefType("Break");
    public static final GriefType PLACE = new GriefType("Place");
    public static final GriefType BLOCK_DAMAGE = new GriefType("Block Damage");
    public static final GriefType TNT_DAMAGE = new GriefType("TNT Explosion");
    
    private String type;
    
    public GriefType(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}

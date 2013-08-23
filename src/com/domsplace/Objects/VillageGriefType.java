package com.domsplace.Objects;

public class VillageGriefType {
    public static final VillageGriefType INTERACT = new VillageGriefType("INTERACT");
    public static final VillageGriefType BREAK = new VillageGriefType("BREAK");
    public static final VillageGriefType PLACE = new VillageGriefType("PLACE");
    
    private String type;
    
    public VillageGriefType(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}

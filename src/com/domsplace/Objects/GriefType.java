package com.domsplace.Objects;

public class GriefType {
    public static final GriefType INTERACT = new GriefType("INTERACT");
    public static final GriefType BREAK = new GriefType("BREAK");
    public static final GriefType PLACE = new GriefType("PLACE");
    
    private String type;
    
    public GriefType(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}

package com.domsplace.Villages.Enums;

import com.domsplace.Villages.Bases.Enum;

public class ExpandMethod extends Enum {
    public static final ExpandMethod CLASSIC = new ExpandMethod("Classic");
    public static final ExpandMethod PER_CHUNK = new ExpandMethod("Per Chunk");
    
    private String type;
    
    public ExpandMethod(String type) {
        this.type = type;
    }
    
    public String getType() {
        return this.type;
    }
}

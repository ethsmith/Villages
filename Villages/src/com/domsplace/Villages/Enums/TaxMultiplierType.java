package com.domsplace.Villages.Enums;

import com.domsplace.Villages.Bases.Enum;

public class TaxMultiplierType extends Enum {
    public static final TaxMultiplierType PLAYER = new TaxMultiplierType("Player");
    public static final TaxMultiplierType CHUNK = new TaxMultiplierType("Region");
    
    private String name;
    
    public TaxMultiplierType(String name) {
        this.name = name;
    }
    
    public String getName() {return this.name;}
}

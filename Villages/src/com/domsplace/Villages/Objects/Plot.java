package com.domsplace.Villages.Objects;

public class Plot {
    private Resident owner;
    private double cost;
    private Village village;
    private Region region;
    
    public Plot(Village parent, Region region) {
        this.village = parent;
        this.region = region;
        this.cost = -1.0d;
    }
    
    public boolean isOwned() {return this.owner != null;}
    public boolean isForSale() { return this.cost >= 0; }
    
    public Resident getOwner() {return this.owner;}
    public Village getVillage() {return this.village;}
    public double getPrice() {return this.cost;}
    public Region getRegion() {return this.region;}
    
    public void setPrice(double price) {this.cost = price;}
    public void setOwner(Resident owner) {this.owner = owner;}

    public boolean canBuild(Resident res) {
        if(res == null) return false;
        if(this.village.isMayor(res)) return true;
        return this.owner.equals(res);
    }
}

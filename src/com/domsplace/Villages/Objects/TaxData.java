package com.domsplace.Villages.Objects;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Enums.DeleteCause;
import com.domsplace.Villages.Events.VillageDeletedEvent;

public class TaxData {
    private Village village;
    private Tax tax;
    private long lastChecked;
    
    public TaxData(Village village, Tax tax) {
        this(village, tax, Base.getNow());
    }
    
    public TaxData(Village village, Tax tax, long lastChecked) {
        this.village = village;
        this.tax = tax;
        this.lastChecked = lastChecked;
    }
    
    public Village getVillage() {return this.village;}
    public Tax getTax() {return this.tax;}
    public long getLastChecked() {return this.lastChecked;}

    public void setLastChecked(long i) {this.lastChecked = i;}

    public void run() {
        this.lastChecked = Base.getNow();
        village.broadcast(tax.getMessage());
        if(Base.useEconomy && Base.getConfig().getBoolean("features.banks.money", true)) {
            double vmoney = Base.getBalance(village);
            double cost = this.tax.getRelativeCost(village);
            
            if(vmoney < cost) {
                //Cant Afford Money
                VillageDeletedEvent event = new VillageDeletedEvent(village, DeleteCause.UPKEEP_FAILED);
                event.fireEvent();
                
                if(!event.isCancelled()) {
                    Base.debug(village.getName() + " doesn't have the " + cost + " to continue!");
                    Base.broadcast(Base.gk("taxdayfail", village));
                    Village.deleteVillage(village);
                    return;
                }
            }
            
            village.getBank().addWealth(-cost);
        }
        
        if(Base.getConfig().getBoolean("features.banks.item", true)) {
            Bank bank = village.getBank();
            if(!bank.containsItems(this.tax.getRelativeItemsCost(village))) {
                VillageDeletedEvent event = new VillageDeletedEvent(village, DeleteCause.UPKEEP_FAILED);
                event.fireEvent();
                if(event.isCancelled()) return;
                
                Base.debug(village.getName() + " doesn't have the items needed to continue!");
                Base.broadcast(Base.gk("taxdayfail", village));
                Village.deleteVillage(village);
            }
            
            bank.removeItems(this.tax.getRelativeItemsCost(village));
        }
    }
}

package com.domsplace.Villages.Threads;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.VillageThread;
import com.domsplace.Villages.Objects.Tax;
import com.domsplace.Villages.Objects.TaxData;
import com.domsplace.Villages.Objects.Village;

public class UpkeepThread extends VillageThread {
    public UpkeepThread() {
        super(1, 10, true);
    }
    
    @Override
    public void run() {
        for(Tax t : Tax.getTaxes()) {
            for(Village v : Village.getVillages()) {
                TaxData td = v.getTaxData(t);
                if(td == null) {
                    td = new TaxData(v, t);
                    v.addTaxData(td);
                }
                
                long lastChecked = td.getLastChecked();
                long now = Base.getNow();
                
                long diff = (long) (t.getHours() * 3600000d); //Hours to Milliseconds
                
                long difference = now - lastChecked;
                if(difference < diff) continue;
                
                td.run();
            }
        }
    }
}

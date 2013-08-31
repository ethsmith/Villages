package com.domsplace.Villages.Threads;

import com.domsplace.Villages.Bases.DataManagerBase;
import com.domsplace.Villages.Bases.ThreadBase;
import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Utils.VillageUtils;
import java.util.ArrayList;

public class UpkeepThread extends ThreadBase {
    public UpkeepThread() {
        super(3, 30);
    }
    
    @Override
    public void run() {
        ArrayList<Village> villages = new ArrayList<Village>(VillageUtils.Villages);
        for(Village village : villages) {
            DataManagerBase.UPKEEP_MANAGER.checkVillage(village);
        }
    }
}

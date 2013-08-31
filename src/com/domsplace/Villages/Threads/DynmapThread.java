package com.domsplace.Villages.Threads;

import com.domsplace.Villages.Bases.ThreadBase;
import com.domsplace.Villages.Utils.VillageDynmapUtils;
import com.domsplace.Villages.Utils.Utils;

public class DynmapThread extends ThreadBase {
    public DynmapThread() {
        super(3, 3, true);
    }
    
    @Override
    public void run() {
        if(!Utils.useDynmap) {
            return;
        }

        VillageDynmapUtils.FixDynmapRegions();
    }
}

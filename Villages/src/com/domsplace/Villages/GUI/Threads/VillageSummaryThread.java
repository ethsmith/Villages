/*
 * Copyright 2013 Dominic.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.domsplace.Villages.GUI.Threads;

import com.domsplace.Villages.Bases.VillageThread;
import com.domsplace.Villages.GUI.Frames.VillageSummaryPanel;

/**
 * @author      Dominic
 * @since       15/10/2013
 */
public class VillageSummaryThread extends VillageThread {
    private VillageSummaryPanel panel;
    
    public VillageSummaryThread(VillageSummaryPanel panel) {
        super(1, 1);
        this.panel = panel;
    }
    
    public VillageSummaryPanel getPanel() {return this.panel;}
    
    @Override
    public void run() {
        if(this.panel.getVillage() == null) {
            return;
        }
        this.panel.update();
    }
}

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
import com.domsplace.Villages.GUI.Frames.WelcomePanel;
import com.domsplace.Villages.Objects.Village;
import java.awt.BorderLayout;

/**
 * @author      Dominic
 * @since       15/10/2013
 */
public class VillageWelcomePanelThread extends VillageThread {
    private WelcomePanel panel;
    
    public VillageWelcomePanelThread(WelcomePanel panel) {
        super(1,1);
        this.panel = panel;
    }
    
    public WelcomePanel getWelcomePanel() {return this.panel;}
    
    @Override
    public void run() {
        for(VillageSummaryPanel vsp : this.panel.getVillagesPanels()) {
            if(vsp == null || vsp.getVillage() == null) {
                this.panel.remove(vsp);
                this.panel.removeVillageSummaryPanel(vsp);
                continue;
            }
        }
            
        for(Village v : Village.getVillages()) {
            boolean found = false;
            for(VillageSummaryPanel vsp : this.panel.getVillagesPanels()) {
                if(vsp == null) {
                    this.panel.remove(vsp);
                    this.panel.removeVillageSummaryPanel(vsp);
                    continue;
                }
                if(!vsp.getVillage().equals(v)) continue;
                found = true;
                break;
            }
            if(found) continue;
            
            VillageSummaryPanel vsp = new VillageSummaryPanel(v);
            this.panel.getPanel().add(vsp);
            this.panel.addVillageSummaryPanel(vsp);
        }
    }
}

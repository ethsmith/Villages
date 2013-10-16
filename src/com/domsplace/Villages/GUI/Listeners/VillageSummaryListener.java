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

package com.domsplace.Villages.GUI.Listeners;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.VillageListener;
import com.domsplace.Villages.Events.VillageCreatedEvent;
import com.domsplace.Villages.Events.VillageDeletedEvent;
import com.domsplace.Villages.Events.VillageGUICloseEvent;
import com.domsplace.Villages.GUI.Frames.VillageSummaryPanel;
import java.awt.BorderLayout;
import org.bukkit.event.EventHandler;

/**
 * @author      Dominic
 * @since       15/10/2013
 */
public class VillageSummaryListener extends VillageListener {
    @EventHandler
    public void stopSummaryThread(VillageGUICloseEvent e) {
        for(VillageSummaryPanel panel : e.getFrame().getTabbedPane().getWelcomePanel().getVillagesPanels()) {
            panel.remove();
        }
        
        e.getFrame().getTabbedPane().getWelcomePanel().getThread().stopThread();
        this.deRegisterListener();
    }
    
    @EventHandler
    public void catchVillageRemoved(VillageDeletedEvent e) {
        VillageSummaryPanel pnl = null;
        for(VillageSummaryPanel panel : Base.guiManager.getFrame().getTabbedPane().getWelcomePanel().getVillagesPanels()) {
            if(!panel.getVillage().equals(e.getVillage())) continue;
            pnl = panel;
            break;
        }
        
        if(pnl == null) return;
        pnl.remove();
    }
    
    @EventHandler
    public void catchVillageCreated(VillageCreatedEvent e) {
        VillageSummaryPanel panel = new VillageSummaryPanel(e.getVillage());
        Base.guiManager.getFrame().getTabbedPane().getWelcomePanel().addVillageSummaryPanel(panel);
        Base.guiManager.getFrame().getTabbedPane().getWelcomePanel().getPanel().add(panel);
    }
}

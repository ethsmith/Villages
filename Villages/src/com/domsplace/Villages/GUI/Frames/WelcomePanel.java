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

package com.domsplace.Villages.GUI.Frames;

import com.domsplace.Villages.GUI.Listeners.VillageSummaryListener;
import com.domsplace.Villages.GUI.Threads.VillageWelcomePanelThread;
import com.domsplace.Villages.Objects.Village;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * @author      Dominic
 * @since       15/10/2013
 */
public class WelcomePanel extends JScrollPane {
    private JPanel panel;
    private List<VillageSummaryPanel> villagesPanel;
    
    private JLabel villagesLabel;
    
    private VillageSummaryListener listener;
    private VillageWelcomePanelThread thread;
    
    public WelcomePanel() {this(new JPanel(new FlowLayout()));}
    
    private WelcomePanel(JPanel panel) {
        super(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.panel = panel;
        this.panel.setVisible(true);
        this.panel.setBackground(Color.LIGHT_GRAY);
        
        this.villagesLabel = new JLabel();
        
        this.villagesPanel = new ArrayList<VillageSummaryPanel>();
        for(Village v : Village.getVillages()) {
            VillageSummaryPanel vsp = new VillageSummaryPanel(v);
            this.panel.add(vsp);
            this.villagesPanel.add(vsp);
        }
        
        this.listener = new VillageSummaryListener();
        this.thread = new VillageWelcomePanelThread(this);
        
        this.setVisible(true);
    }
    
    public JPanel getPanel() {return this.panel;}
    public List<VillageSummaryPanel> getVillagesPanels() {return new ArrayList<VillageSummaryPanel>(this.villagesPanel);}
    public VillageWelcomePanelThread getThread() {return this.thread;}
    
    public void removeVillageSummaryPanel(VillageSummaryPanel panel) {this.villagesPanel.remove(panel);}
    public void addVillageSummaryPanel(VillageSummaryPanel panel) {this.villagesPanel.add(panel);}
}

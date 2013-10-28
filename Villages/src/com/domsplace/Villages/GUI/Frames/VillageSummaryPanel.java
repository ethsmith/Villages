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

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.GUI.Threads.VillageSummaryThread;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author      Dominic
 * @since       15/10/2013
 */
public class VillageSummaryPanel extends JPanel {
    private Village village;
    private VillageSummaryThread thread;
    
    private JLabel nameLabel;
    
    public VillageSummaryPanel(Village v) {
        this.village = v;
        this.thread = new VillageSummaryThread(this);
        
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        
        //Label
        this.nameLabel = new JLabel("Name Goes Here ");
        
        //SetVisible
        this.nameLabel.setVisible(true);
        
        this.setBackground(Color.YELLOW);
        
        //Add Components
        this.add(this.nameLabel);
        this.setVisible(true);
        
        this.update();
    }
    
    public Village getVillage() {return this.village;}
    public VillageSummaryThread getThread() {return this.thread;}
    
    public void remove() {
        this.thread.stopThread();
        this.getParent().remove(this);
        Base.guiManager.getFrame().getTabbedPane().getWelcomePanel().removeVillageSummaryPanel(this);
    }
    
    public final void update() {
        //Base.debug("Updating Summary Panel for Village " + this.village.getName());
        this.nameLabel.setText(village.getName());
    }
}

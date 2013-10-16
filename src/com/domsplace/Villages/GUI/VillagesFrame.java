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

package com.domsplace.Villages.GUI;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Events.VillageGUICloseEvent;
import com.domsplace.Villages.GUI.BukkitGUI.VillageGUIFrame;
import com.domsplace.Villages.GUI.BukkitGUI.VillageImage;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * @author      Dominic
 * @since       14/10/2013
 */
public class VillagesFrame extends VillageGUIFrame {
    private static final String VILLAGE_LOGO_REL = "images/VillagesLogo.png";
    private static final String VILLAGE_ICON_REL = "images/VillagesIcon.png";
    
    private static final int FRAME_WIDTH = 750;
    private static final int FRAME_HEIGHT = 600;
    
    //Instance
    private JPanel panel;
    
    private JLabel copyrightLabel;
    
    private VillageImage villageLogo;
    private VillageImage villageIcon;
    
    private VillagesTabbedPane villageTabbedPane;
    
    public VillagesFrame(VillagesGUIManager manager) {
        super(manager, "Villages");
        this.setBackground(Color.WHITE);
        
        //Build GUI
        this.panel = new JPanel(new BorderLayout(4,4));
        this.panel.setBackground(Color.WHITE);
        
        try {
            //Build Images
            this.villageLogo = new VillageImage(VILLAGE_LOGO_REL);
            this.villageIcon = new VillageImage(VILLAGE_ICON_REL);
        } catch(Exception e) {
            Base.error("Failed to setup gui!", e);
        }
        
        this.villageTabbedPane = new VillagesTabbedPane();
        this.copyrightLabel = new JLabel("2013 Dominic Masters - http://domsplace.com/");
        
        //Position
        this.villageLogo.setHorizontalAlignment(SwingConstants.CENTER);
        this.copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        //Add Items
        this.panel.add(this.villageLogo, BorderLayout.NORTH);
        this.panel.add(this.villageTabbedPane, BorderLayout.CENTER);
        this.panel.add(this.copyrightLabel, BorderLayout.SOUTH);
        
        this.setIconImage(this.villageIcon.getImage());
        
        this.add(this.panel);
        
        this.pack();
        this.setResizable(false);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setVisible(true);
    }
    
    public JPanel getMainPanel() {return this.panel;}
    public VillageImage getVillageLogo() {return this.villageLogo;}
    public VillagesTabbedPane getTabbedPane() {return this.villageTabbedPane;}
    
    @Override
    public void onClose() {
        VillageGUICloseEvent event = new VillageGUICloseEvent(this);
        event.fireEvent();
    }
}

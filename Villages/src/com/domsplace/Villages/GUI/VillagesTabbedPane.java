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

import com.domsplace.Villages.GUI.Frames.WelcomePanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author      Dominic
 * @since       15/10/2013
 */
public class VillagesTabbedPane extends JPanel {
    private JPanel cards;
    private JPanel tabs;
    
    private JButton welcomeButton;
    
    private WelcomePanel welcomePanel;
    
    public VillagesTabbedPane() {
        super(new BorderLayout(0,0));
        this.setBackground(Color.WHITE);
        
        this.cards = new JPanel(new CardLayout());
        this.tabs = new JPanel(new FlowLayout());
        
        //Change BG
        this.cards.setBackground(Color.WHITE);
        this.tabs.setBackground(Color.WHITE);
        
        this.welcomePanel = new WelcomePanel();
        
        this.cards.add(this.welcomePanel);
        
        this.tabs.add(new JButton("Button 1"));
        this.tabs.add(new JButton("Button 2"));
        
        this.add(this.tabs, BorderLayout.NORTH);
        this.add(this.cards, BorderLayout.CENTER);
    }
    
    public WelcomePanel getWelcomePanel() {return this.welcomePanel;}
}

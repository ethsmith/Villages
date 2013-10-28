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

package com.domsplace.Villages.GUI.BukkitGUI;

import com.domsplace.Villages.GUI.VillagesGUIManager;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author      Dominic
 * @since       14/10/2013
 */
public class VillageGUIFrame extends JFrame {
    private VillagesGUIManager manager;
    private WindowAdapter frameAdapter;
    
    private boolean forceClose;
    
    public VillageGUIFrame(VillagesGUIManager manager, String name) {
        super(name);
        this.forceClose = false;
        this.manager = manager;
        this.frameAdapter = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                ((VillageGUIFrame) evt.getSource()).fireClose(evt);
            }
        };
        this.addWindowListener(frameAdapter);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    
    public VillagesGUIManager getGUIManager() {return this.manager;}
    public WindowAdapter getFrameAdapter() {return this.frameAdapter;}
    
    public void close() {this.close(false);}
    
    public void close(boolean force) {
        this.forceClose = true;
        WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }
    
    //REMEMBER TO SUPER THIS
    public void fireClose(WindowEvent evt) {
        if(!forceClose) {
            int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want close the GUI?", "Close Villages GUI?",  JOptionPane.YES_NO_OPTION);
            if(!(reply == JOptionPane.YES_OPTION)) return;
        }
        
        this.onClose();
        
        setVisible(false);
        dispose();
        this.manager.close(false);
    }
    
    public void onClose(){}
}

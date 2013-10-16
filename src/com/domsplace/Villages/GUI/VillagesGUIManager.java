package com.domsplace.Villages.GUI;

import com.domsplace.Villages.Bases.Base;

public class VillagesGUIManager extends Base {
    private VillagesFrame frame;
    
    public VillagesGUIManager() {
        this.frame = new VillagesFrame(this);
    }
    
    public VillagesFrame getFrame() {return this.frame;}

    public void close() {this.close(true);}
    
    public void close(boolean frameInvoke) {
        if(frameInvoke) this.frame.close(true);
        Base.guiManager = null;
    }
}

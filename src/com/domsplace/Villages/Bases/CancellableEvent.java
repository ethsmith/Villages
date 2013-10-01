package com.domsplace.Villages.Bases;

import org.bukkit.event.Cancellable;

public class CancellableEvent extends EventBase implements Cancellable {
    private boolean isCancelled = false;
    
    public CancellableEvent() {
    }
    
    @Override
    public boolean isCancelled() {return isCancelled;}
    @Override
    public void setCancelled(boolean result) {this.isCancelled = result;}
}

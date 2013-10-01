package com.domsplace.Villages.Events;

import com.domsplace.Villages.Bases.CancellableEvent;
import java.util.List;
import org.bukkit.entity.Player;

public class PreCommandEvent extends CancellableEvent {
    
    private Player player;
    private String command;
    private List<String> args;
    
    public PreCommandEvent(Player player, String command, List<String> args) {
        this.player = player;
        this.command = command;
        this.args = args;
    }
    
    public Player getPlayer() {return this.player;}
    public String getCommand() {return this.command;}
    public List<String> getArgs() {return this.args;}
}

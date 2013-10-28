package com.domsplace.Villages.Events;

import com.domsplace.Villages.Bases.CancellableEvent;
import org.bukkit.entity.Player;

public class PvPEvent extends CancellableEvent {
    private Player attacked;
    private Player attacker;
    private double damage;
    
    public PvPEvent(Player attacked, Player attacker, double damage) {
        this.attacked = attacked;
        this.attacker = attacker;
        this.damage = damage;
    }
    
    public Player getAttackedPlayer() {
        return this.attacked;
    }
    
    public Player getAttacker() {
        return this.attacker;
    }
    
    public double getDamage() {
        return this.damage;
    }
}

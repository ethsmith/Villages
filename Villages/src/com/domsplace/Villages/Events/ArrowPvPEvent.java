package com.domsplace.Villages.Events;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

public class ArrowPvPEvent extends PvPEvent {
    private Arrow arrow;
    
    public ArrowPvPEvent(Player attacker, Player attacked, Arrow arrow, double damage) {
        super(attacker, attacked, damage);
        this.arrow = arrow;
    }
    
    public Arrow getArrow() {
        return this.arrow;
    }
}

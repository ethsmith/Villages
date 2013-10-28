package com.domsplace.Villages.Listeners;

import com.domsplace.Villages.Bases.VillageListener;
import com.domsplace.Villages.Objects.VillageScoreboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class VillageScoreboardListener extends VillageListener {
    @EventHandler
    public void handlePlayerLogin(PlayerLoginEvent e) {
        VillageScoreboard vsc = VillageScoreboard.getScoreboard(e.getPlayer());
        vsc.setPlayer();
    }
    
    @EventHandler
    public void handlePlayerLogout(PlayerQuitEvent e) {
        VillageScoreboard vsc = VillageScoreboard.getScoreboard(e.getPlayer());
        VillageScoreboard.deRegister(vsc);
    }
    
    @EventHandler
    public void handlePlayerKick(PlayerKickEvent e) {
        VillageScoreboard vsc = VillageScoreboard.getScoreboard(e.getPlayer());
        VillageScoreboard.deRegister(vsc);
    }
}

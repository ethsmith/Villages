package com.domsplace.Villages.Listeners;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.VillageListener;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.event.EventHandler;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

public class TagAPIListener extends VillageListener {
    @EventHandler
    public void handleVillageFoe(PlayerReceiveNameTagEvent e) {
        if(!useTagAPI) return;
        if(e.getPlayer() == null) return;
        if(e.getNamedPlayer() == null) return;
        
        if(e.getPlayer().equals(e.getNamedPlayer())) return;
        
        if(!inVillageWorld(e.getNamedPlayer())) return;
        if(!inVillageWorld(e.getPlayer())) return;
        
        Resident target = Resident.getResident(e.getPlayer());
        Resident named = Resident.getResident(e.getNamedPlayer());
        
        Village tVillage = Village.getPlayersVillage(target);
        Village nVillage = Village.getPlayersVillage(named);
        
        if(tVillage != null && nVillage != null && tVillage.equals(nVillage)) return;
        
        e.setTag(Base.EnemyColor + e.getNamedPlayer().getName());
    }
    
    @EventHandler
    public void handleVillageFriend(PlayerReceiveNameTagEvent e) {
        if(!useTagAPI) return;
        if(e.getPlayer() == null) return;
        if(e.getNamedPlayer() == null) return;
        
        if(e.getPlayer().equals(e.getNamedPlayer())) return;
        
        if(!inVillageWorld(e.getNamedPlayer())) return;
        if(!inVillageWorld(e.getPlayer())) return;
        
        Resident target = Resident.getResident(e.getPlayer());
        Resident named = Resident.getResident(e.getNamedPlayer());
        
        Village tVillage = Village.getPlayersVillage(target);
        Village nVillage = Village.getPlayersVillage(named);
        
        if(tVillage == null || nVillage == null) return;
        if(!tVillage.equals(nVillage)) return;
        
        e.setTag(Base.FriendColor + e.getNamedPlayer().getName());
    }
}

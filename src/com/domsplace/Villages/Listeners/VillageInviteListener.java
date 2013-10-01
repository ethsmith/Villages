package com.domsplace.Villages.Listeners;

import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.VillageListener;
import com.domsplace.Villages.Commands.SubCommands.VillageInvite;
import com.domsplace.Villages.Events.PreCommandEvent;
import com.domsplace.Villages.Events.ResidentAddedEvent;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import java.util.Map;
import org.bukkit.event.EventHandler;

public class VillageInviteListener extends VillageListener {
    @EventHandler(ignoreCancelled=true)
    public void handleVillageAccept(PreCommandEvent e) {
        if(!e.getCommand().equalsIgnoreCase("accept")) return;
        
        if(!hasPermission(e.getPlayer(), "Villages.accept")) return;
        Resident r = Resident.getResident(e.getPlayer());
        if(r == null) return;
        
        Map<Resident, Village> invites = VillageInvite.VILLAGE_INVITES;
        if(invites == null) return;
        
        if(!invites.containsKey(r)) return;
        Village v = invites.get(r);
        if(v == null) return;
        
        e.setCancelled(true);
        
        if(Village.getPlayersVillage(r) != null) {
            sendMessage(r, "alreadyinvillage");
            return;
        }
        
        invites.remove(r);
        
        ResidentAddedEvent event = new ResidentAddedEvent(r, v);
        event.fireEvent();
        if(event.isCancelled()) return;
        
        v.addResident(r);
        v.broadcast(gk("joinedvillage", r));
        DataManager.saveAll();
    }
    
    @EventHandler(ignoreCancelled=true)
    public void handleVillageDeny(PreCommandEvent e) {
        if(!e.getCommand().equalsIgnoreCase("deny")) return;
        
        if(!hasPermission(e.getPlayer(), "Villages.deny")) return;
        Resident r = Resident.getResident(e.getPlayer());
        if(r == null) return;
        
        Map<Resident, Village> invites = VillageInvite.VILLAGE_INVITES;
        if(invites == null) return;
        
        if(!invites.containsKey(r)) return;
        Village v = invites.get(r);
        if(v == null) return;
        
        invites.remove(r);
        sk(e.getPlayer(), "deniedinvite");
        e.setCancelled(true);
    }
}

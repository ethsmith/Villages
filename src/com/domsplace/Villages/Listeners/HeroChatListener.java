package com.domsplace.Villages.Listeners;

import com.domsplace.Villages.Bases.VillageListener;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import com.dthielke.herochat.ChannelChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HeroChatListener extends VillageListener {
    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void handleVillageHeroChat(ChannelChatEvent e) {
        if(!inVillageWorld(e.getSender().getPlayer())) return;
        
        String village = Wilderness;
        
        Village vil = Village.getPlayersVillage(Resident.getResident(e.getSender().getPlayer()));
        if(vil != null) {
            village = vil.getName();
        }
        
        if(village.equals("")) return;
        
        e.setFormat(e.getFormat().replace("{village}", village));
    }
}

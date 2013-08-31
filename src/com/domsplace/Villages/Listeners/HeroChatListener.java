package com.domsplace.Villages.Listeners;

import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Utils.VillageUtils;
import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.ListenerBase;
import com.dthielke.herochat.ChannelChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class HeroChatListener extends ListenerBase {
    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onHeroChat(ChannelChatEvent e) {
        if(!VillageUtils.isVillageWorld(e.getSender().getPlayer().getWorld())) {
            return;
        }
        
        String village = Base.WildernessPrefix;
        
        Village vil = VillageUtils.getPlayerVillage(e.getSender().getPlayer());
        if(vil != null) {
            village = vil.getName();
        }
        
        if(village.equals("")) {
            return;
        }
        
        e.setFormat(e.getFormat().replace("{village}", village));
    }
}

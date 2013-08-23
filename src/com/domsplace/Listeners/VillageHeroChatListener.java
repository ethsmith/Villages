package com.domsplace.Listeners;

import com.domsplace.Objects.Village;
import com.domsplace.Utils.VillageVillagesUtils;
import com.domsplace.VillageBase;
import com.domsplace.VillagesPlugin;
import com.dthielke.herochat.ChannelChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class VillageHeroChatListener extends VillageBase implements Listener {

    private VillagesPlugin plugin;
    
    public VillageHeroChatListener(VillagesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onHeroChat(ChannelChatEvent e) {
        if(!VillageVillagesUtils.isVillageWorld(e.getSender().getPlayer().getWorld())) {
            return;
        }
        
        String village = VillageBase.WildernessPrefix;
        
        Village vil = VillageVillagesUtils.getPlayerVillage(e.getSender().getPlayer());
        if(vil != null) {
            village = vil.getName();
        }
        
        if(village.equals("")) {
            return;
        }
        
        e.setFormat(e.getFormat().replace("{village}", village));
    }
    
}

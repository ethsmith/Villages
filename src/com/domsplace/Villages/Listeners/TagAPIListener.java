package com.domsplace.Villages.Listeners;

import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Utils.VillageUtils;
import static com.domsplace.Villages.Bases.Base.EnemyColor;
import static com.domsplace.Villages.Bases.Base.VillageColor;
import com.domsplace.Villages.Bases.ListenerBase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

public class TagAPIListener extends ListenerBase {
    @EventHandler
    public void onNameTagChange(PlayerReceiveNameTagEvent e) {
        if(!getConfigManager().useTagAPI) {
            return;
        }
        
        Player p = e.getPlayer();
        Player r = e.getNamedPlayer();
        
        if(p == r) {
            return;
        }
        
        if(p == null || r == null) {
            return;
        }
        
        if(!VillageUtils.isVillageWorld(p.getWorld())) {
            return;
        }
        if(!VillageUtils.isVillageWorld(r.getWorld())) {
            return;
        }
        
        Village v = VillageUtils.getPlayerVillage(p);
        if(v == null) {
            e.setTag(EnemyColor + r.getName());
        }
        
        if(r == null) {
            return;
        }
        
        boolean isRes = false;
        try {
            isRes = v.isResident(r);
        } catch(Exception ex) {
            isRes = false;
        }
        
        if(isRes) {
            e.setTag(VillageColor + r.getName());
            return;
        }
        e.setTag(EnemyColor + r.getName());
    }
}

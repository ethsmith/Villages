package com.domsplace.Listeners;

import com.domsplace.Objects.Village;
import com.domsplace.Utils.VillageUtils;
import com.domsplace.Utils.VillageVillagesUtils;
import com.domsplace.VillageBase;
import static com.domsplace.VillageBase.EnemyColor;
import static com.domsplace.VillageBase.VillageColor;
import com.domsplace.VillagesPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

public class VillageTeamListener extends VillageBase implements Listener {
    
    private VillagesPlugin plugin;
    
    public VillageTeamListener(VillagesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onNameTagChange(PlayerReceiveNameTagEvent e) {
        
        if(!VillageUtils.useTagAPI) {
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
        
        if(!VillageVillagesUtils.isVillageWorld(p.getWorld())) {
            return;
        }
        if(!VillageVillagesUtils.isVillageWorld(r.getWorld())) {
            return;
        }
        
        Village v = VillageVillagesUtils.getPlayerVillage(p);
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

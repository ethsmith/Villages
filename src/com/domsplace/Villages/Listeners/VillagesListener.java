package com.domsplace.Villages.Listeners;

import com.domsplace.Villages.Events.GriefEvent;
import com.domsplace.Villages.Enums.GriefType;
import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Utils.VillageSQLUtils;
import com.domsplace.Villages.Utils.VillageScoreboardUtils;

import com.domsplace.Villages.Utils.VillageUtils;
import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.ListenerBase;
import java.util.ArrayList;
import org.bukkit.Chunk;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class VillagesListener extends ListenerBase {
    public static ArrayList<Player> sentWilderness = new ArrayList<Player>();
    
    public static boolean PVPWilderness = true;
    public static boolean PVPEnemyVillage = true;
    public static boolean PVPSameVillage = false;
    
    @EventHandler(ignoreCancelled=true)
    public void onPlayerLogin(PlayerLoginEvent e) {
        if(getConfigManager().useSQL) {
            VillageSQLUtils.recordSQLPlayer(e.getPlayer());
        }
        VillageScoreboardUtils.SetupScoreboard();
    }
    
    @EventHandler
    public void onPlayerKicked(PlayerKickEvent e) {
        onPlayerLeftServer(e.getPlayer());
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        onPlayerLeftServer(e.getPlayer());
    }
    
    public void onPlayerLeftServer(Player p) {
        for(Village v : VillageUtils.getVillages()) {
            v.sentWelcome.remove(p);
        }
        VillageScoreboardUtils.SetupScoreboard();
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onPlayerMove(PlayerMoveEvent e) {
        if(e.getTo() == null) {
            return;
        }
        
        if(e.getPlayer() == null) {
            return;
        }
        
        Player p = e.getPlayer();
        if(!VillageUtils.isVillageWorld(p.getWorld())) {
            return;
        }
        
        if(p.getLocation().getChunk() == null) {
            return;
        }
        
        Village village = VillageUtils.getVillageFromChunk(p.getLocation().getChunk());
        if(village == null) {
            for(Village v : VillageUtils.getVillages()) {
                if(v == null) {
                    continue;
                }
                v.sentWelcome.remove(p);
            }
            if(getConfig().getBoolean("messages.leavevillage") && !sentWilderness.contains(p)) {
                msgPlayer(p, gK("enterwilderness"));
                sentWilderness.add(p);
            }
            return;
        }
        
        if(village.sentWelcome.contains(p)) {
            return;
        }
        
        if(!getConfig().getBoolean("messages.entervillage")) {
            return;
        }
        
        msgPlayer(p, ChatDefault + village.getDescription());
        village.sentWelcome.add(p);
        sentWilderness.remove(p);
    }
    
    @EventHandler
    public void griefInVillage(GriefEvent e) {
        if(e.getType().equals(GriefType.INTERACT) && e.getPlayer().hasPermission("Villages.interact")) return;
        if(e.getPlayer().hasPermission("Villages.villageadmin")) return;
        if(e.getBlock() == null) return;
        if(!VillageUtils.isVillageWorld(e.getBlock().getWorld())) return;
        
        Village lv = VillageUtils.getVillageFromChunk(e.getBlock().getChunk());
        if(lv == null) {
            if(getConfig().getBoolean("protection.griefwild")) return;
            
            e.setCancelled(true);
            msgPlayer(e.getPlayer(), gK("nointeract"));
            return;
        }
        if(getConfig().getBoolean("protection.griefvillage")) return;
        if(lv.isResident(e.getPlayer())) return;
        e.setCancelled(true);
        msgPlayer(e.getPlayer(), gK("nointeract"));
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if(!VillageUtils.isVillageWorld(p.getWorld())) return;
        
        Village v = VillageUtils.getPlayerVillage(p);
        if(v == null) {
            if(Base.WildernessPrefix.equals("")) return;
            String r = PlayerChatPrefix.replaceAll("%v%", Base.WildernessPrefix) + e.getFormat();
            e.setFormat(r);
            return;
        }
        
        String r = PlayerChatPrefix.replaceAll("%v%", v.getName()) + e.getFormat();
        e.setFormat(r);
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onPVP(EntityDamageByEntityEvent e) {
        if(e.getDamager() == null || e.getEntity() == null) return;
        
        if(e.getDamager().getType() == null || e.getEntity().getType() == null) return;
        
        if(e.getDamager().getType() != EntityType.PLAYER || e.getEntity().getType() != EntityType.PLAYER) {
            return;
        }
        
        if(!VillageUtils.isVillageWorld(e.getDamager().getWorld())) return;
        
        Player killer = (Player) e.getDamager();
        Player attacked = (Player) e.getEntity();
        
        Village killerVillage = VillageUtils.getPlayerVillage(killer);
        Village attackedVillage = VillageUtils.getPlayerVillage(attacked);
        
        boolean killerInVillage = true;
        boolean attackedInVillage = true;
        
        if(killerVillage == null) killerInVillage = false;
        
        if(attackedVillage == null) attackedInVillage = false;
        
        if(!killerInVillage || !attackedInVillage) {
            if(PVPWilderness) return;
            e.setCancelled(true);
            msgPlayer(killer, gK("cantattackwilderness"));
            return;
        }
        
        boolean isSameVillage = false;
        if(attackedVillage == killerVillage) {
            isSameVillage = true;
        }
        
        if(!isSameVillage) {
            if(PVPEnemyVillage) {
                return;
            }
            
            e.setCancelled(true);
            msgPlayer(killer, gK("cantattackdifferentvillage"));
            return;
        }
        
        if(PVPSameVillage) {
            return;
        }
        
        e.setCancelled(true);
        msgPlayer(killer, gK("cantattacksamevillage"));
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onGrief(GriefEvent e) {
        if(!UsePlots) {
            return;
        }
        
        if(e.getPlayer().hasPermission("Villages.villageadmin")) {
            return;
        }
        
        if(e.getType().equals(GriefType.INTERACT) && e.getPlayer().hasPermission("Villages.interact")) {
            return;
        }
        
        if(!VillageUtils.isVillageWorld(e.getPlayer().getWorld())) {
            return;
        }
        
        Chunk c = e.getBlock().getChunk();
        
        Village v = VillageUtils.getVillageFromChunk(c);
        if(v == null) {
            return;
        }
        
        if(v.isMayor(e.getPlayer())) {
            return;
        }
        
        if(v.isChunkClaimed(c)) {
            if(v.isChunkOwnedByPlayer(e.getPlayer(), c)) {
                return;
            }
            
            msgPlayer(e.getPlayer(), gK("chunkclaimedbyplayer"));
            e.setCancelled(true);
            return;
        }
        
        e.setCancelled(true);
        msgPlayer(e.getPlayer(), gK("chunknotowned"));
    }
}

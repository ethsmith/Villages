package com.domsplace.Listeners;

import com.domsplace.DataManagers.VillageConfigManager;
import com.domsplace.Objects.Village;
import com.domsplace.Utils.VillageSQLUtils;
import com.domsplace.Utils.VillageScoreboardUtils;
import com.domsplace.Utils.VillageUtils;
import com.domsplace.Utils.VillageVillagesUtils;
import com.domsplace.VillageBase;
import static com.domsplace.VillageBase.PlayerChatPrefix;
import static com.domsplace.VillageBase.gK;
import com.domsplace.VillagesPlugin;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

public class VillageVillagesListener extends VillageBase implements Listener {
    
    private VillagesPlugin plugin;
    
    public BukkitTask AutoSaveVillages;
    public static ArrayList<Player> sentWilderness;
    
    public static boolean PVPWilderness = true;
    public static boolean PVPEnemyVillage = true;
    public static boolean PVPSameVillage = false;
    
    public VillageVillagesListener(VillagesPlugin plugin) {
        this.plugin = plugin;
        
        sentWilderness = new ArrayList<Player>();
        
        AutoSaveVillages = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            public void run() {
                VillageVillagesUtils.SaveAllVillages();
            }
        }, 60L, 6000L);
    }
    
    
    @EventHandler(ignoreCancelled=true)
    public void onPlayerLogin(PlayerLoginEvent e) {
        if(VillageUtils.useSQL) {
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
        for(Village v : VillageVillagesUtils.Villages) {
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
        
        if(p.getLocation().getChunk() == null) {
            return;
        }
        
        Village village = VillageVillagesUtils.getVillageFromChunk(p.getLocation().getChunk());
        if(village == null) {
            for(Village v : VillageVillagesUtils.Villages) {
                if(v == null) {
                    continue;
                }
                v.sentWelcome.remove(p);
            }
            if(VillageConfigManager.config.getBoolean("messages.leavevillage") && !sentWilderness.contains(p)) {
                VillageUtils.msgPlayer(p, gK("enterwilderness"));
                sentWilderness.add(p);
            }
            return;
        }
        
        if(village.sentWelcome.contains(p)) {
            return;
        }
        
        if(!VillageConfigManager.config.getBoolean("messages.entervillage")) {
            return;
        }
        
        VillageUtils.msgPlayer(p, ChatDefault + village.getDescription());
        village.sentWelcome.add(p);
        sentWilderness.remove(p);
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getPlayer().hasPermission("Villages.villageadmin")) {
            return;
        }
        
        if(e.getClickedBlock() == null) {
            return;
        }
        
        Village lv = VillageVillagesUtils.getVillageFromChunk(e.getClickedBlock().getChunk());
        if(lv == null) {
            //Wilderness//
            if(VillageConfigManager.config.getBoolean("protection.griefwild")) {
                return;
            }
            e.setCancelled(true);
            VillageUtils.msgPlayer(e.getPlayer(), gK("nointeract"));
            return;
        }
        if(VillageConfigManager.config.getBoolean("protection.griefvillage")) {
            return;
        }
        if(lv.isResident(e.getPlayer())) {
            return;
        }
        e.setCancelled(true);
        VillageUtils.msgPlayer(e.getPlayer(), gK("nointeract"));
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if(e.getPlayer().hasPermission("Villages.villageadmin")) {
            return;
        }
        
        if(e.getBlock() == null) {
            return;
        }
        
        Village lv = VillageVillagesUtils.getVillageFromChunk(e.getBlock().getChunk());
        if(lv == null) {
            //Wilderness//
            if(VillageConfigManager.config.getBoolean("protection.griefwild")) {
                return;
            }
            e.setCancelled(true);
            VillageUtils.msgPlayer(e.getPlayer(), gK("nointeract"));
            return;
        }
        if(VillageConfigManager.config.getBoolean("protection.griefvillage")) {
            return;
        }
        if(lv.isResident(e.getPlayer())) {
            return;
        }
        e.setCancelled(true);
        VillageUtils.msgPlayer(e.getPlayer(), gK("nointeract"));
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        Village v = VillageVillagesUtils.getPlayerVillage(p);
        if(v == null) {
            return;
        }
        
        String r = PlayerChatPrefix.replaceAll("%v%", v.getName()) + e.getFormat();
        e.setFormat(r);
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onPVP(EntityDamageByEntityEvent e) {
        if(e.getDamager() == null || e.getEntity() == null) {
            return;
        }
        
        if(e.getDamager().getType() == null || e.getEntity().getType() == null) {
            return;
        }
        
        if(e.getDamager().getType() != EntityType.PLAYER || e.getEntity().getType() != EntityType.PLAYER) {
            return;
        }
        
        Player killer = (Player) e.getDamager();
        Player attacked = (Player) e.getEntity();
        
        Village killerVillage = VillageVillagesUtils.getPlayerVillage(killer);
        Village attackedVillage = VillageVillagesUtils.getPlayerVillage(attacked);
        
        boolean killerInVillage = true;
        boolean attackedInVillage = true;
        
        if(killerVillage == null) {
            killerInVillage = false;
        }
        
        if(attackedVillage == null) {
            attackedInVillage = false;
        }
        
        if(!killerInVillage || !attackedInVillage) {
            if(PVPWilderness) {
                return;
            }
            
            e.setCancelled(true);
            VillageUtils.msgPlayer(killer, gK("cantattackwilderness"));
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
            VillageUtils.msgPlayer(killer, gK("cantattackdifferentvillage"));
            return;
        }
        
        if(PVPSameVillage) {
            return;
        }
        
        e.setCancelled(true);
        VillageUtils.msgPlayer(killer, gK("cantattacksamevillage"));
        
    }
}

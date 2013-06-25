package com.domsplace.Listeners;

import com.domsplace.DataManagers.VillageConfigManager;
import com.domsplace.Objects.Village;
import com.domsplace.Utils.VillageSQLUtils;
import com.domsplace.Utils.VillageUtils;
import com.domsplace.Utils.VillageVillagesUtils;
import com.domsplace.VillageBase;
import com.domsplace.VillagesPlugin;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
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
}

package com.minecraft.softegg;

import static com.minecraft.softegg.VillageBase.ChatError;
import static com.minecraft.softegg.VillageBase.SendMessage;
import static com.minecraft.softegg.VillageBase.gK;
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

public class VillageListener extends VillageBase implements Listener {
    private Villages plugin;
    
    public BukkitTask AutoConfigSave;
    public BukkitTask AutoTownSave;
    
    public VillageListener(final Villages plugin) {
        this.plugin = plugin;
        
        AutoConfigSave = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            public void run() {
                plugin.dataManager.saveConfig();
                VillageUtils.SaveAllVillages();
            }
        }, 60L, 30000L);
        
        AutoTownSave = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            public void run() {
                VillageUtils.SaveAllVillages();
            }
        }, 60L, 6000L);
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onPlayerLogin(PlayerLoginEvent e) {
        if(VillageDataManager.useSQL()) {
            VillageDataManager.dataManager.recordSQLPlayer(e.getPlayer());
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
        for(Village v : VillageUtils.villages) {
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
        
        Village village = VillageUtils.getVillageFromChunk(p.getLocation().getChunk());
        if(village == null) {
            for(Village v : VillageUtils.villages) {
                if(v == null) {
                    continue;
                }
                v.sentWelcome.remove(p);
            }
            if(VillageDataManager.config.getBoolean("messages.leavevillage") && !VillageUtils.sentWilderness.contains(p)) {
                SendMessage(p, gK("enterwilderness"));
                VillageUtils.sentWilderness.add(p);
            }
            return;
        }
        
        if(village.sentWelcome.contains(p)) {
            return;
        }
        
        if(!VillageDataManager.config.getBoolean("messages.entervillage")) {
            return;
        }
        
        SendMessage(p, ChatDefault + village.getDescription());
        village.sentWelcome.add(p);
        VillageUtils.sentWilderness.remove(p);
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getPlayer().hasPermission("Villages.villageadmin")) {
            return;
        }
        
        if(e.getClickedBlock() == null) {
            return;
        }
        
        Village lv = VillageUtils.getVillageFromChunk(e.getClickedBlock().getChunk());
        if(lv == null) {
            //Wilderness//
            if(VillageDataManager.config.getBoolean("protection.griefwild")) {
                return;
            }
            e.setCancelled(true);
            SendMessage(e.getPlayer(), gK("nointeract"));
            return;
        }
        if(VillageDataManager.config.getBoolean("protection.griefvillage")) {
            return;
        }
        if(lv.isResident(e.getPlayer())) {
            return;
        }
        e.setCancelled(true);
        SendMessage(e.getPlayer(), gK("nointeract"));
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if(e.getPlayer().hasPermission("Villages.villageadmin")) {
            return;
        }
        
        if(e.getBlock() == null) {
            return;
        }
        
        Village lv = VillageUtils.getVillageFromChunk(e.getBlock().getChunk());
        if(lv == null) {
            //Wilderness//
            if(VillageDataManager.config.getBoolean("protection.griefwild")) {
                return;
            }
            e.setCancelled(true);
            SendMessage(e.getPlayer(), gK("nointeract"));
            return;
        }
        if(VillageDataManager.config.getBoolean("protection.griefvillage")) {
            return;
        }
        if(lv.isResident(e.getPlayer())) {
            return;
        }
        e.setCancelled(true);
        SendMessage(e.getPlayer(), gK("nointeract"));
    }
}

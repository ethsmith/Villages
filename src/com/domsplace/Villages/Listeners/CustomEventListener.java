package com.domsplace.Villages.Listeners;

import com.domsplace.Villages.Events.GriefEvent;
import com.domsplace.Villages.Events.MayorDeathEvent;
import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Enums.GriefType;
import com.domsplace.Villages.Utils.VillageUtils;
import com.domsplace.Villages.Bases.ListenerBase;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class CustomEventListener extends ListenerBase {
    @EventHandler(ignoreCancelled=true)
    public void onBlockBreak(BlockBreakEvent e) {
        List<Block> blocks = new ArrayList<Block>();
        GriefEvent event = new GriefEvent(e.getPlayer(), e.getBlock(), blocks, GriefType.BREAK);
        Bukkit.getServer().getPluginManager().callEvent(event);
        e.setCancelled(event.isCancelled());
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onBlockPlace(BlockPlaceEvent e) {
        List<Block> blocks = new ArrayList<Block>();
        GriefEvent event = new GriefEvent(e.getPlayer(), e.getBlock(), blocks, GriefType.PLACE);
        Bukkit.getServer().getPluginManager().callEvent(event);
        e.setCancelled(event.isCancelled());
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onPlayerInteract(PlayerInteractEvent e) {
        List<Block> blocks = new ArrayList<Block>();
        GriefEvent event = new GriefEvent(e.getPlayer(), e.getClickedBlock(), blocks, GriefType.INTERACT);
        Bukkit.getServer().getPluginManager().callEvent(event);
        e.setCancelled(event.isCancelled());
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onBlockDamage(BlockDamageEvent e) {
        List<Block> blocks = new ArrayList<Block>();
        GriefEvent event = new GriefEvent(e.getPlayer(), e.getBlock(), blocks, GriefType.BLOCK_DAMAGE);
        Bukkit.getServer().getPluginManager().callEvent(event);
        e.setCancelled(event.isCancelled());
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Village v = VillageUtils.getPlayerVillage(e.getEntity());
        if(v == null) {
            return;
        }
        
        if(!v.isMayor(e.getEntity())) {
            return;
        }
        
        MayorDeathEvent event = new MayorDeathEvent(e.getEntity(), v);
        Bukkit.getServer().getPluginManager().callEvent(event);
    }
}

package com.domsplace.Listeners;

import com.domsplace.Events.VillageGriefEvent;
import com.domsplace.Objects.GriefType;
import com.domsplace.VillageBase;
import com.domsplace.VillagesPlugin;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class VillageCustomEventListener extends VillageBase implements Listener {
    
    private VillagesPlugin plugin;
    
    public VillageCustomEventListener(VillagesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onBlockBreak(BlockBreakEvent e) {
        List<Block> blocks = new ArrayList<Block>();
        VillageGriefEvent event = new VillageGriefEvent(e.getPlayer(), e.getBlock(), blocks, GriefType.BREAK);
        Bukkit.getServer().getPluginManager().callEvent(event);
        e.setCancelled(event.isCancelled());
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onBlockPlace(BlockPlaceEvent e) {
        List<Block> blocks = new ArrayList<Block>();
        VillageGriefEvent event = new VillageGriefEvent(e.getPlayer(), e.getBlock(), blocks, GriefType.PLACE);
        Bukkit.getServer().getPluginManager().callEvent(event);
        e.setCancelled(event.isCancelled());
    }
    
    @EventHandler(ignoreCancelled=true)
    public void onPlayerInteract(PlayerInteractEvent e) {
        List<Block> blocks = new ArrayList<Block>();
        VillageGriefEvent event = new VillageGriefEvent(e.getPlayer(), e.getClickedBlock(), blocks, GriefType.INTERACT);
        Bukkit.getServer().getPluginManager().callEvent(event);
        e.setCancelled(event.isCancelled());
    }
}

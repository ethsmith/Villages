package com.domsplace.Villages.Utils;

import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Bases.UtilsBase;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class VillageUtils extends UtilsBase {
    public static int borderRadius = 1;
    public static ArrayList<Village> Villages;
    public static Map<Player, Village> townInvites = new HashMap<Player, Village>();
    
    public static boolean isChunkInATownsArea(Chunk chunk) {
        for(Village v : Villages) {
            if(!v.isChunkInTownArea(chunk)) {
                continue;
            }
            return true;
        }
        
        return false;
    }
    
    public static boolean isChunkInATownsArea(Chunk chunk, Village ignored) {
        for(Village v : Villages) {
            if(v == ignored) {
                continue;
            }
            
            if(!v.isChunkInTownArea(chunk)) {
                continue;
            }
            return true;
        }
        
        return false;
    }

    public static Village getVillageFromChunk(Chunk chunk) {
        for(Village v : Villages) {
            if(v == null) {
                continue;
            }
            
            if(!v.isChunkInTownArea(chunk)) {
                continue;
            }
            return v;
        }
        return null;
    }
    
    public static List<World> getVillageWorlds() {
        List<World> worlds = new ArrayList<World>();
        for(String w : getConfig().getStringList("Worlds")) {
            World world = Bukkit.getWorld(w);
            if(world == null) {
                continue;
            }
            worlds.add(world);
        }
        
        return worlds;
    }
    
    public static boolean isVillageWorld(World world) {
        if(!getVillageWorlds().contains(world)) {
            return false;
        }
        return true;
    }
    
    public static Village getPlayerVillage(Player player) {
        return getPlayerVillage(Bukkit.getOfflinePlayer(player.getName()));
    }
    
    public static Village getPlayerVillage(OfflinePlayer player) {
        for(Village v : Villages) {
            if(v.isResident(player) || v.isMayor(player)) {
                return v;
            }
        }
        return null;
    }

    public static Village getVillage(String string) {
        for(Village v : Villages) {
            if(!v.getName().toLowerCase().startsWith(string.toLowerCase())) {
                continue;
            }
            return v;
        }
        return null;
    }

    public static Village getVillageExact(String string) {
        for(Village v : Villages) {
            if(!v.getName().toLowerCase().equals(string.toLowerCase())) {
                continue;
            }
            return v;
        }
        return null;
    }
    
    public static boolean doVillagesOverlap(Village v) {
        for(Chunk c : v.getTownArea()) {
            if(!isChunkInATownsArea(c, v)) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    public static double CreateVillageCost() {
        return getConfig().getDouble("cost.createvillage");
    }

    public static List<Inventory> getVillageInventories() {
        List<Inventory> invs = new ArrayList<Inventory>();
        
        for(Village v : getVillages()) {
            invs.add(v.getItemBank().getInventory());
        }
        
        return invs;
    }

    static List<OfflinePlayer> getAllVillagesPlayers() {
        List<OfflinePlayer> players = new ArrayList<OfflinePlayer>();
        
        for(Village v : Villages) {
            for(OfflinePlayer p : v.getResidents()) {
                players.add(p);
            }
        }
        
        return players;
    }
    
    public static boolean isCoordBetweenCoords(Chunk c, ProtectedRegion r) {
        Block b1 = c.getBlock(0, 0, 0);
        Block b2 = c.getBlock(15, 0, 15);
        
        boolean s1 = isCoordBetweenCoords(b1.getX(), b1.getZ(), r.getMinimumPoint(), r.getMaximumPoint());
        if(s1) return true;
        boolean s2 = isCoordBetweenCoords(b2.getX(), b2.getZ(), r.getMinimumPoint(), r.getMaximumPoint());
        if(s2) return true;
        boolean s3 = isCoordBetweenCoords(r.getMinimumPoint(), b1, b2);
        if(s3) return true;
        boolean s4 = isCoordBetweenCoords(r.getMaximumPoint(), b1, b2);
        if(s4) return true;
        
        return false;
    }
    
    public static boolean isCoordBetweenCoords(int checkX, int checkZ, BlockVector min, BlockVector max) {
        return isCoordBetweenCoords(checkX, checkZ, min.getBlockX(), min.getBlockZ(), max.getBlockX(), max.getBlockZ());
    }
    
    public static boolean isCoordBetweenCoords(BlockVector bv, int outerX, int outerZ, int maxX, int maxZ) {
        return isCoordBetweenCoords(bv.getBlockX(), bv.getBlockZ(), outerX, outerZ, maxX, maxZ);
    }
    
    public static boolean isCoordBetweenCoords(BlockVector bv, Block b1, Block b2) {
        return isCoordBetweenCoords(bv.getBlockX(), bv.getBlockZ(), b1.getX(), b1.getZ(), b2.getX(), b2.getZ());
    }
    
    public static boolean isCoordBetweenCoords(int checkX, int checkZ, int outerX, int outerZ, int maxX, int maxZ) {
        if(checkX >= Math.min(outerX, maxX) && checkX <= Math.max(outerX, maxX)) {
            if(checkZ >= Math.min(outerZ, maxZ) && checkZ <= Math.max(outerZ, maxZ)) { return true; }
        }
        return false;
    }
}

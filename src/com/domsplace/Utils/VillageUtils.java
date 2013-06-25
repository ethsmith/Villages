package com.domsplace.Utils;

import com.domsplace.DataManagers.VillagePluginManager;
import com.domsplace.VillageBase;
import com.domsplace.VillagesPlugin;
import java.util.Date;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VillageUtils extends VillageBase {
    public static VillagesPlugin plugin;
    
    public static Boolean useSQL = false;
    public static Boolean useEconomy = false;
    
    public static void broadcast(String message) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            msgPlayer(p, message);
        }
        msgConsole(message);
    }
    
    public static void broadcast(String permission, String message) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!p.hasPermission(permission)) {
                continue;
            }
            
            msgPlayer(p, message);
        }
        msgConsole(message);
    }
    
    public static void broadcast(String permission, List<String> messages) {
        for(String s : messages) {
            broadcast(s);
        }
    }
    public static void broadcast(String permission, String[] messages) {
        for(String s : messages) {
            broadcast(s);
        }
    }
    
    public static void msgConsole(String message) {
        msgPlayer(Bukkit.getConsoleSender(), message);
    }
    
    public static void msgConsole(List<String> messages) {
        for(String message : messages) {
            msgPlayer(Bukkit.getConsoleSender(), message);
        }
    }
    
    public static void msgConsole(String[] messages) {
        for(String message : messages) {
            msgPlayer(Bukkit.getConsoleSender(), message);
        }
    }
    
    public static void msgPlayer(Player player, String message) {
        if(!VillageVillagesUtils.isVillageWorld(player.getLocation().getWorld())) {
            return;
        }
        msgPlayer((CommandSender) player, message);
    }
    
    public static void msgPlayer(CommandSender player, String message) {
        player.sendMessage(ChatPrefix + ChatDefault + message);
    }
    
    public static void msgPlayer(CommandSender player, List<String> messages) {
        for(String s : messages) {
            msgPlayer(player, s);
        }
    }
    
    public static void msgPlayer(CommandSender player, String[] messages) {
        for(String s : messages) {
            msgPlayer(player, s);
        }
    }
    
    public static void Error(String reason, String cause) {
        msgConsole("§fError! §c" + reason + " Caused by " + cause);
    }
    
    public static String ColorString(String msg) {
        String[] andCodes = {"&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9", "&a", "&b", "&c", "&d", "&e", "&f", "&l", "&o", "&n", "&m", "&k", "&r"};
        String[] altCodes = {"§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f", "§l", "§o", "§n", "§m", "§k", "§r"};
        
        for(int x = 0; x < andCodes.length; x++) {
            msg = msg.replaceAll(andCodes[x], altCodes[x]);
        }
        
        return msg;
    }
    
    public static String getStringLocation (Location location) {
        return location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + " " + location.getWorld().getName();
    }
    
    public static String getStringLocation (Chunk chunk) {
        return chunk.getX() + ", " + chunk.getZ() + " : " + chunk.getWorld().getName();
    }
    
    public static String[] getCommandDescription(String name) {
        
        String[] result = {
            ChatImportant + "Usage: " + ChatDefault + VillagePluginManager.PluginYML.getString("commands." + name + ".usage").replaceAll("<command>", name),
            ChatImportant + "Description: " + ChatDefault + VillagePluginManager.PluginYML.getString("commands." + name + ".description")
        };
        
        return result;
    }
    
    public static boolean canSee(CommandSender player, CommandSender player0) {
        if(!(player instanceof Player)) {
            return true;
        }
        if(!(player0 instanceof Player)) {
            return true;
        }
        
        
        
        if(((Player) player).canSee((Player) player0)) {
            return true;
        }
        return false;
    }
    
    public static long getNow() {
        Date someTime = new Date();
        long currentMS = someTime.getTime();
        return currentMS;
    }

    public static Player getPlayer(CommandSender cs, String string) {
        Player p = Bukkit.getPlayer(string);
        if(p == null) {
            return null;
        }
        
        if(!canSee(cs, p)) {
            p = Bukkit.getPlayerExact(string);
            if(p == null) {
                return null;
            }
        }
        return p;
    }
}

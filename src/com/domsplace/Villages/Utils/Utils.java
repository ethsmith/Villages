package com.domsplace.Villages.Utils;

import com.domsplace.Villages.Bases.DataManagerBase;
import com.domsplace.Villages.DataManagers.PluginManager;
import com.domsplace.Villages.Bases.UtilsBase;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Utils extends UtilsBase {
    public static boolean useSQL = false;
    public static boolean useEconomy = false;
    public static boolean useTagAPI = false;
    public static boolean useWorldGuard = false;
    public static boolean useDynmap = false;
    public static boolean useHerochat = false;
    
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
    
    public static void msgPlayer(OfflinePlayer player, String message) {
        if(!player.isOnline()) {
            return;
        }
        msgPlayer(player.getPlayer(), message);
    }
    
    public static void msgPlayer(Player player, String message) {
        if(!VillageUtils.isVillageWorld(player.getLocation().getWorld())) {
            return;
        }
        msgPlayer((CommandSender) player, message);
    }
    
    public static void msgPlayer(CommandSender player, String message) {
        if(message == "") {
            return;
        }
        
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
    
    public static void Error(String reason, Exception cause) {
        if(!DEBUG_MODE) {
            msgConsole("§fError! §c" + reason);
            return;
        }
        msgConsole("§fError! §c" + reason + " Caused by: ");
        
        
        String result = "\r\nUknown Error.";
        String message = "No Message.";
        
        if(cause != null) {
            StackTraceElement[] elements = cause.getStackTrace();
            
            result = "\r\n§dStack Trace:\r\n§4";
            
            for(StackTraceElement el : elements) {
                result += "" + el.getLineNumber();
                result += " : " + el.getMethodName();
                result += " : " + el.getClassName();
                result += "\r\n";
            }
            
            result += "";
            
            message = cause.getLocalizedMessage();
        }
        
        msgConsole("§7" + result + "§eException: " + message + "\r\n");
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
            ChatImportant + "Usage: " + ChatDefault + DataManagerBase.PLUGIN_MANAGER.getString("commands." + name + ".usage").replaceAll("<command>", name),
            ChatImportant + "Description: " + ChatDefault + DataManagerBase.PLUGIN_MANAGER.getString("commands." + name + ".description")
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
    
    public static OfflinePlayer getOfflinePlayer(CommandSender cs, String string) {
        OfflinePlayer p = getPlayer(cs, string);
        if(p == null) {
            p = Bukkit.getOfflinePlayer(string);
        }
        return p;
    }

    public static List<ItemStack> GetItemFromString(List<String> stringList) {
        List<ItemStack> items = new ArrayList<ItemStack>();
        
        for(String item : stringList) {
            //id:data:amount to get parsed
            String[] data = item.split(":");
            
            int id = Integer.parseInt(data[0]);
            byte dt = Byte.parseByte(data[1]);
            int amount = Integer.parseInt(data[2]);
            
            ItemStack is = new ItemStack(id);
            is.getData().setData(dt);
            is.setAmount(amount);
            items.add(is);
        }
        
        return items;
    }
    
    public static boolean getTagAPI() {
        try {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("TagAPI");

            if (plugin == null || !(plugin instanceof org.kitteh.tag.TagAPI)) {
                return false;
            }
            
            if(!plugin.isEnabled()) {
                return false;
            }

            return true;
        } catch(NoClassDefFoundError e) {
            return false;
        }
    }

    public static void refreshTags(Player p) {
        try {
            org.kitteh.tag.TagAPI.refreshPlayer(p);
        } catch(NoClassDefFoundError e) {
        }
    }

    public static void refreshTags() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            refreshTags(p);
        }
    }
    
    public static com.sk89q.worldguard.bukkit.WorldGuardPlugin getWorldGuard() {
        try {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

            if (plugin == null || !(plugin instanceof com.sk89q.worldguard.bukkit.WorldGuardPlugin)) {
                return null;
            }
            
            if(!plugin.isEnabled()) {
                return null;
            }

            return (com.sk89q.worldguard.bukkit.WorldGuardPlugin) plugin;
        } catch(NoClassDefFoundError e) {
            return null;
        }
    }
            
    public static List<Block> getBlocksFromChunk(Chunk c) {
        List<Block> blocks = new ArrayList<Block>();
        
        for(int x = 0; x < 16; x++) {
            for(int y = 0; y < 256; y++) {
                for(int z = 0; z < 16; z++) {
                    Block b = c.getBlock(x, y, z);
                    if(b == null) {
                        continue;
                    }
                    
                    blocks.add(b);
                }
            }
        }
        return blocks;
    }
    
    public static List<Block> getBlocksFromRegion(com.sk89q.worldguard.protection.regions.ProtectedRegion region, World w) {
        if(!region.getTypeName().equalsIgnoreCase("cuboid")) {
            return new ArrayList<Block>();
        }
        
        com.sk89q.worldedit.BlockVector minPoint = region.getMinimumPoint();
        com.sk89q.worldedit.BlockVector maxPoint = region.getMaximumPoint();
        
        Block mN = w.getBlockAt(minPoint.getBlockX(), minPoint.getBlockY(), minPoint.getBlockZ());
        Block mX = w.getBlockAt(maxPoint.getBlockX(), maxPoint.getBlockY(), maxPoint.getBlockZ());
        
        List<Block> blocks = getBlocksFromRegion(mN, mX);
        return blocks;
    }
    
    public static List<Block> getBlocksFromRegion(Block min, Block max) {
        List<Block> blocks = new ArrayList<Block>();
        
        for(int x = min.getX(); x < max.getX(); x++) {
            for(int y = min.getY(); y < max.getY(); y++) {
                for(int z = min.getZ(); z < max.getZ(); z++) {
                    blocks.add(min.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        
        return blocks;
    }

    public static String TimeAway(Date unbanDate) {
        Long NowInMilli = (new Date()).getTime();
        Long TargetInMilli = unbanDate.getTime();
        Long diffInSeconds = (TargetInMilli - NowInMilli) / 1000+1;

        long diff[] = new long[] {0,0,0,0,0};
        /* sec */diff[4] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
        /* min */diff[3] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
        /* hours */diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
        /* days */diff[1] = (diffInSeconds = (diffInSeconds / 24)) >= 31 ? diffInSeconds % 31: diffInSeconds;
        /* months */diff[0] = (diffInSeconds = (diffInSeconds / 31));
        
        String message = "";
        
        if(diff[0] > 0) {
            message += diff[0] + " month";
            if(diff[0] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[1] > 0) {
            message += diff[1] + " day";
            if(diff[1] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[2] > 0) {
            message += diff[2] + " hour";
            if(diff[2] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[3] > 0) {
            message += diff[3] + " minute";
            if(diff[3] > 1) {
                message += "s";
            }
            return message;
        }
        if(diff[4] > 0) {
            message += diff[4] + " second";
            if(diff[4] > 1) {
                message += "s";
            }
            return message;
        }
        
        return "Invalid Time Diff!";
    }
    
    public static String CapitalizeFirstLetter(String word) {
        String[] words = word.split("\\s");
        
        String returnValue = "";
        for(int i = 0; i < words.length; i++){
            char capLetter = Character.toUpperCase(words[i].charAt(0));
            returnValue +=  " " + capLetter + words[i].substring(1, words[i].length());
        }
        
        return returnValue.substring(1, returnValue.length());
    }
}

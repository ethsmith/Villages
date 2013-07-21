package com.domsplace.Utils;

import com.domsplace.DataManagers.VillagePluginManager;
import com.domsplace.VillageBase;
import com.domsplace.VillagesPlugin;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.SimplePluginManager;

public class VillageUtils extends VillageBase {
    public static VillagesPlugin plugin;
    
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
        if(!VillageVillagesUtils.isVillageWorld(player.getLocation().getWorld())) {
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
        
        if(!VillagePluginManager.PluginYML.getBoolean("debug")) {
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
    
    public static boolean unloadPlugin(Plugin pl) {
        PluginManager pm = plugin.getServer().getPluginManager();
        SimplePluginManager spm = (SimplePluginManager) pm;
        SimpleCommandMap cmdMap = null;
        List<Plugin> plugins = null;
        Map<String, Plugin> names = null;
        Map<String, Command> commands = null;
        Map<Event, SortedSet<RegisteredListener>> listeners = null;
        boolean reloadlisteners = true;

        if (spm != null) {
            try {
                Field pluginsField = spm.getClass().getDeclaredField("plugins");
                pluginsField.setAccessible(true);
                plugins = (List<Plugin>) pluginsField.get(spm);

                Field lookupNamesField = spm.getClass().getDeclaredField("lookupNames");
                lookupNamesField.setAccessible(true);
                names = (Map<String, Plugin>) lookupNamesField.get(spm);

                try {
                    Field listenersField = spm.getClass().getDeclaredField("listeners");
                    listenersField.setAccessible(true);
                    listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField.get(spm);
                } catch (Exception e) {
                    reloadlisteners = false;
                }

                Field commandMapField = spm.getClass().getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                cmdMap = (SimpleCommandMap) commandMapField.get(spm);

                Field knownCommandsField = cmdMap.getClass().getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                commands = (Map<String, Command>) knownCommandsField.get(cmdMap);
            } catch (NoSuchFieldException e) {
                return false;
            } catch (IllegalAccessException e) {
                return false;
            }
        }

        pm.disablePlugin(pl);
        if (plugins != null && plugins.contains(pl)) {
            plugins.remove(pl);
        }

        if (names != null && names.containsKey(pl.getName())) {
            names.remove(pl.getName());
        }

        if (listeners != null && reloadlisteners) {
            for (SortedSet<RegisteredListener> set : listeners.values()) {
                for (Iterator<RegisteredListener> it = set.iterator(); it.hasNext(); ) {
                    RegisteredListener value = it.next();

                    if (value.getPlugin() != pl) {
                        continue;
                    }
                    it.remove();
                }
            }
        }

        if (cmdMap != null) {
            for (Iterator<Map.Entry<String, Command>> it = commands.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Command> entry = it.next();
                if (entry.getValue() instanceof PluginCommand) {
                    PluginCommand c = (PluginCommand) entry.getValue();
                    if (c.getPlugin() != pl) {
                        continue;
                    }
                    c.unregister(cmdMap);
                    it.remove();
                }
            }
        }
        return true;
    }
}

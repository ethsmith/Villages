package com.domsplace.Villages.Bases;

import com.domsplace.Villages.DataManagers.ConfigManager;
import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Utils.VillageLanguageUtils;
import com.domsplace.Villages.Utils.VillageUtils;
import com.domsplace.Villages.VillagesPlugin;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Base {
    public static final String TAB = "     ";
    public static boolean DEBUG_MODE = true;
    private static VillagesPlugin plugin;
    
    public static String ChatError = ChatColor.RED + "";
    public static String ChatDefault = ChatColor.GRAY + "";
    public static String ChatImportant = ChatColor.BLUE + "";
    public static String ChatPrefix = "§7[§9Villages§7] ";
    
    public static String VillageColor = ChatColor.GREEN + "";
    public static String EnemyColor = ChatColor.DARK_RED + "";
    
    public static String PlayerChatPrefix = "§6[§9%v%§6] ";
    
    public static boolean UsePlots = true;
    
    public static List<String> villageCreatedCommands;
    public static List<String> villageDeletedCommands;
    public static List<String> villagePlayerAddedCommands;
    public static List<String> villagePlayerRemovedCommands;
    public static List<String> villageMayorDeathCommands;
    
    public static String WildernessPrefix = ChatColor.BLUE + "Wilderness";
    
    public static String CheckUpdateURL = "http://domsplace.com/ajax/getProjectVersion.php?name=Villages";
    public static String LatestVersionURL = "http://dev.bukkit.org/bukkit-plugins/villages/";
    
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
        if("".equals(message)) return;
        message = message.replaceAll("\\t", TAB);
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
    
    public static String gK(String key) {
        return VillageLanguageUtils.getKey(key);
    }
    
    public static String gK(String key, Village village) {
        return VillageLanguageUtils.getKey(key, village);
    }
    
    public static String gK(String key, double money) {
        return VillageLanguageUtils.getKey(key, money);
    }
    
    public static String gK(String key, OfflinePlayer player) {
        return VillageLanguageUtils.getKey(key, player);
    }
    
    public static String gK(String key, OfflinePlayer player, double amount) {
        return VillageLanguageUtils.getKey(key, player, amount);
    }
    
    public static String gK(String key, Chunk c) {
        return VillageLanguageUtils.getKey(key, c);
    }
    
    public static void debug(Object message) {
        if(!DEBUG_MODE) return;
        broadcast("§a[§bDEBUG§a] §d" + message.toString());
    }
    
    public static boolean isPlayer(Object o) {
        return (o instanceof Player) && (o != null);
    }
    
    public static void setPlugin(VillagesPlugin plugin) {
        Base.plugin = plugin;
    }
    
    public static VillagesPlugin getPlugin() {
        return Base.plugin;
    }
    
    public static File getDataFolder() {
        return getPlugin().getDataFolder();
    }
    
    public static void log(Object o) {
        getPlugin().getLogger().log(Level.INFO, o.toString());
    }
    
    public static YamlConfiguration getConfig() {
        return DataManagerBase.CONFIG_MANAGER.config;
    }
    
    public static String getStringFromURL(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                    connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        return response.toString();
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

    public static List<ItemStack> getItemFromString(List<String> stringList) {
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

    public static String getTimeUntilHuman(Date unbanDate) {
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
    
    public static String capitalizeFirstLetter(String word) {
        String[] words = word.split("\\s");
        
        String returnValue = "";
        for(int i = 0; i < words.length; i++){
            char capLetter = Character.toUpperCase(words[i].charAt(0));
            returnValue +=  " " + capLetter + words[i].substring(1, words[i].length());
        }
        
        return returnValue.substring(1, returnValue.length());
    }
    
    public static List<Village> getVillages() {
        return VillageUtils.Villages;
    }
}

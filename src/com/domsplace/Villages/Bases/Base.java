package com.domsplace.Villages.Bases;

import com.domsplace.BansUtils;
import com.domsplace.Villages.Enums.ExpandMethod;
import com.domsplace.Villages.GUI.VillagesGUIManager;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.VillagesPlugin;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Base extends RawBase {
    public static final String TAB = "    ";
    
    public static String GUIScreen;
    
    public static boolean DebugMode = true;
    public static VillagesPlugin plugin;
    
    public static String ChatDefault = ChatColor.GRAY.toString();
    public static String ChatImportant = ChatColor.BLUE.toString();
    public static String ChatError = ChatColor.RED.toString();
    
    public static String ChatPrefix = "&9[&7Villages&9]";
    public static String VillagePrefix = "&9[&7%v%&9]";
    public static String WildernessPrefix = "&9[&7Wilderness&9]";
    
    public static String Wilderness = "Wilderness";
    
    public static String FriendColor = "&a";
    public static String EnemyColor = "&4";
    
    private static String permissionMessage = "&4You don't have permission to do this!";
    
    public static List<World> VillageWorlds = new ArrayList<World>();
    public static List<String> TryWorlds = new ArrayList<String>();
    
    public static ExpandMethod ExpandingMethod = ExpandMethod.PER_CHUNK;
    
    public static VillagesGUIManager guiManager;
    
    //HOOKING OPTIONS
    public static boolean useSQL = false;
    public static boolean useWorldGuard = false;
    public static boolean useTagAPI = false;
    public static boolean useEconomy = false;
    public static boolean useHeroChat = false;
    public static boolean useScoreboards = false;

    //String Utils
    public static String getPrefix() {
        if(!ChatPrefix.contains("§")) ChatPrefix = colorise(ChatPrefix);
        if(ChatPrefix.equalsIgnoreCase("")) return "";
        if(ChatPrefix.endsWith(" ")) return ChatPrefix;
        return ChatPrefix + " ";
    }
    
    public static String getVillagePrefix(Village v) {
        String p = VillagePrefix;
        if(v != null) {
            p = VillagePrefix.replaceAll("%v%", v.getName());
        } else {
            p = VillagePrefix.replaceAll("%v%", "Wilderness");
        }
        
        if(!p.contains("§")) p = colorise(p);
        if(p.replaceAll(" ", "").equalsIgnoreCase("")) return "";
        return p;
    }
    
    public static String getDebugPrefix() {
        return ChatColor.LIGHT_PURPLE + "DEBUG: " + ChatColor.AQUA;
    }
    
    public static String colorise(Object o) {
        String msg = o.toString();
        
        String[] andCodes = {"&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", 
            "&8", "&9", "&a", "&b", "&c", "&d", "&e", "&f", "&l", "&o", "&n", 
            "&m", "&k", "&r"};
        
        String[] altCodes = {"§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", 
            "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f", "§l", "§o", "§n", 
            "§m", "§k", "§r"};
        
        for(int x = 0; x < andCodes.length; x++) {
            msg = msg.replaceAll(andCodes[x], altCodes[x]);
        }
        
        return msg;
    }
    
    public static String getPermissionMessage() {
        return Base.permissionMessage;
    }
    
    public static void setPermissionMessage(String msg) {
        Base.permissionMessage = msg;
    }

    public static String[] listToArray(List<String> c) {
        String[] s = new String[c.size()];
        for(int i = 0; i < c.size(); i++) {
            s[i] = c.get(i);
        }
        
        return s;
    }
    
    public static String capitalizeFirstLetter(String s) {
        if(s.length() < 2) return s.toUpperCase();
        String end = s.substring(1, s.length());
        return s.substring(0, 1).toUpperCase() + end;
    }

    public static String capitalizeEachWord(String toLowerCase) {
        String[] words = toLowerCase.split(" ");
        String w = "";
        for(int i = 0; i < words.length; i++) {
            w += capitalizeFirstLetter(words[i]);
            if(i < (words.length-1)) {
                w += " ";
            }
        }
        return w;
    }
    
    public static String trim(String s, int length) {
        if(s.length() < length) return s;
        return s.substring(0, length);
    }
    
    //Messaging Utils
    public static void sendMessage(CommandSender sender, String msg) {
        if(msg.replaceAll(" ", "").equalsIgnoreCase("")) return;
        if(!inVillageWorld(sender)) return;
        msg = msg.replaceAll("\\t", TAB);
        sender.sendMessage(ChatDefault + getPrefix() + ChatDefault + msg);
    }

    public static void sendMessage(CommandSender sender, String msg, Object... objs) {
        String s = msg;
        for(int i = 0; i < objs.length; i++) {
            s = s.replaceAll("{" + i + "}", objs[i].toString());
        }
        sendMessage(sender, s);
    }
    
    public static void sendMessage(CommandSender sender, Object[] msg) {
        for(Object o : msg) {
            sendMessage(sender, o);
        }
    }

    public static void sendMessage(CommandSender sender, List<?> msg) {
        sendMessage(sender, msg.toArray());
    }

    public static void sendMessage(CommandSender sender, Object msg) {
        if(msg == null) return;
        if(msg instanceof String) {
            sendMessage(sender, (String) msg);
            return;
        }
        
        if(msg instanceof Object[]) {
            sendMessage(sender, (Object[]) msg);
            return;
        }
        
        if(msg instanceof List<?>) {
            sendMessage(sender, (List<?>) msg);
            return;
        }
        sendMessage(sender, msg.toString());
    }

    public static void sendMessage(Player sender, Object... msg) {
        sendMessage((CommandSender) sender, msg);
    }

    public static void sendMessage(OfflinePlayer sender, Object... msg) {
        if(!sender.isOnline()) return;
        sendMessage(sender.getPlayer(), msg);
    }

    public static void sendMessage(Entity sender, Object... msg) {
        if(!(sender instanceof CommandSender)) return;
        sendMessage(sender, msg);
    }

    public static void sendMessage(Resident sender, Object... msg) {
        sendMessage(sender.getOfflinePlayer(), msg);
    }
    
    public static void sendMessage(Object o) {
        sendMessage(Bukkit.getConsoleSender(), o);
    }
    
    public static void sendAll(List<Player> players, Object o) {
        for(Player p : players) {
            sendMessage(p, o);
        }
    }
    
    public static void sendAll(Player[] players, Object o) {
        for(Player p : players) {
            sendMessage(p, o);
        }
    }
    
    public static void sendAll(Object o) {
        sendAll(Bukkit.getOnlinePlayers(), o);
    }
    
    public static void sendAll(String permission, Object o) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!hasPermission(p, permission)) continue;
            sendMessage(p, o);
        }
    }
    
    public static void broadcast(Object o) {
        sendMessage(o);
        sendAll(o);
    }
    
    public static void broadcast(String permission, Object o) {
        sendMessage(o);
        sendAll(permission, o);
    }
    
    public static void debug(Object o) {
        if(!DebugMode) return;
        broadcast(getDebugPrefix() + o.toString());
    }
    
    public static void error(String message, boolean postfix) {
        String msg = ChatError + "Error: " + ChatColor.DARK_RED + message;
        if(postfix && DebugMode) msg += ChatColor.YELLOW + " Caused by: ";
        if(postfix && !DebugMode) msg += ChatColor.YELLOW + " Turn debug mode on to view whole error.";
        sendMessage(msg);
    }
    
    public static void error(String message) {
        error(message, false);
    }
    
    public static void error(String message, Exception e) {
        error(message, true);
        if(!DebugMode) return;
        String lines = "\n" + e.getClass().getName() + ":  " +  e.getMessage();
        for(StackTraceElement ste : e.getStackTrace()) {
            
            lines += "\t" + ChatColor.GRAY + "at " + ste.getClassName() + "." 
                    + ste.getMethodName() + "(" + ste.getFileName() + ":" + 
                    ste.getLineNumber() + ")\n";
        }
        
        sendMessage(lines);
    }
    
    public static void log(Object o) {
        getPlugin().getLogger().info(o.toString());
    }
    
    //Conversion Utils
    public static boolean isPlayer(Object o) {
        return o instanceof Player;
    }
    
    public static Player getPlayer(Object o) {
        return (Player) o;
    }
    
    public static OfflinePlayer getOfflinePlayer(Player player) {
        return Bukkit.getOfflinePlayer(player.getName());
    }
    
    public static OfflinePlayer getOfflinePlayer(String player) {
        return Bukkit.getOfflinePlayer(player);
    }
    
    public static boolean isInt(Object o) {
        try {
            Integer.parseInt(o.toString());
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    public static int getInt(Object o) {
        return Integer.parseInt(o.toString());
    }
    
    public static double getDouble(Object o) {
        return Double.parseDouble(o.toString());
    }
    
    public static boolean isDouble(Object o) {
        try {
            Double.parseDouble(o.toString());
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    public static boolean isShort(Object o) {
        try {
            Short.parseShort(o.toString());
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    public static short getShort(Object o) {
        return Short.parseShort(o.toString());
    }
    
    public static boolean isByte(Object o) {
        try {
            Byte.parseByte(o.toString());
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    public static byte getByte(Object o) {
        return Byte.parseByte(o.toString());
    }
    
    public static String listToString(List<String> strings) {
        return listToString(strings, ", ");
    }
    
    public static String listToString(List<String> strings, String seperator) {
        String m = "";
        
        for(int i = 0; i < strings.size(); i++) {
            m += strings.get(i);
            if(i < (strings.size() - 1)) m += seperator;
        }
        
        return m;
    }
    
    //Plugin Utils
    public static void setPlugin(VillagesPlugin plugin) {
        Base.plugin = plugin;
    }
    
    public static VillagesPlugin getPlugin() {
        return plugin;
    }
    
    public static File getDataFolder() {
        return getPlugin().getDataFolder();
    }
    
    public static YamlConfiguration getConfig() {
        return DataManager.CONFIG_MANAGER.getCFG();
    }
    
    //Location Utils
    public static boolean isVillageWorld(World world) {
        for(World w : VillageWorlds) if(w.equals(world)) return true;
        return false;
    }
    
    public static boolean inVillageWorld(Player player) {
        return isVillageWorld(player.getWorld());
    }
    
    public static boolean inVillageWorld(Entity player) {
        return isVillageWorld(player.getWorld());
    }
    
    public static boolean inVillageWorld(Block block) {
        return isVillageWorld(block.getWorld());
    }
    
    public static boolean inVillageWorld(CommandSender sender) {
        if(!isPlayer(sender)) return true;
        return inVillageWorld(getPlayer(sender));
    }
    
    public static String getLocationString(Location location) {
        return location.getX() + ", " + location.getY() + ", " + location.getZ()
                + " " + location.getWorld().getName();
    }
    
    public static String getStringLocation (Chunk chunk) {
        return chunk.getX() + ", " + chunk.getZ() + " : " + chunk.getWorld().getName();
    }
    
    public static boolean isCoordBetweenCoords(int checkX, int checkZ, int outerX, int outerZ, int maxX, int maxZ) {
        if(checkX >= Math.min(outerX, maxX) && checkX <= Math.max(outerX, maxX)) {
            if(checkZ >= Math.min(outerZ, maxZ) && checkZ <= Math.max(outerZ, maxZ)) { return true; }
        }
        return false;
    }
    
    //Player Utils
    public static boolean hasPermission(CommandSender sender, String permission) {
        debug("Checking if " + sender.getName() + " has " + permission);
        if(permission.equals("Villages.none")) return true;
        if(!isPlayer(sender)) return true;
        
        //PermissionsEx Permission Checking
        if(PluginHook.PEX_HOOK.isHooked()) {
            return PluginHook.PEX_HOOK.hasPermission(getPlayer(sender), permission);
        }
        
        return getPlayer(sender).hasPermission(permission);
    }
    
    public boolean canSee(CommandSender p, OfflinePlayer target) {
        if(!isPlayer(p)) return true;
        if(!target.isOnline()) return true;
        return getPlayer(p).canSee(target.getPlayer());
    }
    
    public boolean isVisible(OfflinePlayer t) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!canSee(p, t)) return false;
        }
        return true;
    }
    
    public List<OfflinePlayer> getPlayersList() {
        List<OfflinePlayer> rv = new ArrayList<OfflinePlayer>();
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!isVisible(p)) continue;
            rv.add(Bukkit.getOfflinePlayer(p.getName()));
        }
        return rv;
    }
    
    public static boolean isMuted(OfflinePlayer player) {
        if(!PluginHook.SEL_BANS_HOOK.isHooked()) return false;
        return !BansUtils.CanPlayerTalk(player);
    }
    
    //Language Utils
    public static List<String> gk(String key, Object... o) {
        return DataManager.LANGUAGE_MANAGER.getKey(key, o);
    }
    
    public static void sk(CommandSender sender, String key, Object... o) {
        sendMessage(sender, gk(key, o));
    }
    
    public static void sk(Resident sender, String key, Object... o) {
        sendMessage(sender, gk(key, o));
    }
    
    public static void bk(String key, Object... o) {
        broadcast(gk(key, o));
    }
    
    //Economy Utils
    public static boolean hasBalance(String player, double amt) {
        if(!useEconomy || PluginHook.VAULT_HOOK.getEconomy() == null) return true;
        if(getBalance(player) >= amt) return true;
        return false;
    }
    
    public static boolean hasBalance(Village village, double amt) {
        if(!useEconomy || PluginHook.VAULT_HOOK.getEconomy() == null) return true;
        if(getBalance(village) >= amt) return true;
        return false;
    }
    
    public static double getBalance(String player) {
        if(!useEconomy || PluginHook.VAULT_HOOK.getEconomy() == null) return -1.0d;
        return PluginHook.VAULT_HOOK.getEconomy().getBalance(player);
    }
    
    public static double getBalance(Village village) {
        if(!useEconomy || PluginHook.VAULT_HOOK.getEconomy() == null) return -1.0d;
        return village.getBank().getWealth();
    }
    
    public static double getCost(String key) {
        return getConfig().getDouble("costs." + key, 0d);
    }
    
    public static String getMoney(double money) {
        return PluginHook.VAULT_HOOK.getEconomy().format(money);
    }
    
    //Time Utils
    public static long getNow() {
        return System.currentTimeMillis();
    }
    
    public static String getTimeDifference(Date late) {return Base.getTimeDifference(new Date(), late);}
    
    public static String getTimeDifference(Date early, Date late) {
        Long NowInMilli = late.getTime();
        Long TargetInMilli = early.getTime();
        Long diffInSeconds = (NowInMilli - TargetInMilli) / 1000;

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
        
        return "Time Error";
    }
}

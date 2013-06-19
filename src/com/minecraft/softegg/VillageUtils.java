package com.minecraft.softegg;

import static com.minecraft.softegg.VillageBase.ChatDefault;
import static com.minecraft.softegg.VillageBase.ChatError;
import static com.minecraft.softegg.VillageBase.ChatPrefix;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VillageUtils extends VillageBase {
    
    private static File dataFolder;
    public static net.milkbowl.vault.economy.Economy economy;
    
    public static String sqlHost = "";
    public static String sqlDB = "";
    public static String sqlUser = "";
    public static String sqlPass = "";
    public static String sqlPort = "";
    private static Connection dbCon;
    
    public static List<Village> villages;
    
    public static HashMap<Player, Village> townInvites;
    
    public static ArrayList<Player> sentWilderness;
    
    public static void LoadVillages() {
        villages = new ArrayList<Village>();
        townInvites = new HashMap<Player, Village>();
        sentWilderness = new ArrayList<Player>();
        
        List<String> towns = VillageDataManager.dataManager.getTownNames();
        for(String t : towns) {
            Village v = VillageDataManager.dataManager.loadVillage(t);
            if(v == null) {
                continue;
            }
            villages.add(v);
            VillageDataManager.dataManager.saveVillage(v);
        }
    }
    
    public static void SaveAllVillages() {
        for(Village vil : villages) {
            VillageDataManager.dataManager.saveVillage(vil);
        }
    }
    
    public static void msgConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatPrefix + ChatDefault + message);
    }

    public static void setDataFolder(File dataFolder) {
        VillageUtils.dataFolder = dataFolder;
    }
    
    public static File getDataFolder() {
        return VillageUtils.dataFolder;
    }
    
    public static SoftEggLand getSoftEggLandPlugin() {
        try {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("SoftEggLand");

            if (plugin == null || !(plugin instanceof com.minecraft.softegg.SoftEggLand)) {
                return null;
            }

            return (com.minecraft.softegg.SoftEggLand) plugin;
        } catch(NoClassDefFoundError e) {
            return null;
        }
    }
    
    public static Villages getVillagesPlugin() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Villages");

        if (plugin == null || !(plugin instanceof Villages)) {
            return null;
        }

        return (Villages) plugin;
    }
    
    public static String ColorString(String msg) {
        String[] andCodes = {"&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9", "&a", "&b", "&c", "&d", "&e", "&f", "&l", "&o", "&n", "&m", "&k", "&r"};
        String[] altCodes = {"§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e", "§f", "§l", "§o", "§n", "§m", "§k", "§r"};
        
        for(int x = 0; x < andCodes.length; x++) {
            msg = msg.replaceAll(andCodes[x], altCodes[x]);
        }
        
        return msg;
    }
    
    public static Boolean setupEconomy(Villages plugin) {
        try {
            RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (economyProvider != null) {
                economy = economyProvider.getProvider();
            }
            return (economy != null);
        } catch(NoClassDefFoundError e) {
            economy = null;
            return false;
        }
    }
    
    public static boolean sqlConnect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://"+sqlHost+":"+sqlPort+"/" + sqlDB;
            msgConsole("Opening SQL connection to " + url);
            dbCon = DriverManager.getConnection(url,sqlUser,sqlPass);
            return true;
        } catch (Exception ex) {
            msgConsole(ChatError + "Failed to Connect to SQL. Error: " + ex.getLocalizedMessage());
            return false;
        }
    }
    
    public static boolean sqlQuery(String query) {
        try {
            PreparedStatement sqlStmt = dbCon.prepareStatement(query);
            boolean result = sqlStmt.execute(query);
            return result;
        } catch (SQLException ex) {
            msgConsole(ChatError + "Failed to execute SQL query. Error: " + ex.getLocalizedMessage());
        }
        return false;
    }
    
    public static int sqlQueryID(String query) {
        try {
            PreparedStatement sqlStmt = dbCon.prepareStatement(query);
            int result = sqlStmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            return result;
        } catch (SQLException ex) {
            msgConsole(ChatError + "Failed to execute SQL (Return ID) query. Error: " + ex.getLocalizedMessage());
        }
        return -1;
    }
    
    public static List<Map<String, String>> sqlFetch(String query) {
        List<Map<String, String>> results = new ArrayList<Map<String, String>>();
        try {
            Statement myStmt = dbCon.createStatement();
            ResultSet result = myStmt.executeQuery(query);
            while (result.next()){
                Map<String, String> data = new HashMap<String, String>();
                for(int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                    data.put(result.getMetaData().getColumnName(i), result.getString(result.getMetaData().getColumnName(i)));
                }
                results.add(data);
            }
        }
        catch (Exception sqlEx) {
            msgConsole(ChatError + "Failed to result SQL query. Error: " + sqlEx.getLocalizedMessage());
        }
        
        if(results.size() < 1) {
            return null;
        }
        
        return results;
    }
    
    public static void sqlClose() {
        try {
            dbCon.close();
            msgConsole("Closing SQL connection...");
        } catch (Exception ex) {
            msgConsole(ChatError + "Failed to Close SQL connection. Error: " + ex.getLocalizedMessage());
        }
    }

    public static String[] getCommandDescription(String name) {
        
        String[] result = {
            ChatImportant + "Usage: " + ChatDefault + Villages.pluginYML.getString("commands." + name + ".usage").replaceAll("<command>", name),
            ChatImportant + "Description: " + ChatDefault + Villages.pluginYML.getString("commands." + name + ".description")
        };
        
        return result;
    }
    public static long getNow() {
        Date someTime = new Date();
        long currentMS = someTime.getTime();
        return currentMS;
    }
    
    public static String dateToSQL(Date date) {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String now = ft.format(date);
        return now;
    }
    
    public static Date getSQLDate(String sqlDate) {
        SimpleDateFormat fat = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        Date returnDate = new Date();
        try {
            returnDate = fat.parse(sqlDate);
        } catch (ParseException ex) {
            return new Date();
        }
        return returnDate;
    }

    static Village getPlayerVillage(Player player) {
        for(Village v : villages) {
            if(v.isResident(player)) {
                return v;
            }
        }
        return null;
    }

    static Village getVillage(String string) {
        for(Village v : villages) {
            if(!v.getName().toLowerCase().startsWith(string.toLowerCase())) {
                continue;
            }
            return v;
        }
        return null;
    }

    static Village getVillageExact(String string) {
        for(Village v : villages) {
            if(!v.getName().toLowerCase().equals(string.toLowerCase())) {
                continue;
            }
            return v;
        }
        return null;
    }
    
    public static String getStringLocation (Location location) {
        return location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + " " + location.getWorld().getName();
    }
    
    public static String getStringLocation (Chunk chunk) {
        return chunk.getX() + ", " + chunk.getZ() + " : " + chunk.getWorld().getName();
    }
    
    public static boolean isChunkInATownsArea(Chunk chunk) {
        for(Village v : villages) {
            if(!v.isChunkInTownArea(chunk)) {
                continue;
            }
            return true;
        }
        
        return false;
    }
    
    public static boolean isChunkInATownsArea(Chunk chunk, Village ignored) {
        for(Village v : villages) {
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
        for(Village v : villages) {
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

    public static boolean townsOverlap(Village v) {
        for(Chunk c : v.getTownArea()) {
            if(!isChunkInATownsArea(c, v)) {
                continue;
            }
            return true;
        }
        return false;
    }
}

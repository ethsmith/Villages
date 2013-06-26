package com.domsplace.Utils;

import com.domsplace.Objects.Village;
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
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public class VillageSQLUtils {
    public static String sqlHost = "";
    public static String sqlDB = "";
    public static String sqlUser = "";
    public static String sqlPass = "";
    public static String sqlPort = "";
    private static Connection dbCon;
    
    public static boolean sqlConnect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://"+sqlHost+":"+sqlPort+"/" + sqlDB;
            VillageUtils.msgConsole("Opening SQL connection to " + url);
            dbCon = DriverManager.getConnection(url,sqlUser,sqlPass);
            return true;
        } catch (Exception ex) {
            VillageUtils.Error("Failed to connect to SQL.", ex.getLocalizedMessage());
            return false;
        }
    }
    
    public static boolean sqlQuery(String query) {
        try {
            PreparedStatement sqlStmt = dbCon.prepareStatement(query);
            boolean result = sqlStmt.execute(query);
            return true;
        } catch (SQLException ex) {
            VillageUtils.Error("Failed to execute query.", ex.getLocalizedMessage());
        }
        return false;
    }
    
    public static int sqlQueryID(String query) {
        try {
            PreparedStatement sqlStmt = dbCon.prepareStatement(query);
            int result = sqlStmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            return result;
        } catch (SQLException ex) {
            VillageUtils.Error("Failed to execute query and return ID.", ex.getLocalizedMessage());
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
            VillageUtils.Error("Failed to fetch SQL. ", sqlEx.getLocalizedMessage());
        }
        
        if(results.size() < 1) {
            return null;
        }
        
        return results;
    }
    
    public static void sqlClose() {
        try {
            dbCon.close();
            VillageUtils.msgConsole("Closing SQL Connection...");
        } catch (Exception ex) {
            VillageUtils.Error("Failed to close SQL Connection.", ex.getLocalizedMessage());
        }
    }
    
    public static int recordSQLPlayer(OfflinePlayer player) {
        int id = getSQLPlayerID(player);
        if(id != -1) {
            return id;
        }
        
        //Player doesn't exist, create one.
        id = VillageSQLUtils.sqlQueryID(""
                + "INSERT INTO `VillagesPlayers` ("
                + "`VillagePlayerName`) VALUES ("
                + "'" + player.getName() + "')"
                + ";");
        
        return id;
    }
    
    public static int getSQLPlayerID(OfflinePlayer player) {
        String stmt = "SELECT `VillagePlayerID` FROM `VillagesPlayers` WHERE `VillagePlayerName` LIKE '" + player.getName() +"' LIMIT 1;";
        List<Map<String, String>> results = VillageSQLUtils.sqlFetch(stmt);
        if(results == null) {
            return -1;
        }
        
        if(results.size() == 0) {
            return -1;
        }
        
        Map<String, String> pl = results.get(0);
        if(pl == null) {
            return -1;
        }
        
        if(!pl.containsKey("VillagePlayerID")) {
            return -1;
        }
        
        try {
            return Integer.parseInt(pl.get("VillagePlayerID"));
        } catch (Exception ex) {
            return -1;
        }
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
    
    public static OfflinePlayer getSQLPlayerByID (int id) {
        String stmt = "SELECT `VillagePlayerName` FROM `VillagesPlayers` WHERE `VillagePlayerID`='" + id +"' LIMIT 1;";
        List<Map<String, String>> results = VillageSQLUtils.sqlFetch(stmt);
        if(results == null) {
            return null;
        }
        
        if(results.size() == 0) {
            return null;
        }
        
        Map<String, String> pl = results.get(0);
        if(pl == null) {
            return null;
        }
        
        if(!pl.containsKey("VillagePlayerName")) {
            return null;
        }
        
        try {
            return Bukkit.getOfflinePlayer(pl.get("VillagePlayerName"));
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static int getVillageIDByName(String name) {
        String stmt = "SELECT `VillageID` FROM `Villages` WHERE `VillageName` LIKE '" + name + "' LIMIT 1;";
        List<Map<String, String>> results = VillageSQLUtils.sqlFetch(stmt);
        if(results == null) {
            return -1;
        }
        
        if(results.size() == 0) {
            return -1;
        }
        
        Map<String, String> pl = results.get(0);
        if(pl == null) {
            return -1;
        }
        
        if(!pl.containsKey("VillageID")) {
            return -1;
        }
        
        try {
            return Integer.parseInt(pl.get("VillageID"));
        } catch (Exception ex) {
            return -1;
        }
    }
    
    public static List<ItemStack> getVillageItems(Village village) {
        if(village.idSQL == -1) {
            village.idSQL = getVillageIDByName(village.getName());
        }
        
        return getVillageItems(village.idSQL);
    }
    
    public static List<ItemStack> getVillageItems(int villageID) {
        List<ItemStack> items = new ArrayList<ItemStack>();
        
        String stmt = "SELECT ItemID, ItemData, ItemAmount FROM `VillageBankItems` WHERE VillageID='" + villageID + "';";
        List<Map<String, String>> data = VillageSQLUtils.sqlFetch(stmt);
        
        if(data == null) {
            return items;
        }
        
        for(Map<String, String> d : data) {
            if(d == null) {
                continue;
            }
            
            int id = Integer.parseInt(d.get("ItemID"));
            byte dt = Byte.parseByte(d.get("ItemData"));
            int amount = Integer.parseInt(d.get("ItemAmount"));
            
            ItemStack is = new ItemStack(id);
            is.getData().setData(dt);
            is.setAmount(amount);
            
            items.add(is);
        }
        
        return items;
    }
}

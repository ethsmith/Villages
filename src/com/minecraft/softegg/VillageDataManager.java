package com.minecraft.softegg;

import static com.minecraft.softegg.VillageBase.ChatDefault;
import static com.minecraft.softegg.VillageBase.ChatError;
import static com.minecraft.softegg.VillageBase.ChatImportant;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class VillageDataManager extends VillageBase {
    
    public static YamlConfiguration config;
    public static File configFile;
    public static VillageDataManager dataManager;
    
    public VillageDataManager() {
        VillageDataManager.dataManager = this;
    }
    
    public boolean checkConfig(Villages plugin) {
        try {
            if(!VillageUtils.getDataFolder().exists()){
                VillageUtils.getDataFolder().mkdir();
            }
            
            configFile = new File(VillageUtils.getDataFolder() + "/config.yml");

            if(!configFile.exists()) {
                configFile.createNewFile();
            }
            
            Boolean oldSQL = VillageDataManager.useSQL();
            
            config = YamlConfiguration.loadConfiguration(configFile);
            
            //Add Worlds
            if(!config.contains("Worlds")) {
                List<String> worlds = new ArrayList<String>();
                for(World w : Bukkit.getWorlds()) {
                    worlds.add(w.getName());
                }
                config.set("Worlds", worlds);
            }
            
            if(!config.contains("sql")) {
                if(!config.contains("sql.use")) {
                    config.set("sql.use", true);
                }
                //(SoftEggLand Server only) get SEL plugin
                SoftEggLand softEggPlugin = VillageUtils.getSoftEggLandPlugin();
                if(softEggPlugin != null) {
                    config.set("sql", softEggPlugin.getConfig().get("SQL"));
                    config.set("sql.table", null);
                } else {
                    config.set("sql.username", "root");
                    config.set("sql.password", "password");
                    config.set("sql.host", "localhost");
                    config.set("sql.database", "minecraft");
                    config.set("sql.port", "3306");
                }
            }
            
            if(!config.contains("townborder")) {
                config.set("townborder", 3);
            }
            
            if(!config.contains("economy")) {
                config.set("economy", true);
            }
            
            if(!config.contains("colors")) {
                if(!config.contains("colors.prefix")) {
                    config.set("colors.prefix", "&7[&9Villages&7]");
                }
                if(!config.contains("colors.error")) {
                    config.set("colors.error", "&c");
                }
                if(!config.contains("colors.default")) {
                    config.set("colors.default", "&7");
                }
                if(!config.contains("colors.important")) {
                    config.set("colors.important", "&9");
                }
            }
            VillageBase.ChatError = VillageUtils.ColorString(config.getString("colors.error"));
            VillageBase.ChatPrefix = VillageUtils.ColorString(config.getString("colors.prefix")) + " ";
            VillageBase.ChatDefault = VillageUtils.ColorString(config.getString("colors.default"));
            VillageBase.ChatImportant = VillageUtils.ColorString(config.getString("colors.important"));
            
            
            String p = "protection.";
            if(!config.contains(p + "griefwild")) {
                config.set(p + "griefwild", true);
            }
            if(!config.contains(p + "griefvillage")) {
                config.set(p + "griefvillage", false);
            }
            
            p = "cost.";
            if(!config.contains(p + "createvillage")) {
                config.set(p + "createvillage", 1000.00);
            }
            if(!config.contains(p + "expand")) {
                config.set(p + "expand", 2000.00);
            }
            
            p = "messages.";
            if(!config.contains(p + "entervillage")) {
                config.set(p + "entervillage", true);
            }
            if(!config.contains(p + "leavevillage")) {
                config.set(p + "leavevillage", true);
            }
            
            //Check That plugins are available//
            if(config.getBoolean("economy")) {
                if(!VillageUtils.setupEconomy(plugin)) {
                    VillageUtils.msgConsole(ChatError + "Couldn't find Vault! Economy disabled.");
                    config.set("economy", false);
                } else {
                    VillageUtils.msgConsole("Hooked into Economy.");
                }
            }
            
            if(config.getBoolean("sql.use")) {
                VillageUtils.sqlHost = config.getString("sql.host");
                VillageUtils.sqlDB = config.getString("sql.database");
                VillageUtils.sqlUser = config.getString("sql.username");
                VillageUtils.sqlPass = config.getString("sql.password");
                VillageUtils.sqlPort = config.getString("sql.port");
                
                if(!VillageUtils.sqlConnect()) {
                    VillageUtils.msgConsole(ChatError + "Failed to connect to SQL. Using YML.");
                    config.set("sql.use", false);
                } else {
                    VillageUtils.msgConsole("Connected to SQL successfully.");
                    SetupDatabase();
                }
            } else if(oldSQL) {
                VillageUtils.sqlClose();
            }
            
            
            if(config.getBoolean("sql.use") != true) {
                File dataFolder = new File(VillageUtils.getDataFolder() + "/data/");
                if(!dataFolder.exists()){
                    dataFolder.mkdir();
                }
                dataFolder = new File(VillageUtils.getDataFolder() + "/data/villages/");
                if(!dataFolder.exists()){
                    dataFolder.mkdir();
                }
            }
            
            saveConfig();
            
            //Load Language files//
            VillageLanguageManager.LoadLanguage();
            
        } catch (Exception ex) {
            VillageUtils.msgConsole(ChatError + "Failed to load Config YML! Error: " + ex.getMessage());
            return false;
        }
        return true;
    }
    
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException ex) {
            VillageUtils.msgConsole(ChatError + "Failed to save Config YML! Error: " + ex.getMessage());
        }
    }
    
    public void loadConfig(Villages plugin) {
        checkConfig(plugin);
    }
    
    public static Boolean useSQL() {
        if(config == null) {
            return false;
        }
        return config.getBoolean("sql.use");
    }

    public void saveVillage(Village village) {
        if(useSQL()) {
            //Todo: Finish SQL Saving.//
            int result = -1;
            //Try to get Village//
            result = this.getVillageIDByName(village.getName());
            
            if(result == -1) {
                String stmt = village.getCreateQuery();
                result = VillageUtils.sqlQueryID(stmt);
                village.idSQL = result;
            } else {
                village.idSQL = result;
                String stmt = village.getUpdateQuery();
                if(stmt != null) {
                    VillageUtils.sqlQuery(stmt);
                }
            }
            
            ArrayList<OfflinePlayer> residents = village.getResidents();
            
            if(residents.size() < 1) {
                return;
            }
            
            String rstmt = "INSERT INTO `VillagesResidents` ("
                    + "`VillagePlayerID`, "
                    + "`VillageID`"
                    + ") VALUES";
            
            for(OfflinePlayer p : residents) {
                int playerID = this.recordSQLPlayer(p);
                String stmt = "DELETE FROM `VillagesResidents` WHERE `VillagePlayerID` = '" + playerID + "';";
                VillageUtils.sqlQuery(stmt);
                rstmt += "('" + playerID + "', '" + village.getSQLID() + "'), ";
            }
            rstmt = rstmt.substring(0, rstmt.length() - 2);
            rstmt += ";";
            VillageUtils.sqlQuery(rstmt);
            
            return;
        }
        
        //Save using YML//
        try {
            File dataFolder = new File(VillageUtils.getDataFolder() + "/data/");
            if(!dataFolder.exists()) {
                dataFolder.mkdir();
            }
            
            dataFolder = new File(VillageUtils.getDataFolder() + "/data/villages/");
            if(!dataFolder.exists()) {
                dataFolder.mkdir();
            }
            
            File villageFile = new File(VillageUtils.getDataFolder() + "/data/villages/" + village.getName() + ".yml");
            if(!villageFile.exists()) {
                villageFile.createNewFile();
            }
            YamlConfiguration configuration = village.getTownAsYML();
            configuration.save(villageFile);
        } catch (Exception ex) {
            VillageUtils.msgConsole(ChatError + "Failed to save Village " + village.getName() + "! Error: " + ex.getMessage());
        }
    }
    
    public void SetupDatabase() {
        String stmt = "CREATE TABLE IF NOT EXISTS `VillagesPlayers` ("
                + "`VillagePlayerID` int(11) NOT NULL AUTO_INCREMENT,"
                + "`VillagePlayerName` VARCHAR(200) NOT NULL,"
                + "CONSTRAINT VillagesPlayerPrimaryKey PRIMARY KEY (`VillagePlayerID`)"
                + ");";
        VillageUtils.sqlQuery(stmt);
        
        stmt = "CREATE TABLE IF NOT EXISTS `Villages` ("
                + "`VillageID` int(11) NOT NULL AUTO_INCREMENT,"
                + "`VillageName` VARCHAR(200) NOT NULL,"
                + "`VillageDescription` VARCHAR(2048) NOT NULL,"
                + "`VillageCreateDate` DATETIME NOT NULL,"
                + "`VillageChunkX` int(11) NOT NULL,"
                + "`VillageChunkZ` int(11) NOT NULL,"
                + "`VillageWorld` VARCHAR(2048) NOT NULL,"
                + "`VillageSize` int(11) NOT NULL,"
                + "`VillageBank` int(11) NOT NULL,"
                + "`VillageMayorID` int(11) NOT NULL,"
                + "CONSTRAINT VillagesPrimaryKey PRIMARY KEY (`VillageID`)"
                + ");";
        VillageUtils.sqlQuery(stmt);
        
        stmt = "CREATE TABLE IF NOT EXISTS `VillagesResidents` ("
                + "`VillagePlayerID` int(11) NOT NULL, "
                + "`VillageID` int(11) NOT NULL, "
                + "CONSTRAINT VillageResidentsPrimaryKey PRIMARY KEY (`VillagePlayerID`)"
                + ");";
        VillageUtils.sqlQuery(stmt);
        
        for(Player p : Bukkit.getOnlinePlayers()) {
            recordSQLPlayer(p);
        }
    }
    
    public int recordSQLPlayer(OfflinePlayer player) {
        int id = getSQLPlayer(player);
        if(id != -1) {
            return id;
        }
        
        //Player doesn't exist, create one.
        id = VillageUtils.sqlQueryID(""
                + "INSERT INTO `VillagesPlayers` ("
                + "`VillagePlayerName`) VALUES ("
                + "'" + player.getName() + "')"
                + ";");
        
        return id;
    }
    
    public OfflinePlayer getSQLPlayerByID (int id) {
        String stmt = "SELECT `VillagePlayerName` FROM `VillagesPlayers` WHERE `VillagePlayerID`='" + id +"' LIMIT 1;";
        List<Map<String, String>> results = VillageUtils.sqlFetch(stmt);
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
    
    public int getSQLPlayer(OfflinePlayer player) {
        String stmt = "SELECT `VillagePlayerID` FROM `VillagesPlayers` WHERE `VillagePlayerName` LIKE '" + player.getName() +"' LIMIT 1;";
        List<Map<String, String>> results = VillageUtils.sqlFetch(stmt);
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
    
    public int getVillageIDByName(String name) {
        String stmt = "SELECT `VillageID` FROM `Villages` WHERE `VillageName` LIKE '" + name + "' LIMIT 1;";
        List<Map<String, String>> results = VillageUtils.sqlFetch(stmt);
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
    
    public ArrayList<OfflinePlayer> getResidentsFromVillage(int villageID) {
        String stmt = ""
                + "SELECT VillagesPlayers.VillagePlayerName FROM "
                + "`VillagesPlayers` INNER JOIN `VillagesResidents` ON VillagesResidents.VillagePlayerID = VillagesPlayers.VillagePlayerID "
                + "WHERE VillagesResidents.VillageID='" + villageID + "';";
        List<Map<String, String>> results = VillageUtils.sqlFetch(stmt);
        
        if(results == null) {
            return new ArrayList<OfflinePlayer>();
        }
        
        ArrayList<OfflinePlayer> players = new ArrayList<OfflinePlayer>();
        for(Map<String, String> iteration : results) {
            if(!iteration.containsKey("VillagePlayerName")) {
                continue;
            }
            
            OfflinePlayer player = Bukkit.getOfflinePlayer(iteration.get("VillagePlayerName"));
            players.add(player);
        }
        
        return players;
    }
    
    public Village loadVillage(String name) {
        if(useSQL()) {
            String stmt = "SELECT * FROM `Villages` WHERE `VillageName` LIKE '" + name + "' LIMIT 1;";
            List<Map<String, String>> results = VillageUtils.sqlFetch(stmt);
            if(results == null) {
                return null;
            }
            
            if(results.size() == 0) {
                return null;
            }
            Map<String, String> vil = results.get(0);
            if(vil == null) {
                return null;
            }
            
            Village village = null;
            
            try {
                village = new Village(vil.get("VillageName"));
                village.setDescription(vil.get("VillageDescription"));
                village.setCreatedDate(VillageUtils.getSQLDate(vil.get("VillageCreateDate")).getTime());
                int x = Integer.parseInt(vil.get("VillageChunkX"));
                int z = Integer.parseInt(vil.get("VillageChunkZ"));
                Chunk chunk = Bukkit.getWorld(vil.get("VillageWorld")).getChunkAt(x, z);
                village.setTownSpawn(chunk);
                ArrayList<OfflinePlayer> players = this.getResidentsFromVillage(Integer.parseInt(vil.get("VillageID")));
                if(players != null) {
                    village.setResidents(players);
                }
                
                int id = Integer.parseInt(vil.get("VillageMayorID"));
                village.setMayor(getSQLPlayerByID(id));
                village.setMoney(Double.parseDouble(vil.get("VillageBank")));
            } catch(Exception ex) {
                VillageUtils.msgConsole(ChatError + "Failed to load village " + name + ", Reason: " + ex.getMessage());
                return null;
            }
            
            return village;
        }
        
        Village village = null;
        
        try {
            File villageFile = new File(VillageUtils.getDataFolder() + "/data/villages/" + name + ".yml");
            
            YamlConfiguration vil = YamlConfiguration.loadConfiguration(villageFile);
            
            village = new Village(vil.getString("name"));
            village.setDescription(vil.getString("description"));
            village.setMayor(Bukkit.getOfflinePlayer(vil.getString("mayor")));
            
            ArrayList<OfflinePlayer> residents = new ArrayList<OfflinePlayer>();
            for(String s : vil.getStringList("residents")) {
                residents.add(Bukkit.getOfflinePlayer(s));
            }
            village.setResidents(residents);
            village.setCreatedDate(vil.getLong("createDate"));
            village.setMoney(vil.getDouble("money"));
            
            Chunk chunk = Bukkit.getWorld(vil.getString("townsquare.world")).getChunkAt(vil.getInt("townsquare.x"), vil.getInt("townsquare.z"));
            village.setTownSpawn(chunk);
        } catch (Exception ex) {
            VillageUtils.msgConsole(ChatError + "Failed to load village " + name + ", Reason: " + ex.getMessage());
            return null;
        }
        
        return village;
    }
    
    public List<String> getTownNames() {
        List<String> towns = new ArrayList<String>();
        if(useSQL()) {
            String stmt = "SELECT `VillageName` FROM `Villages` ORDER BY `VillageName` DESC;";
            List<Map<String, String>> results = VillageUtils.sqlFetch(stmt);
            if(results == null) {
                return new ArrayList<String>();
            }
            for(Map<String, String> s : results) {
                towns.add(s.get("VillageName"));
            }
            return towns;
        }
        
        File villages = new File(VillageUtils.getDataFolder() + "/data/villages/");
        ArrayList<String> names = new ArrayList<String>(Arrays.asList(villages.list()));
        for(String s : names) {
            towns.add(s.replaceAll(".yml", ""));
        }
        return towns;
    }

    public void deleteTown(Village village) {
        if(VillageDataManager.useSQL()) {
            int id = this.getVillageIDByName(village.getName());
            String stmt = "DELETE FROM `Villages` WHERE VillageID='" + id + "';";
            VillageUtils.sqlQuery(stmt);
            
            stmt = "DELETE FROM `VillagesResidents` WHERE VillageID='" + id + "';";
            VillageUtils.sqlQuery(stmt);
            VillageUtils.villages.remove(village);
            VillageBase.Broadcast(ChatDefault + "The Village " + ChatImportant + village.getName() + ChatDefault + " fell into Anarchy!");
            village = null;
            return;
        }
        
        File vill = new File(VillageUtils.getDataFolder() + "/data/villages/" + village.getName() + ".yml");
        vill.delete();
        VillageUtils.villages.remove(village);
        VillageBase.Broadcast(ChatDefault + "The Village " + ChatImportant + village.getName() + ChatDefault + " fell into Anarchy!");
        village = null;
    }
    
    public static boolean useEconomy() {
        if(config == null) {
            return false;
        }
        return config.getBoolean("economy");
    }
    
    public static double CreateVillageCost() {
        return config.getDouble("cost.createvillage");
    }
    
    public static List<World> getVillageWorlds() {
        List<World> worlds = new ArrayList<World>();
        for(String w : config.getStringList("Worlds")) {
            World world = Bukkit.getWorld(w);
            if(world == null) {
                continue;
            }
            worlds.add(world);
        }
        
        return worlds;
    }
}

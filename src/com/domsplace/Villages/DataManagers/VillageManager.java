package com.domsplace.Villages.DataManagers;

import com.domsplace.Villages.Bases.DataManagerBase;
import com.domsplace.Villages.Enums.ManagerType;
import com.domsplace.Villages.Events.VillageDeletedEvent;
import com.domsplace.Villages.Hooks.TagAPIHook;
import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Utils.VillageSQLUtils;
import com.domsplace.Villages.Utils.VillageScoreboardUtils;
import com.domsplace.Villages.Utils.VillageUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

public class VillageManager extends DataManagerBase {
    public VillageManager() {
        super(ManagerType.VILLAGE);
    }
    
    @Override
    public void tryLoad() throws IOException {
        VillageUtils.Villages = new ArrayList<Village>();
        
        List<String> vilNames = LoadVillageNames();
        for(String vn : vilNames) {
            Village v = LoadVillage(vn);
            getVillages().add(v);
        }
        VillageScoreboardUtils.SetupScoreboard();
    }
    
    @Override
    public void trySave() throws IOException {
        for(Village v : getVillages()) {
            getVillageManager().saveVillage(v);
        }
        VillageScoreboardUtils.SetupScoreboard();
    }
    
    public void saveVillage(Village village) {
        if(getConfigManager().useSQL) {
            saveVillageSQL(village);
        } else {
            saveVillageYML(village);
        }
        
        getVillageBankManager().saveVillageBank(village);
        
        for(OfflinePlayer p : village.getResidents()) {
            if(!p.isOnline()) {
                continue;
            }
            
            TagAPIHook.instance.refreshTags();
        }
        
        VillageScoreboardUtils.SetupScoreboard();
    }
    
    public boolean saveVillageYML(Village village) {
        try {
            trySaveVillageYML(village);
            return true;
        } catch (Exception ex) {
            Error("Failed to save village " + village.getName(), ex);
            return false;
        }
    }
    
    public void trySaveVillageYML(Village village) throws IOException {
        File dataFolder = new File(getDataFolder(), "data");
        if(!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        dataFolder = new File(dataFolder, "villages");
        if(!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        File villageFile = new File(dataFolder, village.getName() + ".yml");
        if(!villageFile.exists()) {
            villageFile.createNewFile();
        }

        YamlConfiguration configuration = villageToYML(village);
        configuration.save(villageFile);
    }
    
    public void saveVillageSQL(Village village) {
        int result = -1;
        //Try to get Village//
        result = VillageSQLUtils.getVillageIDByName(village.getName());

        if(result == -1) {
            String stmt = getCreateQuery(village);
            result = VillageSQLUtils.sqlQueryID(stmt);
            village.idSQL = result;
        } else {
            village.idSQL = result;
            String stmt = getUpdateQuery(village);
            if(stmt != null) {
                VillageSQLUtils.sqlQuery(stmt);
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
            int playerID = VillageSQLUtils.recordSQLPlayer(p);
            String stmt = "DELETE FROM `VillagesResidents` WHERE `VillagePlayerID` = '" + playerID + "';";
            VillageSQLUtils.sqlQuery(stmt);
            rstmt += "('" + playerID + "', '" + village.getSQLID() + "'), ";
        }
        rstmt = rstmt.substring(0, rstmt.length() - 2);
        rstmt += ";";
        VillageSQLUtils.sqlQuery(rstmt);
        
        //Save Plots
        rstmt = "DELETE FROM `VillagePlots` WHERE `VillageID`='" + village.idSQL + "';";
        VillageSQLUtils.sqlQuery(rstmt);
        
        //Add chunks, we'll run over them later to make the settings final
        String chunkstmt = "INSERT INTO `VillagePlots` ("
                + "`ChunkX`,"
                + "`ChunkZ`,"
                + "`ChunkWorld`,"
                + "`VillageID`"
                + ") VALUES ";
        
        String oldchunkstmt = chunkstmt;

        for(Chunk c :  village.getPlayerChunks().keySet()) {
            if(village.getTownSpawn().equals(c)) {
                continue;
            }

            chunkstmt += "('" + c.getX() + "', '" + c.getZ() + "', '" + c.getWorld().getName() + "', '" + village.idSQL + "'), ";
        }

        for(Chunk c :  village.getChunkPrices().keySet()) {
            if(village.getTownSpawn().equals(c)) {
                continue;
            }
            
            if(village.getPlayerChunks().containsKey(c)) {
                continue;
            }

            chunkstmt += "('" + c.getX() + "', '" + c.getZ() + "', '" + c.getWorld().getName() + "', '" + village.idSQL + "'), ";
        }
        if(!chunkstmt.equals(oldchunkstmt)) {
            chunkstmt = chunkstmt.substring(0, chunkstmt.length() - 2);
            chunkstmt += ";";
            
            VillageSQLUtils.sqlQuery(chunkstmt);

            for(Chunk c :  village.getPlayerChunks().keySet()) {
                if(village.getTownSpawn().equals(c)) {
                    continue;
                }
                
                VillageSQLUtils.sqlQuery(
                    "UPDATE `VillagePlots` SET `VillagePlayerID`='" + VillageSQLUtils.getSQLPlayerID(village.getPlayerFromChunk(c)) + "'"
                        + " WHERE `ChunkX`='" + c.getX() + "' "
                        + "AND `ChunkZ`='" + c.getZ() + "' "
                        + "AND `ChunkWorld`='" + c.getWorld().getName() + "'"
                        + "AND `VillageID`='" + village.idSQL + "';"
                );
            }

            for(Chunk c :  village.getChunkPrices().keySet()) {
                if(village.getTownSpawn().equals(c)) {
                    continue;
                }
                
                VillageSQLUtils.sqlQuery(
                    "UPDATE `VillagePlots` SET `ChunkCost`='" + village.getChunkPrice(c) + "'"
                        + " WHERE `ChunkX`='" + c.getX() + "' "
                        + "AND `ChunkZ`='" + c.getZ() + "' "
                        + "AND `ChunkWorld`='" + c.getWorld().getName() + "'"
                        + "AND `VillageID`='" + village.idSQL + "';"
                );
            }
        }
    }

    public static void deleteVillage(Village village) {
        VillageDeletedEvent event = new VillageDeletedEvent(village);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()) {
            return;
        }
        
        if(getConfigManager().useSQL) {
            int id = VillageSQLUtils.getVillageIDByName(village.getName());
            String stmt = "DELETE FROM `Villages` WHERE VillageID='" + id + "';";
            VillageSQLUtils.sqlQuery(stmt);
            
            stmt = "DELETE FROM `VillagesResidents` WHERE VillageID='" + id + "';";
            VillageSQLUtils.sqlQuery(stmt);
            
            stmt = "DELETE FROM `VillageBankItems` WHERE VillageID='" + id + "';";
            VillageSQLUtils.sqlQuery(stmt);
            
            stmt = "DELETE FROM `VillagePlots` WHERE `VillageID`='" + id+ "';";
            VillageSQLUtils.sqlQuery(stmt);
        } else {
            File vill = new File(getDataFolder() + "/data/villages/" + village.getName() + ".yml");
            vill.delete();
        }
        
        getVillages().remove(village);
        broadcast(gK("villageclosed", village));
        DataManagerBase.UPKEEP_MANAGER.deleteUpkeep(village);
        village = null;
        VillageScoreboardUtils.SetupScoreboard();
    }
    
    public static Village LoadVillageYML(String name) {
        Village village = null;
        
        try {
            File villageFile = new File(getPlugin().getDataFolder() + "/data/villages/" + name + ".yml");
            
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
            village.setTownSize(vil.getInt("size"));
            
            //Load In Plots
            if(vil.contains("plots")) {
                //Iterate over the plots
                for(String s : ((MemorySection) vil.get("plots")).getKeys(false)) {
                    Chunk c = Bukkit.getWorld(vil.getString("plots." + s + ".world")).getChunkAt(Integer.parseInt(vil.getString("plots." + s + ".x")), Integer.parseInt(vil.getString("plots." + s + ".z")));
                    if(c == null) {
                        continue;
                    }
                    
                    if(vil.contains("plots." + s + ".player")) {
                        OfflinePlayer p = Bukkit.getOfflinePlayer(vil.getString("plots." + s + ".player"));
                        village.forceClaim(p, c);
                    }
                    
                    if(vil.contains("plots." + s + ".price")) {
                        double d = vil.getDouble("plots." + s + ".price");
                        village.setChunkPrice(c, d);
                    }
                }
            }
            
            Chunk chunk = Bukkit.getWorld(vil.getString("townsquare.world")).getChunkAt(vil.getInt("townsquare.x"), vil.getInt("townsquare.z"));
            village.setTownSpawn(chunk);
            getVillageBankManager().loadVillageBankYML(village);
        } catch (Exception ex) {
            Error("Failed to load village!", ex);
            return null;
        }
        
        return village;
    }
    
    public static List<String> LoadVillageNames() {
        List<String> towns = new ArrayList<String>();
        if(getConfigManager().useSQL) {
            String stmt = "SELECT `VillageName` FROM `Villages` ORDER BY `VillageName` DESC;";
            List<Map<String, String>> results = VillageSQLUtils.sqlFetch(stmt);
            if(results == null) {
                return new ArrayList<String>();
            }
            for(Map<String, String> s : results) {
                towns.add(s.get("VillageName"));
            }
            return towns;
        }
        
        File villages = new File(getPlugin().getDataFolder() + "/data");
        if(!villages.exists()) {
            villages.mkdir();
        }
        villages = new File(getPlugin().getDataFolder() + "/data/villages/");
        if(!villages.exists()) {
            villages.mkdir();
        }
        
        ArrayList<String> names = new ArrayList<String>(Arrays.asList(villages.list()));
        for(String s : names) {
            towns.add(s.replaceAll(".yml", ""));
        }
        return towns;
    }
    
    public static Village LoadVillage(String name) {
        if(getConfigManager().useSQL) {
            return LoadVillageSQL(name);
        }
        
        return LoadVillageYML(name);
    }
    
    public static Village LoadVillageSQL(String name) {
        String stmt = "SELECT * FROM `Villages` WHERE `VillageName` LIKE '" + name + "' LIMIT 1;";
        List<Map<String, String>> results = VillageSQLUtils.sqlFetch(stmt);
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
            village.setCreatedDate(VillageSQLUtils.getSQLDate(vil.get("VillageCreateDate")).getTime());
            int x = Integer.parseInt(vil.get("VillageChunkX"));
            int z = Integer.parseInt(vil.get("VillageChunkZ"));
            Chunk chunk = Bukkit.getWorld(vil.get("VillageWorld")).getChunkAt(x, z);
            village.setTownSpawn(chunk);
            ArrayList<OfflinePlayer> players = getResidentsFromVillageSQL(Integer.parseInt(vil.get("VillageID")));
            if(players != null) {
                village.setResidents(players);
            }

            int id = Integer.parseInt(vil.get("VillageMayorID"));
            village.setMayor(VillageSQLUtils.getSQLPlayerByID(id));
            village.setMoney(Double.parseDouble(vil.get("VillageBank")));
            getVillageBankManager().loadVillageBankSQL(village);
            
            //Fixed Village size not being saved using SQL
            village.setTownSize(Integer.parseInt(vil.get("VillageSize")));
            
            //Load in Village Plots
            stmt = "SELECT * FROM `VillagePlots` WHERE `VillageID`='" + village.idSQL + "';";
            results = VillageSQLUtils.sqlFetch(stmt);
            if(results != null) {
                if(results.size() > 0) {
                    for(Map<String, String> cData : results) {
                        int chunkX = Integer.parseInt(cData.get("ChunkX"));
                        int chunkZ = Integer.parseInt(cData.get("ChunkZ"));
                        World chunkWorld = Bukkit.getWorld(cData.get("ChunkWorld"));
                        Chunk c = chunkWorld.getChunkAt(chunkX, chunkZ);

                        if(cData.containsKey("VillagePlayerID") && cData.get("VillagePlayerID") != null && !cData.get("VillagePlayerID").equalsIgnoreCase("null")) {
                            //Player is set for this chunk, let's set the owner
                            OfflinePlayer p = VillageSQLUtils.getSQLPlayerByID(Integer.parseInt(cData.get("VillagePlayerID")));
                            if(p != null) {
                                village.forceClaim(p, c);
                            }
                        }

                        if(cData.containsKey("ChunkCost") && cData.get("ChunkCost") != null && !cData.get("ChunkCost").equalsIgnoreCase("null")) {
                            double cost = Double.parseDouble(cData.get("ChunkCost"));
                            village.setChunkPrice(c, cost);
                        }
                    }
                }
            }
            
        } catch(Exception ex) {
            Error("Failed to load village!", ex);
            return null;
        }
        
        return village;
    }
    
    public static ArrayList<OfflinePlayer> getResidentsFromVillageSQL(int villageID) {
        String stmt = ""
                + "SELECT VillagesPlayers.VillagePlayerName FROM "
                + "`VillagesPlayers` INNER JOIN `VillagesResidents` ON VillagesResidents.VillagePlayerID = VillagesPlayers.VillagePlayerID "
                + "WHERE VillagesResidents.VillageID='" + villageID + "';";
        List<Map<String, String>> results = VillageSQLUtils.sqlFetch(stmt);
        
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
    
    public YamlConfiguration villageToYML(Village village) {
        YamlConfiguration yml = new YamlConfiguration();
        
        yml.set("name", village.getName());
        yml.set("description", village.getDescription());
        yml.set("mayor", village.getMayor().getName());
        
        List<String> names = new ArrayList<String>();
        for(OfflinePlayer p : village.getResidents()) {
            names.add(p.getName());
        }
        
        for(Chunk c : village.getChunkPrices().keySet()) {
            yml.set("plots." + c.getX() + "," + c.getZ() + ".price", village.getChunkPrices().get(c));
            yml.set("plots." + c.getX() + "," + c.getZ() + ".x", c.getX());
            yml.set("plots." + c.getX() + "," + c.getZ() + ".z", c.getZ());
            yml.set("plots." + c.getX() + "," + c.getZ() + ".world", c.getWorld().getName());
        }
        
        for(Chunk c : village.getPlayerChunks().keySet()) {
            if(c.equals(village.getTownSpawn())) {
                continue;
            }
            
            yml.set("plots." + c.getX() + "," + c.getZ() + ".player", village.getPlayerChunks().get(c).getName());
            yml.set("plots." + c.getX() + "," + c.getZ() + ".x", c.getX());
            yml.set("plots." + c.getX() + "," + c.getZ() + ".z", c.getZ());
            yml.set("plots." + c.getX() + "," + c.getZ() + ".world", c.getWorld().getName());
        }
        
        yml.set("residents", names);
        yml.set("createDate", village.getCreatedDate());
        yml.set("townsquare.x", village.getTownSpawn().getX());
        yml.set("townsquare.z", village.getTownSpawn().getZ());
        yml.set("townsquare.world", village.getTownSpawn().getWorld().getName());
        yml.set("bank", village.getItemBank().getItemsAsString());
        yml.set("size", village.getTownSize());
        yml.set("money", village.getMoney());
        
        return yml;
    }
    
    public String getUpdateQuery(Village village) {
        if(village.getSQLID() == -1) return null;
        
        String stmt = "UPDATE `Villages` SET "
                + "`VillageName` = '" + village.getName() + "', "
                + "`VillageDescription` = '" + village.getDescription() + "', "
                + "`VillageCreateDate` = '" + VillageSQLUtils.dateToSQL(new Date(village.getCreatedDate())) + "',"
                + "`VillageChunkX` = '" + village.getTownSpawn().getX() + "', "
                + "`VillageChunkZ` = '" + village.getTownSpawn().getZ() + "', "
                + "`VillageWorld` = '" + village.getTownSpawn().getWorld().getName() + "', "
                + "`VillageSize` = '" + village.getTownSize() + "', "
                + "`VillageBank` = '" + village.getMoney() + "', "
                + "`VillageMayorID` = '" + VillageSQLUtils.recordSQLPlayer(village.getMayor()) + "' "
                + "WHERE `VillageID` = '" + village.getSQLID() + "' LIMIT 1;";
        
        return stmt;
    }
    
    public String getCreateQuery(Village village) {
        String stmt = "INSERT INTO `Villages` ("
                + "`VillageName`,"
                + "`VillageDescription`,"
                + "`VillageCreateDate`,"
                + "`VillageChunkX`,"
                + "`VillageChunkZ`,"
                + "`VillageWorld`,"
                + "`VillageSize`,"
                + "`VillageBank`,"
                + "`VillageMayorID`"
                + ") VALUES ("
                + "'" + village.getName() + "',"
                + "'" + village.getDescription() + "',"
                + "'" + VillageSQLUtils.dateToSQL(new Date(village.getCreatedDate())) + "',"
                + "'" + village.getTownSpawn().getX() + "',"
                + "'" + village.getTownSpawn().getZ() + "',"
                + "'" + village.getTownSpawn().getWorld().getName() + "',"
                + "'" + village.getTownSize() + "',"
                + "'" + village.getMoney() + "',"
                + "'" + VillageSQLUtils.recordSQLPlayer(village.getMayor()) + "'"
                + ");";
        
        return stmt;
    }
}

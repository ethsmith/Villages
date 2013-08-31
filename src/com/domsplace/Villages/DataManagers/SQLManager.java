package com.domsplace.Villages.DataManagers;

import com.domsplace.Villages.Bases.DataManagerBase;
import com.domsplace.Villages.Enums.ManagerType;
import com.domsplace.Villages.Utils.VillageSQLUtils;
import com.domsplace.Villages.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SQLManager extends DataManagerBase {
    public SQLManager() {
        super(ManagerType.SQL);
    }
    
    @Override
    public boolean load() {
        String stmt = "CREATE TABLE IF NOT EXISTS `VillagesPlayers` ("
                + "`VillagePlayerID` int(11) NOT NULL AUTO_INCREMENT,"
                + "`VillagePlayerName` VARCHAR(200) NOT NULL,"
                + "CONSTRAINT VillagesPlayerPrimaryKey PRIMARY KEY (`VillagePlayerID`)"
                + ");";
        if(!VillageSQLUtils.sqlQuery(stmt)) {
            Utils.Error("Failed to create VillagesPlayers Table", null);
            return false;
        }
        
        stmt = "CREATE TABLE IF NOT EXISTS `Villages` ("
                + "`VillageID` int(11) NOT NULL AUTO_INCREMENT,"
                + "`VillageName` VARCHAR(100) NOT NULL,"
                + "`VillageDescription` VARCHAR(100) NOT NULL,"
                + "`VillageCreateDate` DATETIME NOT NULL,"
                + "`VillageChunkX` int(11) NOT NULL,"
                + "`VillageChunkZ` int(11) NOT NULL,"
                + "`VillageWorld` VARCHAR(255) NOT NULL,"
                + "`VillageSize` int(11) NOT NULL,"
                + "`VillageBank` DECIMAL(11, 4) NOT NULL,"
                + "`VillageMayorID` int(11) NOT NULL,"
                + "CONSTRAINT VillagesPrimaryKey PRIMARY KEY (`VillageID`)"
                + ");";
        if(!VillageSQLUtils.sqlQuery(stmt)) {
            Utils.Error("Failed to create Villages Table", null);
            return false;
        }
        
        //Villages version 1.16: Fix bans in SQL to be stored in a decimal.
        VillageSQLUtils.sqlQuery("ALTER TABLE `Villages` CHANGE `VillageBank` `VillageBank` DECIMAL( 11, 4 ) NOT NULL ");
        
        stmt = "CREATE TABLE IF NOT EXISTS `VillagesResidents` ("
                + "`VillagePlayerID` int(11) NOT NULL, "
                + "`VillageID` int(11) NOT NULL, "
                + "CONSTRAINT VillageResidentsPrimaryKey PRIMARY KEY (`VillagePlayerID`)"
                + ");";
        if(!VillageSQLUtils.sqlQuery(stmt)) {
            Utils.Error("Failed to create VillagesResidents Table", null);
            return false;
        }
        
        stmt = "CREATE TABLE IF NOT EXISTS `VillageBankItems` ("
                + "`VillageItemID` int(11) NOT NULL AUTO_INCREMENT,"
                + "`ItemID` int(6) NOT NULL,"
                + "`ItemData` int(6) NOT NULL,"
                + "`ItemAmount` int(4) NOT NULL,"
                + "`VillageID` int(11) NOT NULL,"
                + "CONSTRAINT VillageBankItemsPrimaryKey PRIMARY KEY (`VillageItemID`)"
                + ");";
        if(!VillageSQLUtils.sqlQuery(stmt)) {
            Utils.Error("Failed to create VillageBankItems Table", null);
            return false;
        }
        
        stmt = "CREATE TABLE IF NOT EXISTS `VillageBankItems` ("
                + "`VillageItemID` int(11) NOT NULL AUTO_INCREMENT,"
                + "`ItemID` int(6) NOT NULL,"
                + "`ItemData` int(6) NOT NULL,"
                + "`ItemAmount` int(4) NOT NULL,"
                + "`VillageID` int(11) NOT NULL,"
                + "CONSTRAINT VillageBankItemsPrimaryKey PRIMARY KEY (`VillageItemID`)"
                + ");";
        if(!VillageSQLUtils.sqlQuery(stmt)) {
            Utils.Error("Failed to create VillageBankItems Table", null);
            return false;
        }
        
        stmt = "CREATE TABLE IF NOT EXISTS `VillagePlots` ("
                + "`ChunkX` int(6) NOT NULL,"
                + "`ChunkZ` int(6) NOT NULL,"
                + "`VillagePlayerID` int(11) NULL,"
                + "`VillageID` int(11) NOT NULL,"
                + "`ChunkCost` varchar(255) NULL,"
                + "`ChunkWorld` varchar(255) NOT NULL,"
                + "CONSTRAINT VillagePlotsPrimaryKey PRIMARY KEY (ChunkX, ChunkZ, ChunkWorld, VillageID)"
                + ");";
        if(!VillageSQLUtils.sqlQuery(stmt)) {
            Utils.Error("Failed to create VillagePlots Table", null);
            return false;
        }
        
        for(Player p : Bukkit.getOnlinePlayers()) {
            VillageSQLUtils.recordSQLPlayer(p);
        }
        
        return true;
    }
}

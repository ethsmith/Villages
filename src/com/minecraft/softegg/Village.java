package com.minecraft.softegg;

import java.util.ArrayList;
import java.util.Date;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Village {
    private ArrayList<OfflinePlayer> residents;
    private OfflinePlayer mayor;
    private String name;
    private String description;
    private long createDate;
    private Chunk townSquare;
    private int size;
    private double money;
    public ArrayList<Player> sentWelcome;
    
    public int idSQL;
    
    public Village(String name) {
        this.name = name;
        
        this.setResidents(new ArrayList<OfflinePlayer>());
        
        sentWelcome = new ArrayList<Player>();
        
        this.setMayor(null);
        this.setTownSpawn(Bukkit.getWorlds().get(0).getSpawnLocation().getChunk());
        this.setCreatedDate(0);
        this.setTownSize(0);
        this.setDescription("");
        
        idSQL = -1;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Village setName(String name) {
        this.name = name;
        return this;
    }
    
    public Village setDescription(String description) {
        this.description = description;
        return this;
    }
    
    public int getSQLID() {
        return this.idSQL;
    }
    
    public Village setSQLID(int id) {
        this.idSQL = id;
        return this;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public OfflinePlayer getMayor() {
        return this.mayor;
    }
    
    public Village setMayor(OfflinePlayer mayor) {
        this.mayor = mayor;
        if(mayor != null && !this.getResidents().contains(mayor)) {
            this.addResident(mayor);
        }
        return this;
    }
    
    public Village setResidents(ArrayList<OfflinePlayer> residents) {
        this.residents = residents;
        return this;
    }
    
    public ArrayList<OfflinePlayer> getResidents() {
        return this.residents;
    }
    
    public Village addResident(OfflinePlayer resident) {
        this.getResidents().add(resident);
        return this;
    }
    
    public Village removeResident(OfflinePlayer resident) {
        this.getResidents().remove(resident);
        return this;
    }
    
    public Boolean isResident(OfflinePlayer resident) {
        if(!this.getResidents().contains(resident)) {
            return false;
        }
        return true;
    }
    
    public boolean isMayor (OfflinePlayer mayor) {
        if(this.getMayor() != mayor) {
            return false;
        }
        return true;
    }
    
    public Chunk getTownSpawn() {
        return this.townSquare;
    }
    
    public Village setTownSpawn(Chunk spawn) {
        this.townSquare = spawn;
        return this;
    }
    
    public ArrayList<Chunk> getTownChunks() {
        int x = this.getTownSpawn().getX();
        int z = this.getTownSpawn().getZ();
        
        ArrayList<Chunk> chunks = new ArrayList<Chunk>();
        
        for(int cx = (x - this.size); cx <= (x + this.size); cx++) {
            for(int cz = (z - this.size); cz <= (z + this.size); cz++) {
                Chunk chunk = townSquare.getWorld().getChunkAt(cx, cz);
                if(chunk == null) {
                    continue;
                }
                chunks.add(chunk);
            }
        }
        
        return chunks;
    }
    
    public ArrayList<Chunk> getTownBorderChunks() {
        int x = this.getTownSpawn().getX();
        int z = this.getTownSpawn().getZ();
        
        ArrayList<Chunk> chunks = new ArrayList<Chunk>();
        ArrayList<Chunk> town = this.getTownChunks();
        
        for(int cx = (x - this.size - VillageDataManager.config.getInt("townborder")); cx <= (x + this.size + VillageDataManager.config.getInt("townborder")); cx++) {
            for(int cz = (z - this.size - VillageDataManager.config.getInt("townborder")); cz <= (z + this.size + VillageDataManager.config.getInt("townborder")); cz++) {
                Chunk chunk = townSquare.getWorld().getChunkAt(cx, cz);
                if(chunk == null) {
                    continue;
                }
                if(town.contains(chunk)) {
                    continue;
                }
                chunks.add(chunk);
            }
        }
        
        return chunks;
    }
    public ArrayList<Chunk> getTownArea() {
        int x = this.getTownSpawn().getX();
        int z = this.getTownSpawn().getZ();
        
        ArrayList<Chunk> chunks = new ArrayList<Chunk>();
        
        for(int cx = (x - this.size - VillageDataManager.config.getInt("townborder")); cx <= (x + this.size + VillageDataManager.config.getInt("townborder")); cx++) {
            for(int cz = (z - this.size - VillageDataManager.config.getInt("townborder")); cz <= (z + this.size + VillageDataManager.config.getInt("townborder")); cz++) {
                Chunk chunk = townSquare.getWorld().getChunkAt(cx, cz);
                if(chunk == null) {
                    continue;
                }
                chunks.add(chunk);
            }
        }
        
        return chunks;
    }
    
    public boolean isChunkInTownArea(Chunk chunk) {
        for(Chunk c : this.getTownArea()) {
            if(c != chunk) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    public long getCreatedDate() {
        return this.createDate;
    }
    
    public Village setCreatedDate(long createDate) {
        this.createDate = createDate;
        return this;
    }
    
    public int getTownSize() {
        return this.size;
    }
    
    public Village setTownSize(int size) {
        this.size = size;
        return this;
    }
    
    public Village setMoney(double money) {
        this.money = money;
        return this;
    }
    
    public double getMoney() {
        return this.money;
    }
    
    public Village addMoney(double amount) {
        return this.setMoney(this.getMoney() + amount);
    }
    
    public YamlConfiguration getTownAsYML() {
        YamlConfiguration yml = new YamlConfiguration();
        
        yml.set("name", this.getName());
        yml.set("description", this.getDescription());
        yml.set("mayor", this.getMayor().getName());
        
        ArrayList<String> names = new ArrayList<String>();
        for(OfflinePlayer p : this.getResidents()) {
            names.add(p.getName());
        }
        yml.set("residents", names);
        yml.set("createDate", this.getCreatedDate());
        yml.set("townsquare.x", this.getTownSpawn().getX());
        yml.set("townsquare.z", this.getTownSpawn().getZ());
        yml.set("townsquare.world", this.getTownSpawn().getWorld().getName());
        yml.set("size", this.getTownSize());
        yml.set("money", this.money);
        
        return yml;
    }
    
    public String getUpdateQuery() {
        if(this.getSQLID() == -1) {
            return null;
        }
        
        String stmt = "UPDATE `Villages` SET "
                + "`VillageName` = '" + this.getName() + "', "
                + "`VillageDescription` = '" + this.getDescription() + "', "
                + "`VillageCreateDate` = '" + VillageUtils.dateToSQL(new Date(this.createDate)) + "',"
                + "`VillageChunkX` = '" + this.getTownSpawn().getX() + "', "
                + "`VillageChunkZ` = '" + this.getTownSpawn().getZ() + "', "
                + "`VillageWorld` = '" + this.getTownSpawn().getWorld().getName() + "', "
                + "`VillageSize` = '" + this.getTownSize() + "', "
                + "`VillageBank` = '" + this.money + "', "
                + "`VillageMayorID` = '" + VillageDataManager.dataManager.recordSQLPlayer(mayor) + "' "
                + "WHERE `VillageID` = '" + this.getSQLID() + "' LIMIT 1;";
        
        return stmt;
    }
    
    public String getCreateQuery() {
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
                + "'" + this.getName() + "',"
                + "'" + this.getDescription() + "',"
                + "'" + VillageUtils.dateToSQL(new Date(this.createDate)) + "',"
                + "'" + this.getTownSpawn().getX() + "',"
                + "'" + this.getTownSpawn().getZ() + "',"
                + "'" + this.getTownSpawn().getWorld().getName() + "',"
                + "'" + this.getTownSize() + "',"
                + "'" + this.money + "',"
                + "'" + VillageDataManager.dataManager.recordSQLPlayer(mayor) + "'"
                + ");";
        
        return stmt;
    }

    public boolean isChunkInTown(Chunk chunk) {
        if(!this.getTownChunks().contains(chunk)) {
            return false;
        }
        return true;
    }

    public void SendMessage(String string) {
        for(OfflinePlayer p : this.getResidents()) {
            if(!p.isOnline()) {
                continue;
            }
            
            p.getPlayer().sendMessage(string);
        }
    }
    
    public Location getSpawnBlock() {
        //Get Middle block, 8 / 8
        int y = 64;
        
        Block b = this.getTownSpawn().getBlock(8, y, 8);
        while(b.getType() != Material.AIR) {
            b = b.getRelative(0, 1, 0);
        }
        
        return b.getLocation();
    }
}

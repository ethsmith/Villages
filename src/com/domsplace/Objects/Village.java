package com.domsplace.Objects;

import com.domsplace.Utils.VillageSQLUtils;
import com.domsplace.Utils.VillageScoreboardUtils;
import com.domsplace.Utils.VillageUtils;
import com.domsplace.Utils.VillageVillagesUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    public VillageItemBank itemBank;
    
    public ArrayList<Player> sentWelcome;
    
    private HashMap<Chunk, OfflinePlayer> playerPlots;
    private HashMap<Chunk, Double> plotPrices;
    
    public int idSQL;
    
    public Village(String name) {
        this.name = name;
        Init();
    }
    
    private void Init() {
        this.setResidents(new ArrayList<OfflinePlayer>());
        
        sentWelcome = new ArrayList<Player>();
        
        this.setMayor(null);
        this.setTownSpawn(Bukkit.getWorlds().get(0).getSpawnLocation().getChunk());
        this.setCreatedDate(0);
        this.setTownSize(0);
        this.setDescription("");
        
        this.itemBank = new VillageItemBank(this.getName());
        
        idSQL = -1;
        
        VillageScoreboardUtils.SetupScoreboard();
        
        playerPlots = new HashMap<Chunk, OfflinePlayer>();
        plotPrices = new HashMap<Chunk, Double>();
    }
    
    public String getName() {
        return this.name;
    }
    
    public Village setName(String name) {
        this.name = name;
        this.itemBank.setName(this.getName());
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
        
        if(VillageUtils.useTagAPI && resident.isOnline()) {
            VillageUtils.refreshTags(resident.getPlayer());
        }
        
        VillageScoreboardUtils.SetupScoreboard();
        return this;
    }
    
    public Village removeResident(OfflinePlayer resident) {
        this.getResidents().remove(resident);
        VillageScoreboardUtils.SetupScoreboard();
        
        this.removePlayerChunks(resident);
        
        return this;
    }
    
    public Boolean isResident(OfflinePlayer resident) {
        if(this.getResidents() == null) {
            return false;
        }
        for(OfflinePlayer p : this.getResidents()) {
            if(p.getName().equalsIgnoreCase(resident.getName())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isMayor (OfflinePlayer mayor) {
        if(!this.getMayor().getName().equalsIgnoreCase(mayor.getName())) {
            return false;
        }
        return true;
    }
    
    public Chunk getTownSpawn() {
        return this.townSquare;
    }
    
    public Village setTownSpawn(Chunk spawn) {
        this.townSquare = spawn;
        
        //Reserve the block//
        if(this.getMayor() != null) {
            this.forceClaim(this.getMayor(), this.getTownSpawn());
        }
        
        return this;
    }
    
    public ArrayList<Chunk> getTownChunks() {
        int x = this.getTownSpawn().getX();
        int z = this.getTownSpawn().getZ();
        
        ArrayList<Chunk> chunks = new ArrayList<Chunk>();
        
        int s = this.size - 1;
        
        for(int cx = (x - s); cx <= (x + s); cx++) {
            for(int cz = (z - s); cz <= (z + s); cz++) {
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
        
        int s = this.size - 1;
        
        int r = VillageVillagesUtils.borderRadius;
        
        for(int cx = (x - s - r); cx <= (x + s + r); cx++) {
            for(int cz = (z - s - r); cz <= (z + s + r); cz++) {
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
    
    public ArrayList<Chunk> getTownLocalBorderChunks(int amount) {
        int x = this.getTownSpawn().getX();
        int z = this.getTownSpawn().getZ();
        
        ArrayList<Chunk> chunks = new ArrayList<Chunk>();
        ArrayList<Chunk> town = this.getTownChunks();
        
        int s = this.size - 1;
        int r = amount;
        
        for(int cx = (x - s - r); cx <= (x + s + r); cx++) {
            for(int cz = (z - s - r); cz <= (z + s + r); cz++) {
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
        
        int s = this.size - 1;
        int r = VillageVillagesUtils.borderRadius;
        
        for(int cx = (x - s - r); cx <= (x + s + r); cx++) {
            for(int cz = (z - s - r); cz <= (z + s + r); cz++) {
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
        
        for(Chunk c : this.plotPrices.keySet()) {
            yml.set("plots." + c.getX() + "," + c.getZ() + ".price", this.plotPrices.get(c));
            yml.set("plots." + c.getX() + "," + c.getZ() + ".x", c.getX());
            yml.set("plots." + c.getX() + "," + c.getZ() + ".z", c.getZ());
            yml.set("plots." + c.getX() + "," + c.getZ() + ".world", c.getWorld().getName());
        }
        
        for(Chunk c : this.playerPlots.keySet()) {
            if(c.equals(this.getTownSpawn())) {
                continue;
            }
            
            yml.set("plots." + c.getX() + "," + c.getZ() + ".player", this.playerPlots.get(c).getName());
            yml.set("plots." + c.getX() + "," + c.getZ() + ".x", c.getX());
            yml.set("plots." + c.getX() + "," + c.getZ() + ".z", c.getZ());
            yml.set("plots." + c.getX() + "," + c.getZ() + ".world", c.getWorld().getName());
        }
        
        yml.set("residents", names);
        yml.set("createDate", this.getCreatedDate());
        yml.set("townsquare.x", this.getTownSpawn().getX());
        yml.set("townsquare.z", this.getTownSpawn().getZ());
        yml.set("townsquare.world", this.getTownSpawn().getWorld().getName());
        yml.set("bank", this.getItemBank().getItemsAsString());
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
                + "`VillageCreateDate` = '" + VillageSQLUtils.dateToSQL(new Date(this.createDate)) + "',"
                + "`VillageChunkX` = '" + this.getTownSpawn().getX() + "', "
                + "`VillageChunkZ` = '" + this.getTownSpawn().getZ() + "', "
                + "`VillageWorld` = '" + this.getTownSpawn().getWorld().getName() + "', "
                + "`VillageSize` = '" + this.getTownSize() + "', "
                + "`VillageBank` = '" + this.money + "', "
                + "`VillageMayorID` = '" + VillageSQLUtils.recordSQLPlayer(mayor) + "' "
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
                + "'" + VillageSQLUtils.dateToSQL(new Date(this.createDate)) + "',"
                + "'" + this.getTownSpawn().getX() + "',"
                + "'" + this.getTownSpawn().getZ() + "',"
                + "'" + this.getTownSpawn().getWorld().getName() + "',"
                + "'" + this.getTownSize() + "',"
                + "'" + this.money + "',"
                + "'" + VillageSQLUtils.recordSQLPlayer(mayor) + "'"
                + ");";
        
        return stmt;
    }

    public boolean isChunkInTown(Chunk chunk) {
        if(!this.getTownChunks().contains(chunk)) {
            return false;
        }
        return true;
    }

    public Village SendMessage(String string) {
        for(OfflinePlayer p : this.getResidents()) {
            if(!p.isOnline()) {
                continue;
            }
            
            VillageUtils.msgPlayer(p.getPlayer(), string);
        }
        return this;
    }
    
    public Location getSpawnBlock() {
        //Get Middle block, 8 / 8
        int y = 256;
        
        Block b = this.getTownSpawn().getBlock(8, y, 8);
        Block below = this.getTownSpawn().getBlock(8, y-1, 8);
        Block up = this.getTownSpawn().getBlock(8, y+1, 8);
        Block d = this.getTownSpawn().getBlock(8, 64, 8);
        
        boolean look = true;
        
        while(look) {
            below = b.getRelative(0, -1, 0);
            up = b.getRelative(0, 1, 0);
            
            if(b == null) {
                return d.getLocation();
            }
            
            if(below == null) {
                return d.getLocation();
            }
            
            if(up == null) {
                return d.getLocation();
            }
            
            if(below.getY() <= 0) {
                return d.getLocation();
            }
            
            if(b.getType().equals(Material.AIR) && up.getType().equals(Material.AIR) && !below.getType().equals(Material.AIR)) {
                return b.getLocation();
            }
            
            b = b.getRelative(0, -1, 0);
        }
        
        return b.getLocation();
    }

    public double getWorth() {
        double worth = this.getTownSize() * 10;
        if(VillageUtils.useEconomy) {
            worth += this.getMoney();
        }
        
        return worth;
    }
    
    public Village save() {
        VillageVillagesUtils.SaveVillage(this);
        return this;
    }
    
    public VillageItemBank getItemBank() {
        return this.itemBank;
    }
    
    public Chunk getLowestChunk() {
        Chunk lowestChunk = this.getTownSpawn();
        
        for(Chunk c : this.getTownArea()) {
            if(c.getX() >= lowestChunk.getX()) {
                continue;
            }
            
            lowestChunk = c;
        }
        
        return lowestChunk;
    }
    
    public Chunk getHighestChunk() {
        Chunk chunk = this.getTownSpawn();
        
        for(Chunk c : this.getTownArea()) {
            if(c.getX() < chunk.getX()) {
                continue;
            }
            
            chunk = c;
        }
        
        return chunk;
    }
    
    public OfflinePlayer getPlayerFromChunk(Chunk c) {
        return this.playerPlots.get(c);
    }
    
    public boolean isChunkClaimed(Chunk c) {
        return (this.getPlayerFromChunk(c)!=null);
    }
    
    public boolean isChunkOwnedByPlayer(OfflinePlayer p, Chunk c) {
        return this.getPlayerFromChunk(c).equals(p);
    }
    
    public ArrayList<Chunk> getPlayersChunks(OfflinePlayer p) {
        ArrayList<Chunk> chunks = new ArrayList<Chunk>();
        
        for(Chunk c : this.playerPlots.keySet()) {
            if(!this.getPlayerFromChunk(c).getName().equalsIgnoreCase(p.getName())) {
                continue;
            }
            chunks.add(c);
        }
        
        return chunks;
    }
    
    public HashMap<Chunk, OfflinePlayer> getPlayerChunks() {
        return this.playerPlots;
    }
    
    public boolean ClaimChunk(OfflinePlayer p, Chunk c) {
        if(this.isChunkClaimed(c)) {
            return false;
        }
        
        this.getPlayerChunks().put(c, p);
        return this.isChunkOwnedByPlayer(p, c);
    }
    
    public Village forceClaim(OfflinePlayer p, Chunk c) {
        this.getPlayerChunks().put(c, p);
        return this;
    }
    
    public HashMap<Chunk, Double> getChunkPrices() {
        return this.plotPrices;
    }
    
    public boolean isPriceSet(Chunk c) {
        return this.getChunkPrices().containsKey(c);
    }
    
    public double getChunkPrice(Chunk chunk) {
        
        double cost = 0.0d;
        if(this.getChunkPrices().containsKey(chunk)) {
            cost = this.getChunkPrices().get(chunk);
        }
        return cost;
    }
    
    public Village setChunkPrice(Chunk c, double d) {
        this.getChunkPrices().put(c, d);
        return this;
    }

    public Village removePlayerChunks(OfflinePlayer resident) {
        for(Chunk c : this.getPlayersChunks(Bukkit.getOfflinePlayer(resident.getName()))) {
            this.playerPlots.remove(c);
        }
        return this;
    }
}

package com.domsplace.Villages.Objects;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.DataManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Village {
    private static final List<Village> VILLAGES = new ArrayList<Village>();
    
    public static void registerVillage(Village village) {
        VILLAGES.add(village);
    }
    
    public static void  deRegisterVillage(Village village) {
        VILLAGES.remove(village);
        village.getBank().updateGUI();
    }
    
    public static List<Village> getVillages() {
        return new ArrayList<Village>(VILLAGES);
    }

    public static List<String> getVillageNames() {
        List<String> rv = new ArrayList<String>();
        for(Village v : VILLAGES) {
            if(v == null) continue;
            rv.add(v.getName());
        }
        return rv;
    }
    
    public static Village getPlayersVillage(Resident player) {
        if(player == null) return null;
        for(Village v : VILLAGES) {
            if(v.getMayor() == null) continue;
            if(v.isMayor(player) || v.isResident(player)) return v;
        }
        return null;
    }
    
    public static boolean doesRegionOverlapVillage(Region region) {
        return getOverlappingVillage(region) != null;
    }
    
    public static Village getOverlappingVillage(Region region) {
        for(Village v : VILLAGES) {
            if(!v.isRegionOverlappingVillage(region)) continue;
            return v;
        }
        
        return null;
    }

    public static Village getVillage(String name) {
        for(Village v : VILLAGES) {
            if(v.getName().equalsIgnoreCase(name)) return v;
        }
        return null;
    }
    
    public static void deleteVillage(Village village) {
        DataManager.VILLAGE_MANAGER.deleteVillage(village);
    }

    public static void deRegisterVillages(List<Village> villages) {
        for(Village v : villages) {
            Village.deRegisterVillage(v);
        }
    }
    
    //Instance
    private String name;
    private String description = "Welcome!";
    private long createdDate;
    
    private Resident mayor;
    private Bank bank;
    private Region spawn;
    
    private List<Region> regions;
    private List<Plot> plots;
    private List<Resident> residents;
    private List<TaxData> taxData;
    
    public Village() {
        this.bank = new Bank(this);
        this.plots = new ArrayList<Plot>();
        this.regions = new ArrayList<Region>();
        this.residents = new ArrayList<Resident>();
        this.taxData = new ArrayList<TaxData>();
        this.createdDate = Base.getNow();
    }
    
    public String getName() {return this.name;}
    public String getDescription() {return this.description;}
    public Resident getMayor() {return this.mayor;}
    public Region getSpawn() {return this.spawn;}
    public Bank getBank() {return this.bank;}
    public long getCreatedDate() {return this.createdDate;}
    
    public List<Region> getRegions() {return new ArrayList<Region>(this.regions);}
    public List<Plot> getPlots() {return new ArrayList<Plot>(this.plots);}
    public List<Resident> getResidents() {return new ArrayList<Resident>(this.residents);}
    public List<TaxData> getTaxData() {return new ArrayList<TaxData>(this.taxData);}
    
    public void setName(String name) {this.name = name; this.bank.updateGUI();}
    public void setDescription(String description) {this.description = description;}
    public void setMayor(Resident mayor) {this.mayor = mayor; this.addResident(mayor);}
    public void setSpawn(Region region) {this.addRegion(region); this.spawn = region;}
    public void setCreatedDate(long date) {this.createdDate = date;}
    
    public void addRegion(Region region) {if(this.regions.contains(region)) {return;} this.regions.add(region);}
    public void addPlot(Plot plot) {this.plots.add(plot);}
    public void addResident(Resident resident) {if(this.residents.contains(resident)){return;} this.residents.add(resident);}
    public void addTaxData(TaxData data) {this.taxData.add(data);}
    
    public void removeResident(Resident resident) {this.residents.remove(resident);}

    public boolean isMayor(Resident player) {if(player == null) return false; return this.mayor.equals(player); }
    public boolean isResident(Resident player) {if(player == null) return false; return this.residents.contains(player) || this.isMayor(player);}
    
    public List<Plot> getAvailablePlots() {
        List<Plot> list = new ArrayList<Plot>();
        for(Plot p : this.plots) {
            if(p.isOwned()) continue;
            list.add(p);
        }
        return list;
    }
    
    public Region getOverlappingRegion(Region region) {
        for(Region r : this.regions) {
            if(!r.compare(region)) continue;
            return r;
        }
        return null;
    }
    
    public boolean isRegionOverlappingVillage(Region region) {
        return this.getOverlappingRegion(region) != null;
    }

    public List<String> getResidentsAsString() {
        List<String> res = new ArrayList<String>();
        for(Resident r : this.residents) {
            res.add(r.getName());
        }
        return res;
    }

    public List<String> getRegionsAsString() {
        List<String> regions = new ArrayList<String>();
        for(Region r : this.regions) {
            regions.add(r.toString());
        }
        return regions;
    }
    
    public List<Player> getOnlineResidents() {
        List<Player> players = new ArrayList<Player>();
        for(Resident r : this.residents) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(r.getName());
            if(!p.isOnline()) continue;
            players.add(p.getPlayer());
        }
        return players;
    }
    
    public void broadcast(Player[] ignoredPlayers, Object... o) {
        List<Player> players = this.getOnlineResidents();
        for(Player p : ignoredPlayers) {
            if(players.contains(p)) players.remove(p);
        }
        
        Base.sendAll(players, o);
    }

    public void broadcast(Object... o) {
        this.broadcast(new Player[]{}, o);
    }
    
    public boolean doesRegionBorder(Region r) {
        for(Region re : this.regions) {
            if(re.doesRegionBorder(r)) return true;
        }
        
        return false;
    }

    public void addRegions(Collection<Region> claiming) {
        this.regions.addAll(claiming);
    }
    
    public void explode() {
        //WARNING! Can be CPU intensive
        List<Block> explode = new ArrayList<Block>();
        for(Region r : this.regions) {
            Block x = r.getHighBlock();
            Block y = r.getSafeMiddle().getBlock();
            Block z = r.getLowBlock();
            explode.add(x);
            explode.add(y);
            explode.add(z);
        }
        
        for(Block b : explode) {
            //Create Explosion at 6F (1.5x a regular TNT)
            b.getWorld().createExplosion(b.getLocation(), 6f);
            b.getRelative(0, 30, 0).getWorld().createExplosion(b.getLocation(), 6f);
            b.getRelative(0, -30, 0).getWorld().createExplosion(b.getLocation(), 6f);
        }
    }

    public int getValue() {
        int v = this.getRegions().size();
        v += this.residents.size();
        if(Base.useEconomy) v += this.getBank().getWealth();
        //TODO: Add ItemBank stuff here
        return v;
    }

    public Plot getPlot(Region standing) {
        for(Plot p : this.plots) {
            if(p.getRegion().compare(standing)) return p;
        }
        return null;
    }
    
    public void delete() {
        this.bank.delete();
    }

    public TaxData getTaxData(Tax t) {
        for(TaxData td : this.taxData) {
            if(td.getTax().equals(t)) return td;
        }
        
        return null;
    }
}

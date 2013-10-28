package com.domsplace.Villages.Objects;

import com.domsplace.Villages.Bases.Base;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Resident {
    private static final List<Resident> RESIDENTS = new ArrayList<Resident>();
    
    public static Resident registerResident(OfflinePlayer player) {
        Resident r = new Resident(player.getName());
        RESIDENTS.add(r);
        return r;
    }
    
    public static Resident getResident(Player player) {
        return getResident(Base.getOfflinePlayer(player));
    }
    
    public static Resident getResident(String player) {
        if(player == null) return null;
        return getResident(Base.getOfflinePlayer(player));
    }
    
    public static Resident getResident(OfflinePlayer player) {
        for(Resident r : RESIDENTS) {
            if(r.getName().equalsIgnoreCase(player.getName())) return r;
        }
        
        return registerResident(player);
    }
    
    public static Resident guessResident(String string) {
        for(Resident r : RESIDENTS) {
            if(r.getName().toLowerCase().startsWith(string.toLowerCase())) return r;
        }
        
        return null;
    }

    public static List<Resident> getRegisteredResidents() {
        return new ArrayList<Resident>(RESIDENTS);
    }
    
    //Instance
    private String player;
    
    private Resident(String player) {
        this.player = player;
    }
    
    public String getName() {return this.player;}
    public OfflinePlayer getOfflinePlayer() {return Bukkit.getOfflinePlayer(this.player);}
    public Player getPlayer() {return this.getOfflinePlayer().getPlayer();}

    public void teleport(Region spawn) {
        this.getPlayer().teleport(spawn.getSafeMiddle());
    }
}

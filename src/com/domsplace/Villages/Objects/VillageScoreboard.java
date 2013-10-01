package com.domsplace.Villages.Objects;

import com.domsplace.Villages.Bases.Base;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class VillageScoreboard {
    private static final List<VillageScoreboard> SCOREBOARDS = new ArrayList<VillageScoreboard>();
    
    public static VillageScoreboard getScoreboard(Player player) {return getScoreboard(Bukkit.getOfflinePlayer(player.getName()));}
    
    public static VillageScoreboard getScoreboard(OfflinePlayer player) {
        for(VillageScoreboard sc : SCOREBOARDS) {
            if(sc.player.getName().equalsIgnoreCase(player.getName())) return sc;
        }
        
        return new VillageScoreboard(player);
    }
    
    public static void deRegister(VillageScoreboard vsc) {
        SCOREBOARDS.remove(vsc);
    }
    
    //Instance
    private Scoreboard sc;
    private OfflinePlayer player;
    private Objective objective;
    private List<Score> scores;
    private String name;
    
    private VillageScoreboard(OfflinePlayer player) {
        this.player = player;
        this.name = "";
        
        this.sc = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = this.sc.registerNewObjective("VillageList", "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        
        this.scores = new ArrayList<Score>();
        
        this.register();
    }
    
    public void setName(String name) {this.name = name; this.objective.setDisplayName(name);}
    
    public void reset() {
        for(Score s : scores) {
            this.sc.resetScores(s.getPlayer());
        }
    }
    
    public void addScore(String name, int score) {
        name = Base.trim(name, 16);
        Score sc = this.objective.getScore(Bukkit.getOfflinePlayer(name));
        sc.setScore(score);
        this.scores.add(sc);
    }
    
    private void register() {SCOREBOARDS.add(this);}
    public void setPlayer() {
        if(!this.player.isOnline()) return;
        if(!Base.inVillageWorld(player.getPlayer())) return;
        try {this.player.getPlayer().setScoreboard(sc);} catch(Exception e) {return;}
    }
    
    
}
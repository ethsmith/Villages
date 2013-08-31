package com.domsplace.Villages.Utils;

import com.domsplace.Villages.Objects.Village;
import static com.domsplace.Villages.Bases.Base.ChatImportant;
import com.domsplace.Villages.Bases.UtilsBase;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class VillageScoreboardUtils extends UtilsBase {
    public static Scoreboard sb;
    
    public static void SetupScoreboard() {
        ScoreboardManager sbManager = Bukkit.getScoreboardManager();
        
        if(!getConfig().getBoolean("colors.ingamelist")) {
            if(sb != null) {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    if(!VillageUtils.isVillageWorld(p.getWorld())) {
                        continue;
                    }

                    if(p.getScoreboard() != sb) {
                        continue;
                    }

                    p.setScoreboard(sbManager.getNewScoreboard());
                }
            }
            return;
        }
        
        //Reset old Scoreboards
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!VillageUtils.isVillageWorld(p.getWorld())) {
                continue;
            }
            
            if(sb == null || p.getScoreboard() != sb) {
                continue;
            }
            
            p.setScoreboard(sbManager.getNewScoreboard());
        }
        
        sb = sbManager.getNewScoreboard();
        
        //Set Scoreboards for all players
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(sb);
        }
        
        Objective villages = sb.registerNewObjective("VillageList", "dummy");
        villages.setDisplaySlot(DisplaySlot.SIDEBAR);
        villages.setDisplayName(ChatImportant + " = §bTop Villages " + ChatImportant + "= ");
        
        //Create villages
        for(Village v : VillageUtils.getVillages()) {
            OfflinePlayer pl = Bukkit.getOfflinePlayer(v.getName());
            sb.resetScores(pl);
            Score score = villages.getScore(pl);
            score.setScore(v.getResidents().size());
        }
    }
}

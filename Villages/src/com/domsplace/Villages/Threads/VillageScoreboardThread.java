package com.domsplace.Villages.Threads;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.VillageThread;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Tax;
import com.domsplace.Villages.Objects.TaxData;
import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Objects.VillageScoreboard;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VillageScoreboardThread extends VillageThread {
    private String current = "";
    
    public VillageScoreboardThread() {
        super(10, Base.getConfig().getInt("features.cyclespeed", 60));
    }
    
    @Override
    public void run() {
        if(!Base.useScoreboards) return;
        this.current = this.getNext();
        
        List<Village> dontmod = Village.getVillages();
        List<Village> copy = new ArrayList<Village>(dontmod);
        List<Village> top = new ArrayList<Village>();
        for(int i = 0; i < 5; i++) {
            Village best = null;
            for(Village vil : dontmod) {
                if(!copy.contains(vil)) continue;
                if(best == null) {
                    best = vil;
                    continue;
                }

                if(vil.getResidents().size() > best.getResidents().size()) {
                    best = vil;
                }
            }
            if(best == null) continue;
            top.add(best);
            copy.remove(best);
        }
        
        for(Player p : Bukkit.getOnlinePlayers()) {
            VillageScoreboard v = VillageScoreboard.getScoreboard(p);
            v.setPlayer();
            v.reset();
            
            if(current.equals("top")) {
                v.setName(Base.colorise(Base.trim(Base.gk("topvillageslist").get(0), 32)));
                
                int i = 1;
                for(Village vil : top) {
                    v.addScore("#" + i + " " + vil.getName(), vil.getResidents().size());
                    i++;
                }
            }
            
            if(current.equals("mem")) {
                v.setName(colorise(trim(Base.gk("residentslist").get(0), 32)));
                
                Village pvil = Village.getPlayersVillage(Resident.getResident(p));
                if(pvil == null) {
                    v.addScore(Base.Wilderness, 1);
                } else {
                    int i = 1;
                    v.addScore(ChatImportant + pvil.getMayor().getName(), i);
                    for(Resident r : pvil.getResidents()) {
                        if(pvil.isMayor(r)) continue;
                        i++;
                        v.addScore(r.getName(), i);
                    }
                }
            }
            
            if(current.equals("tax")) {
                v.setName(colorise(trim(Base.gk("taxeslist").get(0), 32)));
                
                Village vil = Village.getPlayersVillage(Resident.getResident(p));
                if(vil == null) {
                    v.addScore(gk("taxeslistnotvillage").get(0), 1);
                } else {
                    for(Tax t : Tax.getTaxes()) {
                        TaxData td = vil.getTaxData(t);
                        if(td == null) {
                            td = new TaxData(vil, t);
                            vil.addTaxData(td);
                        }
                        long due = td.getLastChecked() + (long)(t.getHours() * 3600000d);
                        
                        int diff = Math.round((due - getNow())/3600000);
                        v.addScore(t.getName(), diff);
                    }
                }
            }
            
            //Next Player
        }
    }
    
    private String getNext() {
        boolean vil = Base.getConfig().getBoolean("features.lists.topvillages", true);
        boolean mem = Base.getConfig().getBoolean("features.lists.villagemembers", true);
        boolean tax = Base.getConfig().getBoolean("features.lists.taxday", true);
        
        List<String> ss = new ArrayList<String>();
            
        if(vil) ss.add("top");
        if(mem) ss.add("mem");
        if(tax) ss.add("tax");
        
        String c = ss.get(0);
        
        if(ss.size() > 1 && ss.contains(current)) {
            int f = -1;
            for(int i = 0; i < ss.size(); i++) {
                if(!ss.get(i).equals(current)) continue;
                f = i+1;
            }
            if(f > -1) {
                if(f >= ss.size()) f = 0;
                c = ss.get(f);
            }
        }
        
        return c;
    }
}

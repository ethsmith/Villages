package com.domsplace.Villages.Commands.SubCommands.Mayor;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.PluginHook;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Enums.ExpandMethod;
import com.domsplace.Villages.Events.VillageExpandEvent;
import com.domsplace.Villages.Objects.Region;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageMayorExpand extends SubCommand {
    public VillageMayorExpand() {
        super("village", "mayor", "expand");
        this.setPermission("mayor.expand");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {sk(sender, "playeronly");return true;}
        
        Resident r = Resident.getResident(getPlayer(sender));
        Village v = Village.getPlayersVillage(r);
        if(v == null) {sk(sender, "notinvillage");return true;}
        if(!v.isMayor(r)) {sk(sender, "onlymayorexpand"); return true;}
        
        List<Region> claiming = new ArrayList<Region>();
        if(Base.ExpandingMethod.equals(ExpandMethod.PER_CHUNK)) {
            Region region = Region.getRegion(getPlayer(sender).getLocation());
            if(region == null) return true; //Make sure Region is Valid
            
            //Check to make sure this Region borders the Village
            if(!v.doesRegionBorder(region)) {
                sk(sender, "cantexpandborder");
                return true;
            }
            
            claiming.add(region);
        } else if(Base.ExpandingMethod.equals(ExpandMethod.CLASSIC)) {
            //Get current "classic size" of Village
            int size = 1;
            List<Region> chunks = v.getRegions();
            if(chunks.size() > 1) {
                size = (int) Math.ceil((Math.sqrt(chunks.size()) / 2.0d) + 0.5d);
            }
            
            //Get Chunks that border
            int x = v.getSpawn().getRegionX();
            int z = v.getSpawn().getRegionZ();
        
            for(int cx = -size; cx <= size; cx++) {
                for(int cz = -size; cz <= size; cz++) {
                    Region re = v.getSpawn().getRelativeRegion(cx, cz);
                    if(r == null) {
                        continue;
                    }
                    
                    if(!(((cx == -size) || (cx == size))  || ((cz == -size) || (cz == size)))) {continue;}
                    
                    claiming.add(re);
                }
            }
        }
        
        for(Region region : claiming) {
            //Make sure the Region is in the same world as their Village
            if(!region.getWorld().equalsIgnoreCase(v.getSpawn().getWorld())) {
                sk(sender, "notinthisworld");
                return true;
            }
            
            //Make Sure region isn't claimed
            if(Village.doesRegionOverlapVillage(region)) {
                sk(sender, "expandvillageoverlap");
                return true;
            }

            //Make sure they have WorldGuard Perms
            if(Base.useWorldGuard) {
                if(PluginHook.WORLD_GUARD_HOOK.isOverlappingRegion(region)) {
                    sk(sender, "expandregionoverlap");
                    return true;
                }
            }
        }
        
        //Charge Village on Per-Chunk basis
        double cost = getCost("expandvillage") * claiming.size();
        
        //First, make sure they have the money to expand
        if(Base.useEconomy && getConfig().getBoolean("features.banks.money", true)  && !hasBalance(v, cost)) {
            sk(sender, "villagebankneedmore", PluginHook.VAULT_HOOK.formatEconomy(cost));
            return true;
        }
        
        //Fire Event
        VillageExpandEvent event = new VillageExpandEvent(v, claiming, r);
        event.fireEvent();
        if(event.isCancelled()) return true;
        
        //Charge Village
        if(Base.useEconomy && getConfig().getBoolean("features.banks.money", true)) {
            v.getBank().addWealth(-cost);
        }
        
        v.addRegions(claiming);
        sk(sender, "villageexpanded", claiming.size());
        DataManager.saveAll();
        return true;
    }
}

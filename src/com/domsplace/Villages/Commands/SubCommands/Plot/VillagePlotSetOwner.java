package com.domsplace.Villages.Commands.SubCommands.Plot;

import static com.domsplace.Villages.Bases.Base.sk;
import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Objects.Plot;
import com.domsplace.Villages.Objects.Region;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillagePlotSetOwner extends SubCommand {
    public VillagePlotSetOwner() {
        super("village", "plot", "set", "owner");
        this.setPermission("plot.set.owner");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(!getConfig().getBoolean("features.plots", true)) {
            sk(sender, "plotsnotenabled");
            return true;
        }
        if(!isPlayer(sender)) {sk(sender, "playeronly");return true;}
        
        Resident r = Resident.getResident(getPlayer(sender));
        Village v = Village.getPlayersVillage(r);
        if(v == null) {sk(sender, "notinvillage");return true;}
        if(!v.isMayor(r)) {sk(sender, "onlymayorplot"); return true;}
        
        Region standing = Region.getRegion(getPlayer(sender));
        if(standing == null) return true;
        
        if(!v.isRegionOverlappingVillage(standing)) {
            sk(sender, "plotnotinvillage");
            return true;
        }
        
        if(args.length < 1) {
            sk(sender, "enterplayer");
            return true;
        }
        
        Resident target = Resident.guessResident(args[0]);
        if(target == null) {
            sk(sender, "playernotfound");
            return true;
        }
        
        Village tVillage = Village.getPlayersVillage(target);
        if((tVillage == null) || !(tVillage.equals(v))) {
            sk(sender, "notresident", target);
            return true;
        }
        
        Plot plot = v.getPlot(standing);
        if(plot == null) {
            plot = new Plot(v, standing);
            v.addPlot(plot);
        }
        
        plot.setOwner(target);
        sk(sender, "setplotowner", target);
        sk(target, "chunkclaimed", plot);
        DataManager.saveAll();
        return true;
    }
}

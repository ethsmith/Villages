package com.domsplace.Villages.Commands.SubCommands.Plot;

import com.domsplace.Villages.Bases.Base;
import static com.domsplace.Villages.Bases.Base.sk;
import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.PluginHook;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Objects.Plot;
import com.domsplace.Villages.Objects.Region;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillagePlotCheck extends SubCommand {
    public VillagePlotCheck() {
        super("village", "plot", "check");
        this.setPermission("plot.check");
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
        
        Region standing = Region.getRegion(getPlayer(sender));
        if(standing == null) return true;
        
        if(!v.isRegionOverlappingVillage(standing)) {
            sk(sender, "plotnotinvillage");
            return true;
        }
        
        Plot plot = v.getPlot(standing);
        if(plot == null) {
            sk(sender, "notplot");
            return true;
        }
        
        if(plot.getPrice() <= 0 && (plot.getOwner() == null || plot.getOwner().equals(v.getMayor()))) {
            sk(sender, "notplot");
            return true;
        }
        
        if(plot.getOwner() == null || plot.getOwner().equals(v.getMayor())) {
            if(Base.useEconomy) {
                sk(sender, "chunkavailable", PluginHook.VAULT_HOOK.formatEconomy(plot.getPrice()));
            } else {
                sk(sender, "chunkavailable", "Free!");
            }
            return true;
        }
        
        sk(sender, "claimedchunkinfo", plot.getOwner());
        return true;
    }
}

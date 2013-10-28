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

public class VillagePlotSetPrice extends SubCommand {
    public VillagePlotSetPrice() {
        super("village", "plot", "set", "price");
        this.setPermission("plot.set.price");
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
        
        if(!Base.useEconomy) {
            sk(sender, "economydisabled");
            return true;
        }
        
        Region standing = Region.getRegion(getPlayer(sender));
        if(standing == null) return true;
        
        if(!v.isRegionOverlappingVillage(standing)) {
            sk(sender, "plotnotinvillage");
            return true;
        }
        
        if(args.length < 1) {
            sk(sender, "enteramt");
            return true;
        }
        
        if(!isDouble(args[0])) {
            sk(sender, "mustbenumber");
            return true;
        }
        
        double amt = getDouble(args[0]);
        if(amt <= 0) {
            sk(sender, "mustbeone");
            return true;
        }
        
        Plot plot = v.getPlot(standing);
        if(plot == null) {
            plot = new Plot(v, standing);
            v.addPlot(plot);
        }
        
        plot.setPrice(amt);
        sk(sender, "setplotprice", PluginHook.VAULT_HOOK.formatEconomy(amt));
        return true;
    }
}

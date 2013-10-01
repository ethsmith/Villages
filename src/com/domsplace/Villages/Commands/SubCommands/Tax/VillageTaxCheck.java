package com.domsplace.Villages.Commands.SubCommands.Tax;

import com.domsplace.Villages.Bases.Base;
import static com.domsplace.Villages.Bases.Base.getPlayer;
import static com.domsplace.Villages.Bases.Base.sk;
import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.PluginHook;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Tax;
import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Objects.VillageItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageTaxCheck extends SubCommand {
    public VillageTaxCheck() {
        super("village", "tax", "check");
        this.setPermission("tax.check");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        //Make sure it's a player running the command
        if(!isPlayer(sender)) {
            sk(sender, "playeronly");
            return false;
        }
        
        Resident r = Resident.getResident(getPlayer(sender));
        Village v = Village.getPlayersVillage(r);
        if(v == null) {sk(sender, "notinvillage");return true;}
        
        if(args.length > 0) {
            Tax t = Tax.getTaxByName(args[0]);
            if(t == null) {
                sk(sender, "notaxfound");
                return true;
            }
            
            List<String> messages = new ArrayList<String>();
            
            messages.addAll(gk("taxinfo", t));
            
            if(Base.useVault && Base.getConfig().getBoolean("features.banks.money", true)) {
                messages.add("\tDue Money: " + PluginHook.VAULT_HOOK.getEconomy().format(t.getRelativeCost(v)));
            }
            
            if(Base.getConfig().getBoolean("features.banks.item", true)) {
                List<String> ss = VillageItem.getHumanMessages(t.getRelativeItemsCost(v));
                for(String s : ss) {
                    messages.add("\tItem Due: " + s);
                }
            }
            
            messages.add("\tDue in: " + getTimeDifference(new Date(getNow() + (long)(t.getHours() * 3600000d))));
            
            sendMessage(sender, messages);
            return true;
        }
        
        sk(sender, "taxes", label);
        
        for(Tax t : Tax.getTaxes()) {
            String message = "\t" + t.getName();
            sendMessage(sender, message);
        }
        
        return true;
    }
}

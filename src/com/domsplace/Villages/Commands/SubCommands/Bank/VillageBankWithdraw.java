package com.domsplace.Villages.Commands.SubCommands.Bank;

import com.domsplace.Villages.Bases.Base;
import static com.domsplace.Villages.Bases.Base.getPlayer;
import static com.domsplace.Villages.Bases.Base.sk;
import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.PluginHook;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageBankWithdraw extends SubCommand {
    public VillageBankWithdraw() {
        super("village", "bank", "withdraw");
        this.setPermission("bank.withdraw");
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
        
        if(args.length < 1) {
            sk(sender, "enteramount");
            return false;
        }
        
        if(!isDouble(args[0])) {
            sk(sender, "notmoney");
            return false;
        }
        
        double amt = getDouble(args[0]);
        if(amt <= 0) {
            sk(sender, "mustbeone");
            return true;
        }
        
        double b = v.getBank().getWealth();
        if(b < amt) {
            sk(sender, "villagebankneedmore", amt);
            return true;
        }
        
        v.getBank().addWealth(-amt);
        PluginHook.VAULT_HOOK.getEconomy().depositPlayer(sender.getName(), amt);
        v.broadcast(gk("withdrawledmoney", r, amt));
        return true;
    }
}

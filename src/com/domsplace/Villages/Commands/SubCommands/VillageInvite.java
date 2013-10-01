package com.domsplace.Villages.Commands.SubCommands;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageInvite extends SubCommand {
    public static final Map<Resident, Village> VILLAGE_INVITES = new HashMap<Resident, Village>();
    
    public VillageInvite() {
        super("village", "invite");
        this.addOption(SubCommand.PLAYERS_OPTION);
        this.setPermission("invite");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {
            sk(sender, "playeronly");
            return true;
        }
        
        if(args.length < 1) {
            sk(sender, "enterplayer");
            return false;
        }
        
        Resident target = Resident.guessResident(args[0]);
        if(target == null || !target.getOfflinePlayer().isOnline()) {
            sk(sender, "playernotfound");
            return true;
        }
        
        Village v = Village.getPlayersVillage(target);
        if(v != null) {
            sk(sender, "playerinvillage", target);
            return true;
        }
        
        Resident player = Resident.getResident(getPlayer(sender));
        Village playersVillage = Village.getPlayersVillage(player);
        if(player == null || playersVillage == null) {
            sk(sender, "notinvillage");
            return true;
        }
        
        VILLAGE_INVITES.remove(target);
        VILLAGE_INVITES.put(target, playersVillage);
        
        sk(target.getPlayer(), "villageinvite", player, playersVillage);
        playersVillage.broadcast(gk("residentinvited", target));
        return true;
    }
}

package com.domsplace.Villages.Commands;

import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Utils.Utils;
import com.domsplace.Villages.Utils.VillageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.domsplace.Villages.Bases.CommandBase;

public class VillageAcceptCommand extends CommandBase {
    public VillageAcceptCommand() {
        super("villageaccept");
    }
    
    @Override
    public boolean cmd(CommandSender cs, Command cmd, String label, String[] args) {
        if(!(cs instanceof Player)) {
            Utils.msgPlayer(cs, gK("playeronly"));
            return true;
        }

        Player sender = (Player) cs;

        if(!VillageUtils.townInvites.containsKey(sender)) {
            Utils.msgPlayer(cs, gK("noinvite"));
            return true;
        }

        Village v = VillageUtils.townInvites.get(sender);
        if(v == null) {
            Utils.msgPlayer(cs, gK("noinvite"));
            return true;
        }

        Village vs = VillageUtils.getPlayerVillage(sender);
        if(vs != null) {
            Utils.msgPlayer(cs, gK("alreadyinvillage"));
            return true;
        }

        v.addResident(sender);
        v.SendMessage(gK("joinedvillage", sender));
        sender.teleport(v.getSpawnBlock());
        return true;
    }
}

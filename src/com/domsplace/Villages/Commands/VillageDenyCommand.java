package com.domsplace.Villages.Commands;

import com.domsplace.Villages.Objects.Village;

import com.domsplace.Villages.Utils.VillageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.domsplace.Villages.Bases.CommandBase;

public class VillageDenyCommand extends CommandBase {
    public VillageDenyCommand() {
        super("villagedeny");
    }
    
    @Override
    public boolean cmd(CommandSender cs, Command cmd, String label, String[] args) {
        if(!(cs instanceof Player)) {
            msgPlayer(cs, gK("playeronly"));
            return true;
        }

        Player sender = (Player) cs;

        if(!VillageUtils.townInvites.containsKey(sender)) {
            msgPlayer(cs, gK("noinvite"));
            return true;
        }

        Village v = VillageUtils.townInvites.get(sender);
        if(v == null) {
            msgPlayer(cs, gK("noinvite"));
            return true;
        }

        sender.sendMessage(ChatDefault + "Denied the invite.");
        VillageUtils.townInvites.remove(sender);
        return true;
    }
}

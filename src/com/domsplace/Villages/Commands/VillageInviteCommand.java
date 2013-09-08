package com.domsplace.Villages.Commands;

import com.domsplace.Villages.Objects.Village;

import com.domsplace.Villages.Utils.VillageUtils;
import com.domsplace.Villages.Bases.CommandBase;
import com.domsplace.Villages.Objects.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VillageInviteCommand extends CommandBase {
    public VillageInviteCommand () {
        super("villageinvite");
        this.addSubCommand(SubCommand.make(SubCommand.PLAYER));
    }

    @Override
    public boolean cmd(CommandSender cs, Command cmd, String label, String[] args) {
        if(!isPlayer(cs)) {
            msgPlayer(cs, gK("playeronly"));
            return true;
        }

        if(args.length < 1) {
            msgPlayer(cs, gK("enterplayer"));
            return false;
        }

        Player sender = (Player) cs;

        //Get The Senders Village
        Village village = VillageUtils.getPlayerVillage(sender);
        if(village == null) {
            msgPlayer(cs, gK("notinvillage"));
            return true;
        }

        Player p = getPlayer(cs, args[0]);
        if(p == null) {
            msgPlayer(cs, ChatError + args[0] + " isn't online.");
            return true;
        }

        Village tp = VillageUtils.getPlayerVillage(p);
        if(tp != null) {
            msgPlayer(cs, ChatError + p.getDisplayName() + " is already in a Village.");
            return true;
        }

        VillageUtils.townInvites.put(p, village);
        msgPlayer(p, gK("villageinvite", village).replaceAll("%p%", sender.getName()));
        msgPlayer(p, ChatDefault + "Type " + ChatImportant + "/villageaccept" + ChatDefault + " or " + ChatImportant + "/villagedeny");
        msgPlayer(sender, gK("residentinvited", p));
        return true;
    }
}

package com.domsplace.Villages.Commands;

import com.domsplace.Villages.Objects.Village;

import com.domsplace.Villages.Utils.VillageUtils;
import com.domsplace.Villages.Bases.CommandBase;
import com.domsplace.Villages.Objects.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BroadcastCommand extends CommandBase {
    public BroadcastCommand () {
        super("villagesbroadcast");
        this.addSubCommand(SubCommand.make(SubCommand.VILLAGE, "message"));
    }

    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            msgPlayer(sender, gK("entervillagename"));
            return false;
        }

        if(args.length < 2) {
            msgPlayer(sender, gK("entermessage"));
            return false;
        }

        Village v = VillageUtils.getVillage(args[0]);
        if(v == null) {
            msgPlayer(sender, gK("cantfindvillage"));
            return true;
        }

        String msg = "";

        for(int i = 1; i < args.length; i++) {
            msg += args[i];
            if(i < args.length-1) {
                msg += " ";
            }
        }
        msg = ChatDefault + "[" + ChatImportant + v.getName() + ChatDefault + "] " + ChatImportant + "Broadcast: " + ChatDefault + msg;

        v.sendMessage(msg);
        if(!v.isResident(sender)) {
            msgPlayer(sender, msg);
        }
        return true;
    }
}

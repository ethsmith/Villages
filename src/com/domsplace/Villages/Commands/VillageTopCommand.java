package com.domsplace.Villages.Commands;

import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Utils.Utils;
import com.domsplace.Villages.Utils.VillageUtils;
import static com.domsplace.Villages.Bases.Base.ChatDefault;
import com.domsplace.Villages.Bases.CommandBase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageTopCommand extends CommandBase {
    public VillageTopCommand () {
        super("villagetop");
    }

    @Override
    public boolean cmd(CommandSender cs, Command cmd, String label, String[] args) {
        if(VillageUtils.Villages.size() < 1) {
            Utils.msgPlayer(cs, ChatError + "No villages.");
            return true;
        }

        Village top = null;
        Village sec = null;
        Village thr = null;

        for(Village v : VillageUtils.Villages) {
            if(top == null) {
                top = v;
                continue;
            }

            if(v.getMoney() > top.getWorth()) {
                if(sec != null) {
                    thr = sec;
                }
                sec = top;
                top = v;
                continue;
            }

            if(v.getMoney() > sec.getWorth() && sec != null) {
                thr = sec;
                sec = v;
                continue;
            }

            if(v.getMoney() > thr.getWorth() && thr != null) {
                thr = v;
                continue;
            }
        }

        Utils.msgPlayer(cs, ChatDefault + "Top Villages: ");
        if(top != null) {
            Utils.msgPlayer(cs, ChatImportant + "#1: " + ChatDefault + top.getName());
        }
        if(sec != null) {
            Utils.msgPlayer(cs, ChatImportant + "#2: " + ChatDefault + sec.getName());
        }
        if(thr != null) {
            Utils.msgPlayer(cs, ChatImportant + "#3: " + ChatDefault + thr.getName());
        }

        return true;
    }
}

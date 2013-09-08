package com.domsplace.Villages.Commands;

import com.domsplace.Villages.Objects.Village;

import com.domsplace.Villages.Utils.VillageUtils;
import com.domsplace.Villages.Bases.CommandBase;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillagesCommand extends CommandBase {
    public VillagesCommand () {
        super("villages");
    }

    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        List<Village> villages = VillageUtils.getVillages();

        String msg = ChatImportant + "Villages: " + ChatDefault;
        for(int i = 0; i < villages.size(); i++) {
            msg += villages.get(i).getName();
            if(i < (villages.size() - 1)) {
                msg += ", ";
            }
        }

        msgPlayer(sender, msg);
        return true;
    }
}

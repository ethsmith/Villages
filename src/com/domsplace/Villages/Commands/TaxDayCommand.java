package com.domsplace.Villages.Commands;

import com.domsplace.Villages.DataManagers.UpkeepManager;
import com.domsplace.Villages.Objects.Village;

import com.domsplace.Villages.Utils.VillageUtils;
import com.domsplace.Villages.Bases.CommandBase;
import com.domsplace.Villages.Bases.DataManagerBase;
import java.io.File;
import java.util.Date;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class TaxDayCommand extends CommandBase {
    public TaxDayCommand () {
        super("taxday");
    }

    @Override
    public boolean cmd(CommandSender cs, Command cmd, String label, String[] args) {
        if(!isPlayer(cs)) {
            msgPlayer(cs, gK("playeronly"));
            return false;
        }

        Player p = (Player) cs;
        Village v = VillageUtils.getPlayerVillage(p);

        if(v == null) {
            msgPlayer(cs, gK("notinvillage"));
            return true;
        }

        File upkeepF = new File(getDataFolder() + "/upkeepdata.yml");
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(upkeepF);
        MemorySection ms = (MemorySection) yml.get(v.getName());
        if(ms == null) {
            msgPlayer(cs, gK("error"));
            return true;
        }

        int found = 0;

        for(String upkeep : ms.getKeys(false)) {
            long time = yml.getLong(v.getName() + "." + upkeep + ".time");
            long nextTime = time + (DataManagerBase.UPKEEP_MANAGER.Upkeep.getLong(upkeep + ".hours") * 60L * 60L * 1000L);


            Date lastCheck = new Date(time);
            Date nextCheck = new Date(nextTime);

            String nd = getTimeUntilHuman(nextCheck);

            msgPlayer(cs, gK("nexttaxday").replaceAll("%tax%", upkeep).replaceAll("%date%", nd));
            found++;
        }

        if(found == 0) {
            msgPlayer(cs, gK("notaxes"));
        }

        return true;
    }
}

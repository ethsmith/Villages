package com.domsplace.Commands;

import com.domsplace.DataManagers.VillageUpkeepManager;
import com.domsplace.Objects.Village;
import com.domsplace.Utils.VillageUtils;
import com.domsplace.Utils.VillageVillagesUtils;
import com.domsplace.VillageBase;
import com.domsplace.VillagesPlugin;
import java.io.File;
import java.util.Date;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class VillagesTaxDayCommand extends VillageBase implements CommandExecutor {
    
    private VillagesPlugin plugin;
    
    public VillagesTaxDayCommand (VillagesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("taxday")) {
            if(!(cs instanceof Player)) {
                VillageUtils.msgPlayer(cs, gK("playeronly"));
                VillageUtils.msgPlayer(cs, VillageUtils.getCommandDescription(cmd.getName()));
                return true;
            }
            
            Player p = (Player) cs;
            Village v = VillageVillagesUtils.getPlayerVillage(p);

            if(v == null) {
                VillageUtils.msgPlayer(cs, gK("notinvillage"));
                return true;
            }
            
            File upkeepF = new File(VillageUtils.plugin.getDataFolder() + "/upkeepdata.yml");
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(upkeepF);
            MemorySection ms = (MemorySection) yml.get(v.getName());
            if(ms == null) {
                VillageUtils.msgPlayer(cs, gK("error"));
                return true;
            }
            
            int found = 0;
            
            for(String upkeep : ms.getKeys(false)) {
                long time = yml.getLong(v.getName() + "." + upkeep + ".time");
                long nextTime = time + (VillageUpkeepManager.Upkeep.getLong(upkeep + ".hours") * 60L * 60L * 1000L);
                
                
                Date lastCheck = new Date(time);
                Date nextCheck = new Date(nextTime);
                
                String nd = VillageUtils.TimeAway(nextCheck);
                
                VillageUtils.msgPlayer(cs, gK("nexttaxday").replaceAll("%tax%", upkeep).replaceAll("%date%", nd));
                found++;
            }
            
            if(found == 0) {
                VillageUtils.msgPlayer(cs, gK("notaxes"));
            }
            
            return true;
        }
        
        return false;
    }
}

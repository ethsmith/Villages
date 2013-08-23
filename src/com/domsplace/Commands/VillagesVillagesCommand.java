package com.domsplace.Commands;

import com.domsplace.Objects.Village;
import com.domsplace.Utils.VillageUtils;
import com.domsplace.Utils.VillageVillagesUtils;
import com.domsplace.VillageBase;
import com.domsplace.VillagesPlugin;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VillagesVillagesCommand extends VillageBase implements CommandExecutor {
    
    private VillagesPlugin plugin;
    
    public VillagesVillagesCommand (VillagesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("villages")) {            
            List<Village> villages = VillageVillagesUtils.Villages;
            
            String msg = ChatImportant + "Villages: " + ChatDefault;
            for(int i = 0; i < villages.size(); i++) {
                msg += villages.get(i).getName();
                if(i < (villages.size() - 1)) {
                    msg += ", ";
                }
            }
            
            VillageUtils.msgPlayer(sender, msg);
            return true;
        }
        
        return false;
    }
}

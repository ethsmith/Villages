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

public class VillagesBroadcastCommand extends VillageBase implements CommandExecutor {
    
    private VillagesPlugin plugin;
    
    public VillagesBroadcastCommand (VillagesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("villagesbroadcast")) {
            if(args.length < 1) {
                VillageUtils.msgPlayer(sender, gK("entervillagename"));
                return false;
            }
            
            if(args.length < 2) {
                VillageUtils.msgPlayer(sender, gK("entermessage"));
                return false;
            }
            
            Village v = VillageVillagesUtils.getVillage(args[0]);
            if(v == null) {
                VillageUtils.msgPlayer(sender, gK("cantfindvillage"));
                return true;
            }
            
            String msg = "";
            
            for(int i = 1; i < args.length; i++) {
                msg += args[i];
                if(i < args.length-1) {
                    msg += " ";
                }
            }
            msg = ChatDefault + "[" + ChatImportant + v.getName() + ChatDefault + "] " + ChatImportant + "Broadcast: " + msg;
            
            v.SendMessage(msg);
            if(!v.isResident(sender)) {
                VillageUtils.msgPlayer(sender, msg);
            }
            return true;
        }
        
        return false;
    }
}

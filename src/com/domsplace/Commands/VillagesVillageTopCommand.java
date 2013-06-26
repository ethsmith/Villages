package com.domsplace.Commands;

import com.domsplace.Objects.Village;
import com.domsplace.Utils.VillageUtils;
import com.domsplace.Utils.VillageVillagesUtils;
import com.domsplace.VillageBase;
import static com.domsplace.VillageBase.ChatDefault;
import com.domsplace.VillagesPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VillagesVillageTopCommand extends VillageBase implements CommandExecutor {
    
    private VillagesPlugin plugin;
    
    public VillagesVillageTopCommand (VillagesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("villagetop")) {
            if(VillageVillagesUtils.Villages.size() < 1) {
                VillageUtils.msgPlayer(cs, ChatError + "No villages.");
                return true;
            }
            
            Village top = null;
            Village sec = null;
            Village thr = null;
            
            for(Village v : VillageVillagesUtils.Villages) {
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
            
            VillageUtils.msgPlayer(cs, ChatDefault + "Top Villages: ");
            if(top != null) {
                VillageUtils.msgPlayer(cs, ChatImportant + "#1: " + ChatDefault + top.getName());
            }
            if(sec != null) {
                VillageUtils.msgPlayer(cs, ChatImportant + "#2: " + ChatDefault + sec.getName());
            }
            if(thr != null) {
                VillageUtils.msgPlayer(cs, ChatImportant + "#3: " + ChatDefault + thr.getName());
            }
            
            return true;
        }
        
        return false;
    }
}

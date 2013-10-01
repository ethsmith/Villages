package com.domsplace.Villages.Commands.SubCommands;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Objects.Village;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageTop extends SubCommand {
    public VillageTop() {
        super("village", "top");
        this.setPermission("villagetop");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        List<Village> dontmod = Village.getVillages();
        List<Village> copy = new ArrayList<Village>(dontmod);
        List<Village> top = new ArrayList<Village>();
        for(int i = 0; i < 5; i++) {
            Village best = null;
            for(Village v : dontmod) {
                if(!copy.contains(v)) continue;
                if(best == null) {
                    best = v;
                    continue;
                }

                if(v.getValue() > best.getValue()) {
                    best = v;
                }
            }
            if(best == null) continue;
            top.add(best);
            copy.remove(best);
        }
        
        List<String> messages = new ArrayList<String>();
        messages.addAll(gk("topvillages", top.size()));
        int i = 1;
        for(Village v : top) {
            messages.add("\t#" + i + ": " + ChatImportant + v.getName());
        }
        
        sendMessage(sender, messages);
        return true;
    }
}

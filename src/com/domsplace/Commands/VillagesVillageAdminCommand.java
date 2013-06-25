package com.domsplace.Commands;

import com.domsplace.DataManagers.*;
import com.domsplace.Utils.*;
import com.domsplace.*;
import com.domsplace.Objects.*;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VillagesVillageAdminCommand extends VillageBase implements CommandExecutor {
    
    private VillagesPlugin plugin;
    
    public VillagesVillageAdminCommand (VillagesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("villageadmin")) {
            if(args.length == 0) {
                List<String> msgs = new ArrayList<String>();
                
                msgs.add(ChatImportant + "Admin Commands:");
                if(sender.hasPermission("Villages.reload")) {
                    msgs.add(ChatImportant + "/villageadmin reload " + ChatDefault + " - Reloads the config.");
                }
                msgs.add(ChatImportant + "/villageadmin save " + ChatDefault + " - Saves the config.");
                msgs.add(ChatImportant + "/villageadmin delete [town] " + ChatDefault + " - Delete a village.");
                
                VillageUtils.msgPlayer(sender, msgs);
                return true;
            }
            
            String arg = args[0].toLowerCase();
            if(arg.equals("reload")) {
                VillageUtils.msgPlayer(sender, ChatDefault + "Reloading Config...");
                VillageConfigManager.LoadConfig();
                VillageVillagesUtils.LoadAllVillages();
                VillageUtils.msgPlayer(sender, ChatImportant + "Reloaded!");
                return true;
            }
            
            if(arg.equals("save")) {
                VillageUtils.msgPlayer(sender, ChatDefault + "Flushing data...");
                VillageVillagesUtils.SaveAllVillages();
                VillageUtils.msgPlayer(sender, ChatImportant + "Saved Data!");
                return true;
            }
            
            if(arg.equals("delete")) {
                if(args.length < 2) {
                    VillageUtils.msgPlayer(sender, gK("neednamedelete"));
                    return true;
                }
                
                String name = args[1];
                Village village = VillageVillagesUtils.getVillage(name);
                if(village == null) {
                    VillageUtils.msgPlayer(sender, gK("villagedoesntexist"));
                    return true;
                }
                
                VillageUtils.msgPlayer(sender, gK("villagedelete", village));
                VillageVillagesUtils.DeleteVillage(village);
                return true;
            }
            
            return true;
        }
        
        return false;
    }
}

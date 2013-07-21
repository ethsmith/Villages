package com.domsplace.Commands;

import com.domsplace.DataManagers.*;
import com.domsplace.Utils.*;
import com.domsplace.*;
import com.domsplace.Objects.*;
import static com.domsplace.VillageBase.ChatDefault;
import static com.domsplace.VillageBase.ChatError;
import static com.domsplace.VillageBase.ChatImportant;
import static com.domsplace.VillageBase.gK;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                
                msgs.add(ChatImportant + "Commands help: " + ChatDefault + "http://adf.ly/Rh7uA");
                
                VillageUtils.msgPlayer(sender, msgs);
                return true;
            }
            
            String arg = args[0].toLowerCase();
            if(arg.equals("reload")) {
                VillageUtils.msgPlayer(sender, ChatDefault + "Reloading Config...");
                VillageConfigManager.LoadConfig();
                VillageVillagesUtils.LoadAllVillages();
                VillageUpkeepManager.SetupUpkeep();
                VillageUtils.msgPlayer(sender, ChatImportant + "Reloaded!");
                return true;
            }
            
            if(arg.equals("save")) {
                if(args.length == 2 && args[1].equalsIgnoreCase("yml") && VillageUtils.useSQL) {
                    VillageUtils.msgPlayer(sender, "Force saving SQL data as YML.");
                    
                    for(Village v : VillageVillagesUtils.getVillages()) {
                        VillageVillagesUtils.SaveVillageYML(v);
                    }
                    
                    VillageUtils.msgPlayer(sender, ChatImportant + "Saved data as YML!");
                    return true;
                }
                
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
                
            if(arg.equalsIgnoreCase("kick")) {
                if(args.length < 2) {
                    VillageUtils.msgPlayer(sender, gK("notenougharguments"));
                    return true;
                }

                OfflinePlayer p = VillageUtils.getOfflinePlayer(sender, args[1]);
                if(p == null) {
                    sender.sendMessage(ChatError + args[1] + " not found.");
                    return true;
                }

                Village tp = VillageVillagesUtils.getPlayerVillage(p);
                if(tp == null) {
                    VillageUtils.msgPlayer(sender, gK("notinvillage"));
                    return true;
                }

                if(tp.isMayor(p)) {
                    VillageUtils.msgPlayer(sender, gK("cantkickmayor"));
                    return true;
                }

                tp.SendMessage(gK("residentkicked", p));
                tp.removeResident(p);
                VillageVillagesUtils.SaveAllVillages();
                VillageUtils.msgPlayer(sender, gK("residentkicked", p));
                return true;
            }
            
            //Try to get Village based off argument(s)
            Village v = VillageVillagesUtils.getVillage(arg);
            
            if(v != null) {
                //Got Village, lets use the extra arguments
                
                if(args.length < 2) {
                    VillageUtils.msgPlayer(sender, gK("notenougharguments"));
                    return true;
                }
                
                String ar = args[1];
                if(ar.equalsIgnoreCase("invite")) {
                    if(args.length < 3) {
                        VillageUtils.msgPlayer(sender, gK("notenougharguments"));
                        return true;
                    }
                    
                    Player p = VillageUtils.getPlayer(sender, args[2]);
                    if(p == null) {
                        sender.sendMessage(ChatError + args[2] + " not found.");
                        return true;
                    }
                    
                    Village tp = VillageVillagesUtils.getPlayerVillage(p);
                    if(tp != null) {
                        VillageUtils.msgPlayer(sender, ChatError + p.getDisplayName() + " is already in a Village.");
                        return true;
                    }

                    VillageVillagesUtils.townInvites.put(p, v);
                    VillageUtils.msgPlayer(p, gK("villageinvite", v).replaceAll("%p%", sender.getName()));
                    VillageUtils.msgPlayer(p, ChatDefault + "Type " + ChatImportant + "/villageaccept" + ChatDefault + " or " + ChatImportant + "/villagedeny");
                    VillageUtils.msgPlayer(sender, gK("residentinvited", p));
                    return true;
                }
                
                if(ar.equalsIgnoreCase("mayor")) {
                    if(args.length < 3) {
                        VillageUtils.msgPlayer(sender, gK("notenougharguments"));
                        return true;
                    }
                    
                    OfflinePlayer p = VillageUtils.getOfflinePlayer(sender, args[2]);
                    if(p == null) {
                        sender.sendMessage(ChatError + args[2] + " not found.");
                        return true;
                    }
                    
                    Village tp = VillageVillagesUtils.getPlayerVillage(p);
                    if(tp == null) {
                        VillageUtils.msgPlayer(sender, ChatError + p.getName() + " isnt in a Village.");
                        return true;
                    }
                    
                    if(tp != v) {
                        VillageUtils.msgPlayer(sender, ChatError + p.getName() + " isnt in that Village.");
                        return true;
                    }
                    
                    if(!v.isResident(v.getMayor())) {
                        v.addResident(v.getMayor());
                    }
                    v.setMayor(p);
                    if(!v.isResident(v.getMayor())) {
                        v.addResident(v.getMayor());
                    }
                    
                    VillageUtils.msgPlayer(sender, ChatImportant + p.getName() + ChatDefault + " is the new Mayor of " + ChatImportant + v.getName());
                    return true;
                }
            }
            
            
            VillageUtils.msgPlayer(sender, gK("invalidargument"));
            return true;
        }
        
        return false;
    }
}

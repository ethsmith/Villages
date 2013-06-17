package com.minecraft.softegg;

import static com.minecraft.softegg.VillageBase.ChatDefault;
import static com.minecraft.softegg.VillageBase.ChatError;
import static com.minecraft.softegg.VillageBase.ChatImportant;
import static com.minecraft.softegg.VillageBase.SendMessage;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VillagesCommands extends VillageBase implements CommandExecutor {
    
    private Villages plugin;
    
    public VillagesCommands(Villages plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("villageadmin")) {
            if(args.length == 0) {
                List<String> msgs = new ArrayList<String>();
                
                msgs.add(ChatImportant + "Admin Commands:");
                if(cs.hasPermission("Villages.reload")) {
                    msgs.add(ChatImportant + "/villageadmin reload " + ChatDefault + " - Reloads the config.");
                }
                msgs.add(ChatImportant + "/villageadmin save " + ChatDefault + " - Saves the config.");
                msgs.add(ChatImportant + "/villageadmin delete [town] " + ChatDefault + " - Delete a village.");
                
                SendMessage(cs, msgs);
                return true;
            }
            
            String arg = args[0].toLowerCase();
            if(arg.equals("reload")) {
                SendMessage(cs, ChatDefault + "Reloading Config...");
                VillageDataManager.dataManager.checkConfig(plugin);
                VillageUtils.LoadVillages();
                SendMessage(cs, ChatImportant + "Reloaded!");
                return true;
            }
            
            if(arg.equals("save")) {
                SendMessage(cs, ChatDefault + "Flushing data...");
                VillageUtils.SaveAllVillages();
                SendMessage(cs, ChatImportant + "Saved Data!");
                return true;
            }
            
            if(arg.equals("delete")) {
                if(args.length < 2) {
                    SendMessage(cs, ChatError + "Please enter a village name to delete.");
                    return true;
                }
                
                String name = args[1];
                Village village = VillageUtils.getVillage(name);
                if(village == null) {
                    SendMessage(cs, ChatError + "Village doesn't exist.");
                    return true;
                }
                
                SendMessage(cs, ChatImportant + "Deleted " + village.getName() + "!");
                VillageDataManager.dataManager.deleteTown(village);
                return true;
            }
            
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("villages")) {
            if(args.length > 0) {
                SendMessage(cs, ChatError + "Too many arguments.");
                SendMessage(cs, VillageUtils.getCommandDescription(cmd.getName()));
                return true;
            }
            
            List<Village> villages = VillageUtils.villages;
            
            String msg = ChatImportant + "Villages: " + ChatDefault;
            for(int i = 0; i < villages.size(); i++) {
                msg += villages.get(i).getName();
                if(i < (villages.size() - 1)) {
                    msg += ", ";
                }
            }
            
            SendMessage(cs, msg);
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("village")) {
            if(!(cs instanceof Player)) {
                if(args.length == 0) {
                    SendMessage(cs, ChatError + "Please provide a Village name.");
                    SendMessage(cs, VillageUtils.getCommandDescription(cmd.getName()));
                    return true;
                }
            }
            
            Village village = null;
            if(args.length == 0) {
                village = VillageUtils.getPlayerVillage((Player) cs);
            } else if(args.length >= 1) {
                String command = args[0].toLowerCase();
                
                if(command.equalsIgnoreCase("deposit") && VillageDataManager.useEconomy() && (cs instanceof Player)) {
                    if(args.length < 2) {
                        SendMessage(cs, ChatError + "Please enter an amount to deposit into the Village bank.");
                        return true;
                    }
                    
                    double amount = -1;
                    try {
                        amount = Double.parseDouble(args[1]);
                    } catch(Exception ex) {
                        SendMessage(cs, ChatError + "Amount must be a number.");
                        return true;
                    }
                    
                    //Ensure player has enough money to deposit
                    double pAmount = VillageUtils.economy.getBalance(cs.getName());
                    
                    if(pAmount < amount) {
                        SendMessage(cs, ChatError + "You don't have " + VillageUtils.economy.format(amount) + " to deposit.");
                        return true;
                    }
                    
                    //Add amount to village
                    Village v = VillageUtils.getPlayerVillage((Player) cs);
                    
                    if(v == null) {
                        SendMessage(cs, ChatError + "You aren't in a Village.");
                        return true;
                    }
                    
                    v.addMoney(amount);
                    VillageUtils.economy.withdrawPlayer(cs.getName(), amount);
                    v.SendMessage(ChatImportant + ((Player) cs).getDisplayName() + ChatDefault + " deposited " + ChatImportant + VillageUtils.economy.format(amount) + ChatDefault + " into the Village bank.");
                    VillageUtils.SaveAllVillages();
                    return true;
                }
                
                if(command.equalsIgnoreCase("leave") && (cs instanceof Player)) {
                    village = VillageUtils.getPlayerVillage((Player) cs);
                    if(village == null) {
                        SendMessage(cs, ChatError + "You aren't in a Village.");
                        return true;
                    }
                    
                    if(village.isMayor((Player) cs)) {
                        SendMessage(cs, ChatError + "You are the mayor! you cannot leave the Village.");
                        return true;
                    }
                    
                    village.SendMessage(ChatImportant + ((Player) cs).getDisplayName() + ChatDefault + " left the Village!");
                    village.removeResident((Player) cs);
                    VillageUtils.SaveAllVillages();
                    return true;
                }
                
                if(command.equalsIgnoreCase("close") && (cs instanceof Player)) {
                    Village v = VillageUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        SendMessage(cs, ChatError + "You aren't in a Village.");
                        return true;
                    }
                    
                    if(!v.isMayor((Player) cs)) {
                        SendMessage(cs, ChatError + "You aren't the mayor! you cannot close the Village.");
                        return true;
                    }
                    
                    VillageUtils.economy.depositPlayer(cs.getName(), v.getMoney());
                    VillageDataManager.dataManager.deleteTown(v);
                    VillageUtils.SaveAllVillages();
                    return true;
                }
                
                if(command.equalsIgnoreCase("spawn") && (cs instanceof Player)) {
                    Village v = VillageUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        SendMessage(cs, ChatError + "You aren't in a Village.");
                        return true;
                    }
                    
                    ((Player) cs).teleport(v.getSpawnBlock());
                    SendMessage(cs, ChatDefault + "Going to the Village square.");
                    VillageUtils.SaveAllVillages();
                    return true;
                }
                
                if(command.equalsIgnoreCase("description")) {
                    if(args.length < 2) {
                        SendMessage(cs, ChatError + "Please enter a description message.");
                        return true;
                    }
                    
                    Village v = VillageUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        SendMessage(cs, ChatError + "You aren't in a Village.");
                        return true;
                    }
                    
                    Player s = (Player) cs;
                    if(!v.isMayor(s)) {
                        SendMessage(cs, ChatError + "Only the mayor can set the description.");
                        return true;
                    }
                    
                    String msg = "";
                    
                    for(int i = 1; i < args.length; i++) {
                        msg += args[i];
                        if(i < args.length - 1) {
                            msg += " ";
                        }
                    }
                    
                    v.SendMessage(ChatDefault + "The new Village description is: " + ChatImportant + ChatColor.ITALIC + msg);
                    v.setDescription(msg);
                    VillageUtils.SaveAllVillages();
                    return true;
                }
                
                if(command.equalsIgnoreCase("msg")) {
                    if(args.length < 2) {
                        SendMessage(cs, ChatError + "Please enter a message to send.");
                        return true;
                    }
                    
                    Village v = VillageUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        SendMessage(cs, ChatError + "You aren't in a Village.");
                        return true;
                    }
                    
                    String msg = ChatDefault + "[" + ChatImportant + v.getName() + ChatDefault + "] " +
                            ChatImportant + cs.getName() + ChatDefault + ": ";
                    
                    for(int i = 1; i < args.length; i++) {
                        msg += args[i];
                        if(i < args.length - 1) {
                            msg += " ";
                        }
                    }
                    
                    v.SendMessage(msg);
                    return true;
                }
                
                if(command.equalsIgnoreCase("kick") && (cs instanceof Player)) {
                    if(args.length < 2) {
                        SendMessage(cs, ChatError + "Enter the name of the player to kick.");
                        return true;
                    }
                    
                    Village v = VillageUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        SendMessage(cs, ChatError + "You aren't in a Village.");
                        return true;
                    }
                    
                    Player s = (Player) cs;
                    if(!v.isMayor(s)) {
                        SendMessage(cs, ChatError + "Only the mayor can kick players.");
                        return true;
                    }
                    
                    OfflinePlayer p = Bukkit.getPlayer(args[1]);
                    if(p == null || !VillageUtils.canSee(cs, p.getPlayer())) {
                        p = Bukkit.getOfflinePlayer(args[1]);
                    }
                    
                    if(v.isMayor(p)) {
                        SendMessage(cs, ChatError + "You cannot kick the mayor of the Village!");
                        return true;
                    }
                    
                    v.SendMessage(ChatImportant + ((Player) p).getDisplayName() + ChatDefault + " was kicked from the Village!");
                    v.removeResident(p);
                    VillageUtils.SaveAllVillages();
                    return true;
                }
                
                if(command.equalsIgnoreCase("expand")) {
                    //Expanding the town
                    if(args.length < 2) {
                        SendMessage(cs, ChatError + "Enter the amount of chunks to expand.");
                        return true;
                    }
                    
                    Village v = VillageUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        SendMessage(cs, ChatError + "You aren't in a Village.");
                        return true;
                    }
                    
                    Player s = (Player) cs;
                    if(!v.isMayor(s)) {
                        SendMessage(cs, ChatError + "Only the mayor can expand the village.");
                        return true;
                    }
                    
                    int amount = -1;
                    try {
                         amount = Integer.parseInt(args[1]);
                    } catch (Exception ex) {
                        SendMessage(cs, ChatError + "Amount of chunks must be a number.");
                        return true;
                    }
                    
                    double cost = VillageDataManager.config.getDouble("cost.expand");
                    double totalcost = cost * amount;
                    //Ensure town has enough
                    if(totalcost > v.getMoney()) {
                        SendMessage(cs, ChatError + "You don't have enough money in the Village bank! you need " + VillageUtils.economy.format(totalcost) + " to do this.");
                        return true;
                    }
                    
                    //Expand town//
                    int oldsize = v.getTownSize();
                    
                    v.setTownSize(v.getTownSize() + amount);
                    
                    if(VillageUtils.townsOverlap(v)) {
                        v.setTownSize(oldsize);
                        SendMessage(cs, ChatError + "Cannot Expand town this much, it overlaps another Village!");
                        return true;
                    }
                    v.addMoney(-totalcost);
                    v.SendMessage(ChatDefault + "The town was expanded another " + ChatImportant + amount + ChatDefault + " chunk(s).");
                    VillageUtils.SaveAllVillages();
                    return true;
                }
                
                village = VillageUtils.getVillage(args[0]);
            }
            
            if(village == null && (cs instanceof Player) &&  VillageUtils.getPlayerVillage((Player) cs) == null) {
                SendMessage(cs, ChatError + "You aren't part of a Village.");
                SendMessage(cs, VillageUtils.getCommandDescription(cmd.getName()));
                return true;
            }
            
            if(village == null) {
                SendMessage(cs, ChatError + "Couldn't find Village.");
                SendMessage(cs, VillageUtils.getCommandDescription(cmd.getName()));
                return true;
            }
            
            String residents = "";
            for(int i = 0; i < village.getResidents().size(); i++) {
                residents += village.getResidents().get(i).getName();
                if(i < (village.getResidents().size() - 1)) {
                    residents += ", ";
                }
            }
            
            List<String> msg = new ArrayList<String>();
            
            msg.add(ChatDefault + "Information about " + ChatImportant + village.getName() + ChatDefault + ":");
            msg.add(ChatImportant + ChatColor.ITALIC + village.getDescription() + ChatColor.RESET + ChatDefault + " - Mayor " + village.getMayor().getName());
            msg.add(ChatDefault + "Residents (" + ChatImportant + village.getResidents().size() + ChatDefault + "): " + residents);
            
            if(VillageDataManager.useEconomy()) {
                msg.add(ChatDefault + "Village Wealth: " + ChatImportant + VillageUtils.economy.format(village.getMoney()));
            }
            
            msg.add(ChatDefault + "Located at: " + ChatImportant + VillageUtils.getStringLocation(village.getTownSpawn()));
            msg.add(ChatDefault + "Village Size: " + ChatImportant + village.getTownSize());
            
            SendMessage(cs, msg);
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("createvillage")) {
            if(!(cs instanceof Player)) {
                SendMessage(cs, ChatError + "Only players can run this command.");
                SendMessage(cs, VillageUtils.getCommandDescription(cmd.getName()));
                return true;
            }
            
            if(args.length < 1) {
                SendMessage(cs, ChatError + "Please enter a Village name!");
                SendMessage(cs, VillageUtils.getCommandDescription(cmd.getName()));
                return true;
            }
            
            Player sender = (Player) cs;
            
            //Ensure player has enough cash
            if(VillageDataManager.useEconomy()) {
                //Get Money player has
                double cash = VillageUtils.economy.getBalance(sender.getName());
                double townCost = VillageDataManager.CreateVillageCost();
                if(cash < townCost) {
                    SendMessage(cs, ChatError + "You dont have the " + VillageUtils.economy.format(townCost) + " necessary to make a Village.");
                    return true;
                }
            }
            
            //Make sure name is valid
            String name = args[0];
            if(!name.matches("^[a-zA-Z0-9]*$")) {
                SendMessage(cs, ChatError + "Invalid Village name, name can only have letters and/or numbers.");
                return true;
            }
            
            //Check town isnt a reserved name//
            String[] reservedNames = {
                "deposit",
                "kick",
                "withdraw",
                "spawn",
                "close",
                "leave"
            };
            
            for(String s : reservedNames) {
                if(name.equalsIgnoreCase(s)) {
                    SendMessage(cs, ChatError + "Invalid Village name, name is reserved.");
                    return true;
                }
            }
            
            //Check town doesnt exist/
            Village oldtown = VillageUtils.getVillageExact(name);
            if(oldtown != null) {
                SendMessage(cs, ChatError + "The name " + name + " is already a Village name, please choose something else.");
                return true;
            }
            
            //Check user isn't in a town already
            oldtown = VillageUtils.getPlayerVillage(sender);
            if(oldtown != null) {
                SendMessage(cs, ChatError + "You are already in the Village " + oldtown.getName() + ", please leave it first before starting a new one.");
                return true;
            }
            
            //Check Surrounding areas for towns
            if(VillageUtils.isChunkInATownsArea(sender.getLocation().getChunk())) {
                SendMessage(cs, ChatError + "Can't create a Village here, another Village is too close by.");
                return true;
            }
            
            //All Good, make town and charge player//
            if(VillageDataManager.useEconomy()) {
                //Get Money player has
                double cash = VillageUtils.economy.getBalance(sender.getName());
                double townCost = VillageDataManager.CreateVillageCost();
                VillageUtils.economy.withdrawPlayer(sender.getName(), townCost);
            }
            
            Village newtown = new Village(name);
            newtown.setCreatedDate(VillageUtils.getNow());
            newtown.setDescription("Welcome to " + name + "!");
            newtown.setMayor(sender);
            newtown.setMoney(0);
            newtown.setTownSpawn(sender.getLocation().getChunk());
            newtown.setTownSize(1);
            
            Broadcast(ChatImportant + sender.getDisplayName() + ChatDefault + " created the Village " + ChatImportant + name + ChatDefault + "!");
            VillageUtils.villages.add(newtown);
            VillageUtils.SaveAllVillages();
            
            sender.teleport(newtown.getSpawnBlock().getBlock().getLocation());
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("villageinvite")) {
            if(!(cs instanceof Player)) {
                SendMessage(cs, ChatError + "Only players can run this command.");
                return true;
            }
            
            if(args.length < 1) {
                SendMessage(cs, ChatError + "Please enter a player name to invite.");
                SendMessage(cs, VillageUtils.getCommandDescription(cmd.getName()));
                return true;
            }
            
            Player sender = (Player) cs;
            
            //Get The Senders Village
            Village village = VillageUtils.getPlayerVillage(sender);
            if(village == null) {
                SendMessage(cs, ChatError + "You aren't part of a Village you can't invite any players.");
                return true;
            }
            
            Player p = VillageUtils.getPlayer(cs, args[0]);
            if(p == null) {
                SendMessage(cs, ChatError + args[0] + " isn't online.");
                return true;
            }
            
            Village tp = VillageUtils.getPlayerVillage(p);
            if(tp != null) {
                SendMessage(cs, ChatError + p.getDisplayName() + " is already in a Village.");
                return true;
            }
            
            VillageUtils.townInvites.put(p, village);
            SendMessage(p, ChatImportant + sender.getDisplayName() + ChatDefault + " has invited you to join " + ChatImportant + village.getName() + ChatDefault + ".");
            SendMessage(p, ChatDefault + "Type " + ChatImportant + "/villageaccept" + ChatDefault + " or " + ChatImportant + "/villagedeny");
            SendMessage(sender, ChatDefault + "Invited " + ChatImportant + p.getDisplayName() + ChatDefault + " to the Village.");
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("villageaccept")) {
            if(!(cs instanceof Player)) {
                SendMessage(cs, ChatError + "Only players can run this command.");
                return true;
            }
            
            Player sender = (Player) cs;
            
            if(!VillageUtils.townInvites.containsKey(sender)) {
                SendMessage(cs, ChatError + "You haven't recieved an invite.");
                return true;
            }
            
            Village v = VillageUtils.townInvites.get(sender);
            if(v == null) {
                SendMessage(cs, ChatError + "You haven't recieved an invite.");
                return true;
            }
            
            Village vs = VillageUtils.getPlayerVillage(sender);
            if(vs != null) {
                SendMessage(cs, ChatError + "You are already part of a Village. Leave it before accepting another invite.");
                return true;
            }
            
            v.addResident(sender);
            v.SendMessage(ChatImportant + sender.getDisplayName() + ChatDefault + " joined the Village!");
            sender.teleport(v.getSpawnBlock());
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("villagedeny")) {
            if(!(cs instanceof Player)) {
                SendMessage(cs, ChatError + "Only players can run this command.");
                return true;
            }
            
            Player sender = (Player) cs;
            
            if(!VillageUtils.townInvites.containsKey(sender)) {
                SendMessage(cs, ChatError + "You haven't recieved an invite.");
                return true;
            }
            
            Village v = VillageUtils.townInvites.get(sender);
            if(v == null) {
                SendMessage(cs, ChatError + "You haven't recieved an invite.");
                return true;
            }
            
            sender.sendMessage(ChatDefault + "Denied the invite.");
            VillageUtils.townInvites.remove(sender);
            return true;
        }
        
        return false;
    }
}

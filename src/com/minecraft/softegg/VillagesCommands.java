package com.minecraft.softegg;

import static com.minecraft.softegg.VillageBase.ChatDefault;
import static com.minecraft.softegg.VillageBase.ChatError;
import static com.minecraft.softegg.VillageBase.ChatImportant;
import static com.minecraft.softegg.VillageBase.SendMessage;
import static com.minecraft.softegg.VillageBase.gK;
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
                    SendMessage(cs, gK("neednamedelete"));
                    return true;
                }
                
                String name = args[1];
                Village village = VillageUtils.getVillage(name);
                if(village == null) {
                    SendMessage(cs, gK("villagedoesntexist"));
                    return true;
                }
                
                SendMessage(cs, gK("villagedelete", village));
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
                    SendMessage(cs, gK("needvillagename"));
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
                        SendMessage(cs, gK("enteramount"));
                        return true;
                    }
                    
                    double amount = -1;
                    try {
                        amount = Double.parseDouble(args[1]);
                    } catch(Exception ex) {
                        SendMessage(cs, gK("mustbenumber"));
                        return true;
                    }
                    
                    if(amount <= 0) {
                        SendMessage(cs, gK("mustbeone"));
                        return true;
                    }
                    
                    //Ensure player has enough money to deposit
                    double pAmount = VillageUtils.economy.getBalance(cs.getName());
                    
                    if(pAmount < amount) {
                        SendMessage(cs, gK("notenoughmoney", amount));
                        return true;
                    }
                    
                    //Add amount to village
                    Village v = VillageUtils.getPlayerVillage((Player) cs);
                    
                    if(v == null) {
                        SendMessage(cs, gK("notinvillage"));
                        return true;
                    }
                    
                    v.addMoney(amount);
                    VillageUtils.economy.withdrawPlayer(cs.getName(), amount);
                    v.SendMessage(gK("depositedmoney", (Player) cs, amount));
                    VillageUtils.SaveAllVillages();
                    return true;
                }
                
                if(command.equalsIgnoreCase("leave") && (cs instanceof Player)) {
                    village = VillageUtils.getPlayerVillage((Player) cs);
                    if(village == null) {
                        SendMessage(cs, gK("notinvillage"));
                        return true;
                    }
                    
                    if(village.isMayor((Player) cs)) {
                        SendMessage(cs, gK("leavevillagemayor"));
                        return true;
                    }
                    
                    village.SendMessage(gK("leftvillage", (Player) cs));
                    village.removeResident((Player) cs);
                    VillageUtils.SaveAllVillages();
                    return true;
                }
                
                if(command.equalsIgnoreCase("close") && (cs instanceof Player)) {
                    Village v = VillageUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        SendMessage(cs, gK("notinvillage"));
                        return true;
                    }
                    
                    if(!v.isMayor((Player) cs)) {
                        SendMessage(cs, gK("closevillagenotmayor"));
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
                        SendMessage(cs, gK("notinvillage"));
                        return true;
                    }
                    
                    ((Player) cs).teleport(v.getSpawnBlock());
                    SendMessage(cs, gK("goingtovillage"));
                    VillageUtils.SaveAllVillages();
                    return true;
                }
                
                if(command.equalsIgnoreCase("description")) {
                    if(args.length < 2) {
                        SendMessage(cs, gK("enterdescription"));
                        return true;
                    }
                    
                    Village v = VillageUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        SendMessage(cs, gK("notinvillage"));
                        return true;
                    }
                    
                    Player s = (Player) cs;
                    if(!v.isMayor(s)) {
                        SendMessage(cs, gK("notmayordescription"));
                        return true;
                    }
                    
                    String msg = "";
                    
                    for(int i = 1; i < args.length; i++) {
                        msg += args[i];
                        if(i < args.length - 1) {
                            msg += " ";
                        }
                    }
                    
                    v.SendMessage(gK("newdescription").replaceAll("%description%", ChatColor.ITALIC + msg));
                    v.setDescription(msg);
                    VillageUtils.SaveAllVillages();
                    return true;
                }
                
                if(command.equalsIgnoreCase("msg")) {
                    if(args.length < 2) {
                        SendMessage(cs, gK("entermessage"));
                        return true;
                    }
                    
                    Village v = VillageUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        SendMessage(cs, gK("notinvillage"));
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
                        SendMessage(cs, gK("enterkickname"));
                        return true;
                    }
                    
                    Village v = VillageUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        SendMessage(cs, gK("notinvillage"));
                        return true;
                    }
                    
                    Player s = (Player) cs;
                    if(!v.isMayor(s)) {
                        SendMessage(cs, gK("mayorkickonly"));
                        return true;
                    }
                    
                    OfflinePlayer p = Bukkit.getPlayer(args[1]);
                    if(p == null || !VillageUtils.canSee(cs, p.getPlayer())) {
                        p = Bukkit.getOfflinePlayer(args[1]);
                    }
                    
                    if(v.isMayor(p)) {
                        SendMessage(cs, gK("cantkickmayor"));
                        return true;
                    }
                    
                    if(!v.isResident(p)) {
                        SendMessage(cs, gK("notresident").replaceAll("%p%", args[0]));
                        return true;
                    }
                    
                    v.SendMessage(gK("residentkicked", p));
                    v.removeResident(p);
                    VillageUtils.SaveAllVillages();
                    return true;
                }
                
                if(command.equalsIgnoreCase("expand")) {
                    //Expanding the town
                    if(args.length < 2) {
                        SendMessage(cs, gK("enterchunks"));
                        return true;
                    }
                    
                    Village v = VillageUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        SendMessage(cs, gK("notinvillage"));
                        return true;
                    }
                    
                    Player s = (Player) cs;
                    if(!v.isMayor(s)) {
                        SendMessage(cs, gK("onlymayorexpand"));
                        return true;
                    }
                    
                    int amount = -1;
                    try {
                         amount = Integer.parseInt(args[1]);
                    } catch (Exception ex) {
                        SendMessage(cs, gK("mustbenumber"));
                        return true;
                    }
                    
                    if(amount < 1) {
                        SendMessage(cs, gK("mustbeone"));
                        return true;
                    }
                    
                    double cost = VillageDataManager.config.getDouble("cost.expand");
                    double totalcost = cost * amount;
                    //Ensure town has enough
                    if(totalcost > v.getMoney() && VillageDataManager.useEconomy()) {
                        SendMessage(cs, gK("villagebankneedmore", totalcost));
                        return true;
                    }
                    
                    //Expand town//
                    int oldsize = v.getTownSize();
                    
                    v.setTownSize(v.getTownSize() + amount);
                    
                    if(VillageUtils.townsOverlap(v)) {
                        v.setTownSize(oldsize);
                        SendMessage(cs, gK("expandvillageoverlap"));
                        return true;
                    }
                    if(VillageDataManager.useEconomy()) {
                        v.addMoney(-totalcost);
                    }
                    v.SendMessage(gK("villageexpanded").replaceAll("%n%", "" + amount));
                    VillageUtils.SaveAllVillages();
                    return true;
                }
                
                village = VillageUtils.getVillage(args[0]);
            }
            
            if(village == null && (cs instanceof Player) &&  VillageUtils.getPlayerVillage((Player) cs) == null) {
                SendMessage(cs, gK("notinvillage"));
                SendMessage(cs, VillageUtils.getCommandDescription(cmd.getName()));
                return true;
            }
            
            if(village == null) {
                SendMessage(cs, gK("cantfindvillage"));
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
                SendMessage(cs, gK("playeronly"));
                SendMessage(cs, VillageUtils.getCommandDescription(cmd.getName()));
                return true;
            }
            
            if(args.length < 1) {
                SendMessage(cs, gK("entervillagename"));
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
                    SendMessage(cs, gK("notenoughmoney", townCost));
                    return true;
                }
            }
            
            //Make sure name is valid
            String name = args[0];
            if(!name.matches("^[a-zA-Z0-9]*$")) {
                SendMessage(cs, gK("invalidvillagename"));
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
                    SendMessage(cs, gK("villagenameused"));
                    return true;
                }
            }
            
            //Check town doesnt exist/
            Village oldtown = VillageUtils.getVillageExact(name);
            if(oldtown != null) {
                SendMessage(cs, gK("villagenameused"));
                return true;
            }
            
            //Check user isn't in a town already
            oldtown = VillageUtils.getPlayerVillage(sender);
            if(oldtown != null) {
                SendMessage(cs, gK("alreadyinvillage"));
                return true;
            }
            
            //Check Surrounding areas for towns
            if(VillageUtils.isChunkInATownsArea(sender.getLocation().getChunk())) {
                SendMessage(cs, gK("createvillageoverlap"));
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
            
            Broadcast(gK("createdvillage", newtown).replaceAll("%p%", sender.getName()));
            VillageUtils.villages.add(newtown);
            VillageUtils.SaveAllVillages();
            
            sender.teleport(newtown.getSpawnBlock().getBlock().getLocation());
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("villageinvite")) {
            if(!(cs instanceof Player)) {
                SendMessage(cs, gK("playeronly"));
                return true;
            }
            
            if(args.length < 1) {
                SendMessage(cs, gK("enterplayer"));
                SendMessage(cs, VillageUtils.getCommandDescription(cmd.getName()));
                return true;
            }
            
            Player sender = (Player) cs;
            
            //Get The Senders Village
            Village village = VillageUtils.getPlayerVillage(sender);
            if(village == null) {
                SendMessage(cs, gK("notinvillage"));
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
            SendMessage(p, gK(gK("villageinvite", village), sender));
            SendMessage(p, ChatDefault + "Type " + ChatImportant + "/villageaccept" + ChatDefault + " or " + ChatImportant + "/villagedeny");
            SendMessage(sender, gK("residentinvited", p));
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("villageaccept")) {
            if(!(cs instanceof Player)) {
                SendMessage(cs, gK("playeronly"));
                return true;
            }
            
            Player sender = (Player) cs;
            
            if(!VillageUtils.townInvites.containsKey(sender)) {
                SendMessage(cs, gK("noinvite"));
                return true;
            }
            
            Village v = VillageUtils.townInvites.get(sender);
            if(v == null) {
                SendMessage(cs, gK("noinvite"));
                return true;
            }
            
            Village vs = VillageUtils.getPlayerVillage(sender);
            if(vs != null) {
                SendMessage(cs, gK("alreadyinvillage"));
                return true;
            }
            
            v.addResident(sender);
            v.SendMessage(gK("joinedvillage", sender));
            sender.teleport(v.getSpawnBlock());
            return true;
        }
        
        if(cmd.getName().equalsIgnoreCase("villagedeny")) {
            if(!(cs instanceof Player)) {
                SendMessage(cs, gK("playeronly"));
                return true;
            }
            
            Player sender = (Player) cs;
            
            if(!VillageUtils.townInvites.containsKey(sender)) {
                SendMessage(cs, gK("noinvite"));
                return true;
            }
            
            Village v = VillageUtils.townInvites.get(sender);
            if(v == null) {
                SendMessage(cs, gK("noinvite"));
                return true;
            }
            
            sender.sendMessage(ChatDefault + "Denied the invite.");
            VillageUtils.townInvites.remove(sender);
            return true;
        }
        
        return false;
    }
}

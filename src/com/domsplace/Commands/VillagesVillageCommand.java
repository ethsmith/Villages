package com.domsplace.Commands;

import com.domsplace.DataManagers.*;
import com.domsplace.Utils.*;
import com.domsplace.*;
import com.domsplace.Objects.*;
import static com.domsplace.VillageBase.gK;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VillagesVillageCommand extends VillageBase implements CommandExecutor {
    
    private VillagesPlugin plugin;
    
    public VillagesVillageCommand (VillagesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String string, String[] args) {
        if(cmd.getName().equalsIgnoreCase("village")) {
            if(!(cs instanceof Player)) {
                if(args.length == 0) {
                    VillageUtils.msgPlayer(cs, gK("needvillagename"));
                    VillageUtils.msgPlayer(cs, VillageUtils.getCommandDescription(cmd.getName()));
                    return true;
                }
            }
            
            Village village = null;
            if(args.length == 0) {
                village = VillageVillagesUtils.getPlayerVillage((Player) cs);
            } else if(args.length >= 1) {
                String command = args[0].toLowerCase();
                
                if(command.equalsIgnoreCase("deposit") && VillageUtils.useEconomy && (cs instanceof Player)) {
                    if(args.length < 2) {
                        VillageUtils.msgPlayer(cs, gK("enteramount"));
                        return true;
                    }
                    
                    double amount = -1;
                    try {
                        amount = Double.parseDouble(args[1]);
                    } catch(Exception ex) {
                        VillageUtils.msgPlayer(cs, gK("mustbenumber"));
                        return true;
                    }
                    
                    if(amount <= 0) {
                        VillageUtils.msgPlayer(cs, gK("mustbeone"));
                        return true;
                    }
                    
                    //Ensure player has enough money to deposit
                    double pAmount = VillageEconomyUtils.economy.getBalance(cs.getName());
                    
                    if(pAmount < amount) {
                        VillageUtils.msgPlayer(cs, gK("notenoughmoney", amount));
                        return true;
                    }
                    
                    //Add amount to village
                    Village v = VillageVillagesUtils.getPlayerVillage((Player) cs);
                    
                    if(v == null) {
                        VillageUtils.msgPlayer(cs, gK("notinvillage"));
                        return true;
                    }
                    
                    v.addMoney(amount);
                    VillageEconomyUtils.economy.withdrawPlayer(cs.getName(), amount);
                    v.SendMessage(gK("depositedmoney", (Player) cs, amount));
                    VillageVillagesUtils.SaveAllVillages();
                    return true;
                }
                
                if(command.equalsIgnoreCase("leave") && (cs instanceof Player)) {
                    village = VillageVillagesUtils.getPlayerVillage((Player) cs);
                    if(village == null) {
                        VillageUtils.msgPlayer(cs, gK("notinvillage"));
                        return true;
                    }
                    
                    if(village.isMayor((Player) cs)) {
                        VillageUtils.msgPlayer(cs, gK("leavevillagemayor"));
                        return true;
                    }
                    
                    village.SendMessage(gK("leftvillage", (Player) cs));
                    village.removeResident((Player) cs);
                    VillageVillagesUtils.SaveAllVillages();
                    return true;
                }
                
                if(command.equalsIgnoreCase("close") && (cs instanceof Player)) {
                    Village v = VillageVillagesUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        VillageUtils.msgPlayer(cs, gK("notinvillage"));
                        return true;
                    }
                    
                    if(!v.isMayor((Player) cs)) {
                        VillageUtils.msgPlayer(cs, gK("closevillagenotmayor"));
                        return true;
                    }
                    
                    if(VillageUtils.useEconomy) {
                        VillageEconomyUtils.economy.depositPlayer(cs.getName(), v.getMoney());
                    }
                    VillageVillagesUtils.DeleteVillage(v);
                    VillageVillagesUtils.SaveAllVillages();
                    return true;
                }
                
                if(command.equalsIgnoreCase("spawn") && (cs instanceof Player)) {
                    Village v = VillageVillagesUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        VillageUtils.msgPlayer(cs, gK("notinvillage"));
                        return true;
                    }
                    
                    ((Player) cs).teleport(v.getSpawnBlock());
                    VillageUtils.msgPlayer(cs, gK("goingtovillage"));
                    VillageVillagesUtils.SaveAllVillages();
                    return true;
                }
                
                if(command.equalsIgnoreCase("description")) {
                    if(args.length < 2) {
                        VillageUtils.msgPlayer(cs, gK("enterdescription"));
                        return true;
                    }
                    
                    Village v = VillageVillagesUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        VillageUtils.msgPlayer(cs, gK("notinvillage"));
                        return true;
                    }
                    
                    Player s = (Player) cs;
                    if(!v.isMayor(s)) {
                        VillageUtils.msgPlayer(cs, gK("notmayordescription"));
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
                    VillageVillagesUtils.SaveAllVillages();
                    return true;
                }
                
                if(command.equalsIgnoreCase("msg")) {
                    if(args.length < 2) {
                        VillageUtils.msgPlayer(cs, gK("entermessage"));
                        return true;
                    }
                    
                    Village v = VillageVillagesUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        VillageUtils.msgPlayer(cs, gK("notinvillage"));
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
                        VillageUtils.msgPlayer(cs, gK("enterkickname"));
                        return true;
                    }
                    
                    Village v = VillageVillagesUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        VillageUtils.msgPlayer(cs, gK("notinvillage"));
                        return true;
                    }
                    
                    Player s = (Player) cs;
                    if(!v.isMayor(s)) {
                        VillageUtils.msgPlayer(cs, gK("mayorkickonly"));
                        return true;
                    }
                    
                    OfflinePlayer p = Bukkit.getPlayer(args[1]);
                    if(p == null || !VillageUtils.canSee(cs, p.getPlayer())) {
                        p = Bukkit.getOfflinePlayer(args[1]);
                    }
                    
                    if(v.isMayor(p)) {
                        VillageUtils.msgPlayer(cs, gK("cantkickmayor"));
                        return true;
                    }
                    
                    if(!v.isResident(p)) {
                        VillageUtils.msgPlayer(cs, gK("notresident").replaceAll("%p%", args[0]));
                        return true;
                    }
                    
                    v.SendMessage(gK("residentkicked", p));
                    v.removeResident(p);
                    VillageVillagesUtils.SaveAllVillages();
                    return true;
                }
                
                if(command.equalsIgnoreCase("expand")) {
                    //Expanding the town
                    if(args.length < 2) {
                        VillageUtils.msgPlayer(cs, gK("enterchunks"));
                        return true;
                    }
                    
                    Village v = VillageVillagesUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        VillageUtils.msgPlayer(cs, gK("notinvillage"));
                        return true;
                    }
                    
                    Player s = (Player) cs;
                    if(!v.isMayor(s)) {
                        VillageUtils.msgPlayer(cs, gK("onlymayorexpand"));
                        return true;
                    }
                    
                    int amount = -1;
                    try {
                         amount = Integer.parseInt(args[1]);
                    } catch (Exception ex) {
                        VillageUtils.msgPlayer(cs, gK("mustbenumber"));
                        return true;
                    }
                    
                    if(amount < 1) {
                        VillageUtils.msgPlayer(cs, gK("mustbeone"));
                        return true;
                    }
                    
                    if(amount > 5) {
                        VillageUtils.msgPlayer(cs, gK("maxofthreeexpand"));
                        return true;
                    }
                    
                    //Determine the amount of chunks I am expanding
                    int chunksToExpand = v.getTownLocalBorderChunks(amount).size();
                    
                    double cost = VillageConfigManager.config.getDouble("cost.expand") * chunksToExpand;
                    double totalcost = cost * amount;
                    //Ensure town has enough
                    if(totalcost > v.getMoney() && VillageUtils.useEconomy) {
                        VillageUtils.msgPlayer(cs, gK("villagebankneedmore", totalcost));
                        return true;
                    }
                    
                    //Expand town//
                    int oldsize = v.getTownSize();
                    
                    VillageUtils.msgConsole(ChatImportant + v.getName() + ChatDefault + " is expanding " + chunksToExpand + " chunks.");
                    
                    //Msg player, since this lags the server a tad
                    VillageUtils.msgPlayer(cs, gK("expandingvillage"));
                    
                    //Set new size
                    v.setTownSize(v.getTownSize() + amount);
                    
                    if(VillageVillagesUtils.doVillagesOverlap(v)) {
                        v.setTownSize(oldsize);
                        VillageUtils.msgPlayer(cs, gK("expandvillageoverlap"));
                        return true;
                    }
                    
                    if(VillageVillagesUtils.doesVillageOverlapRegion(v)) {
                        v.setTownSize(oldsize);
                        VillageUtils.msgPlayer(cs, gK("expandregionoverlap"));
                        return true;
                    }
                    
                    if(VillageUtils.useEconomy) {
                        v.addMoney(-totalcost);
                    }
                    v.SendMessage(gK("villageexpanded").replaceAll("%n%", "" + amount));
                    VillageVillagesUtils.SaveAllVillages();
                    return true;
                }
                
                if(command.equalsIgnoreCase("bank") && (cs instanceof Player)) {
                    Player p = (Player) cs;
                    Village v = VillageVillagesUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        VillageUtils.msgPlayer(cs, gK("notinvillage"));
                        return true;
                    }
                    
                    v.getItemBank().OpenAsInventory(p);
                    return true;
                }
                
                village = VillageVillagesUtils.getVillage(args[0]);
            }
            
            if(village == null && (cs instanceof Player) &&  VillageVillagesUtils.getPlayerVillage((Player) cs) == null) {
                VillageUtils.msgPlayer(cs, gK("notinvillage"));
                VillageUtils.msgPlayer(cs, VillageUtils.getCommandDescription(cmd.getName()));
                return true;
            }
            
            if(village == null) {
                VillageUtils.msgPlayer(cs, gK("cantfindvillage"));
                VillageUtils.msgPlayer(cs, VillageUtils.getCommandDescription(cmd.getName()));
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
            
            if(VillageUtils.useEconomy) {
                msg.add(ChatDefault + "Village Wealth: " + ChatImportant + VillageEconomyUtils.economy.format(village.getMoney()));
            }
            
            msg.add(ChatDefault + "Located at: " + ChatImportant + VillageUtils.getStringLocation(village.getTownSpawn()));
            msg.add(ChatDefault + "Village Size: " + ChatImportant + village.getTownSize());
            
            VillageUtils.msgPlayer(cs, msg);
            return true;
        }
        
        return false;
    }
    
}

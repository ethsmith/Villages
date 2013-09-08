package com.domsplace.Villages.Commands;

import com.domsplace.Villages.Objects.Village;

import com.domsplace.Villages.Utils.VillageEconomyUtils;
import com.domsplace.Villages.Utils.VillageUtils;
import com.domsplace.Villages.Bases.CommandBase;
import com.domsplace.Villages.Hooks.WorldGuardHook;
import com.domsplace.Villages.Objects.SubCommand;
import com.domsplace.Villages.Objects.SubCommand;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VillageCommand extends CommandBase {
    public VillageCommand () {
        super("village");
        this.addSubCommand(SubCommand.make(SubCommand.VILLAGE));
        this.addSubCommand(SubCommand.make("close"));
        this.addSubCommand(SubCommand.make("leave"));
        this.addSubCommand(SubCommand.make("spawn"));
        this.addSubCommand(SubCommand.make("bank"));
        this.addSubCommand(SubCommand.make("explode"));
        this.addSubCommand(SubCommand.make("kick", SubCommand.PLAYER));
        this.addSubCommand(SubCommand.make("lookup", SubCommand.PLAYER));
        this.addSubCommand(SubCommand.make("msg", "message"));
        this.addSubCommand(SubCommand.make("expand", "amount"));
        this.addSubCommand(SubCommand.make("description", "message"));
        
        if(UsePlots) {
            this.addSubCommand(SubCommand.make("plot",
                    SubCommand.make("check"),
                    SubCommand.make("set", 
                        SubCommand.make("owner", SubCommand.PLAYER),
                        SubCommand.make("price", "amount")
                    ),
                    SubCommand.make("claim")
            ));
        }
        
        if(getConfigManager().useEconomy) {
            this.addSubCommand(SubCommand.make("withdraw", "amount"));
            this.addSubCommand(SubCommand.make("withdrawl", "amount"));
            this.addSubCommand(SubCommand.make("deposit", "amount"));
        }
    }

    @Override
    public boolean cmd(CommandSender cs, Command cmd, String string, String[] args) {
        if(!isPlayer(cs)) {
            if(args.length == 0) {
                msgPlayer(cs, gK("needvillagename"));
            return false;
            }
        }

        Village village = null;
        if(args.length == 0) {
            village = VillageUtils.getPlayerVillage((Player) cs);
        } else if(args.length >= 1) {
            String command = args[0].toLowerCase();

            if(command.equalsIgnoreCase("deposit") && getConfigManager().useEconomy && isPlayer(cs)) {
                if(args.length < 2) {
                    msgPlayer(cs, gK("enteramount"));
                    return true;
                }

                double amount = -1;
                try {
                    amount = Double.parseDouble(args[1]);
                } catch(Exception ex) {
                    msgPlayer(cs, gK("mustbenumber"));
                    return true;
                }

                if(amount <= 0) {
                    msgPlayer(cs, gK("mustbeone"));
                    return true;
                }

                //Ensure player has enough money to deposit
                double pAmount = VillageEconomyUtils.economy.getBalance(cs.getName());

                if(pAmount < amount) {
                    msgPlayer(cs, gK("notenoughmoney", amount));
                    return true;
                }

                //Add amount to village
                Village v = VillageUtils.getPlayerVillage((Player) cs);

                if(v == null) {
                    msgPlayer(cs, gK("notinvillage"));
                    return true;
                }

                v.addMoney(amount);
                VillageEconomyUtils.economy.withdrawPlayer(cs.getName(), amount);
                v.sendMessage(gK("depositedmoney", (Player) cs, amount));
                saveAllData();
                return true;
            }

            if((command.equalsIgnoreCase("withdraw") || command.equalsIgnoreCase("withdrawl")) && getConfigManager().useEconomy && (cs instanceof Player)) {
                if(args.length < 2) {
                    msgPlayer(cs, gK("enteramount"));
                    return true;
                }

                double amount = -1;
                try {
                    amount = Double.parseDouble(args[1]);
                } catch(Exception ex) {
                    msgPlayer(cs, gK("mustbenumber"));
                    return true;
                }

                if(amount <= 0) {
                    msgPlayer(cs, gK("mustbeone"));
                    return true;
                }

                //Ensure player has enough money to deposit
                double pAmount = VillageEconomyUtils.economy.getBalance(cs.getName());

                if(pAmount < amount) {
                    msgPlayer(cs, gK("notenoughmoney", amount));
                    return true;
                }

                //Add amount to village
                Village v = VillageUtils.getPlayerVillage((Player) cs);

                if(v == null) {
                    msgPlayer(cs, gK("notinvillage"));
                    return true;
                }

                v.addMoney(-amount);
                VillageEconomyUtils.economy.depositPlayer(cs.getName(), amount);
                v.sendMessage(gK("withdrawledmoney", (Player) cs, amount));
                saveAllData();
                return true;
            }

            if(command.equalsIgnoreCase("leave") && (cs instanceof Player)) {
                village = VillageUtils.getPlayerVillage((Player) cs);
                if(village == null) {
                    msgPlayer(cs, gK("notinvillage"));
                    return true;
                }

                if(village.isMayor((Player) cs)) {
                    msgPlayer(cs, gK("leavevillagemayor"));
                    return true;
                }

                village.sendMessage(gK("leftvillage", (Player) cs));
                village.removeResident((Player) cs);
                saveAllData();
                return true;
            }

            if(command.equalsIgnoreCase("close") && (cs instanceof Player)) {
                Village v = VillageUtils.getPlayerVillage((Player) cs);
                if(v == null) {
                    msgPlayer(cs, gK("notinvillage"));
                    return true;
                }

                if(!v.isMayor((Player) cs)) {
                    msgPlayer(cs, gK("closevillagenotmayor"));
                    return true;
                }

                if(getConfigManager().useEconomy) {
                    VillageEconomyUtils.economy.depositPlayer(cs.getName(), v.getMoney());
                }
                getVillageManager().deleteVillage(v);
                saveAllData();
                return true;
            }

            if(command.equalsIgnoreCase("spawn") && (cs instanceof Player)) {
                if(!cs.hasPermission("Villages.villagespawn")) {
                    msgPlayer(cs, gK("nopermission"));
                    return true;
                }

                Village v = VillageUtils.getPlayerVillage((Player) cs);
                if(v == null) {
                    msgPlayer(cs, gK("notinvillage"));
                    return true;
                }

                ((Player) cs).teleport(v.getSpawnBlock());
                msgPlayer(cs, gK("goingtovillage"));
                saveAllData();
                return true;
            }

            if(command.equalsIgnoreCase("description")) {
                if(!(cs instanceof Player)) {
                    msgPlayer(cs, gK("playeronly"));
                    return true;
                }

                if(args.length < 2) {
                    msgPlayer(cs, gK("enterdescription"));
                    return true;
                }

                Village v = VillageUtils.getPlayerVillage((Player) cs);
                if(v == null) {
                    msgPlayer(cs, gK("notinvillage"));
                    return true;
                }

                Player s = (Player) cs;
                if(!v.isMayor(s)) {
                    msgPlayer(cs, gK("notmayordescription"));
                    return true;
                }

                String msg = "";

                for(int i = 1; i < args.length; i++) {
                    msg += args[i];
                    if(i < args.length - 1) {
                        msg += " ";
                    }
                }

                v.sendMessage(gK("newdescription").replaceAll("%description%", ChatColor.ITALIC + msg));
                v.setDescription(msg);
                saveAllData();
                return true;
            }

            if(command.equalsIgnoreCase("msg")) {
                if(!(cs instanceof Player)) {
                    msgPlayer(cs, gK("playeronly"));
                    return true;
                }

                if(args.length < 2) {
                    msgPlayer(cs, gK("entermessage"));
                    return true;
                }

                Village v = VillageUtils.getPlayerVillage((Player) cs);
                if(v == null) {
                    msgPlayer(cs, gK("notinvillage"));
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

                v.sendMessage(msg);
                return true;
            }

            if(command.equalsIgnoreCase("kick") && (cs instanceof Player)) {
                if(args.length < 2) {
                    msgPlayer(cs, gK("enterkickname"));
                    return true;
                }

                Village v = VillageUtils.getPlayerVillage((Player) cs);
                if(v == null) {
                    msgPlayer(cs, gK("notinvillage"));
                    return true;
                }

                Player s = (Player) cs;
                if(!v.isMayor(s)) {
                    msgPlayer(cs, gK("mayorkickonly"));
                    return true;
                }

                OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);

                if(v.isMayor(p)) {
                    msgPlayer(cs, gK("cantkickmayor"));
                    return true;
                }

                if(!v.isResident(p)) {
                    msgPlayer(cs, gK("notresident").replaceAll("%p%", args[1]));
                    return true;
                }

                v.sendMessage(gK("residentkicked", p));
                v.removeResident(p);
                saveAllData();
                return true;
            }

            if(command.equalsIgnoreCase("expand")) {
                if(!(cs instanceof Player)) {
                    msgPlayer(cs, gK("playeronly"));
                    return true;
                }

                //Expanding the town
                if(args.length < 2) {
                    msgPlayer(cs, gK("enterchunks"));
                    return true;
                }

                Village v = VillageUtils.getPlayerVillage((Player) cs);
                if(v == null) {
                    msgPlayer(cs, gK("notinvillage"));
                    return true;
                }

                Player s = (Player) cs;
                if(!v.isMayor(s)) {
                    msgPlayer(cs, gK("onlymayorexpand"));
                    return true;
                }

                int amount = -1;
                try {
                     amount = Integer.parseInt(args[1]);
                } catch (Exception ex) {
                    msgPlayer(cs, gK("mustbenumber"));
                    return true;
                }

                if(amount < 1) {
                    msgPlayer(cs, gK("mustbeone"));
                    return true;
                }

                if(amount > 5) {
                    msgPlayer(cs, gK("maxofthreeexpand"));
                    return true;
                }

                //Determine the amount of chunks I am expanding
                int chunksToExpand = v.getTownLocalBorderChunks(amount).size();

                double cost = getConfig().getDouble("cost.expand") * chunksToExpand;
                double totalcost = cost * amount;
                //Ensure town has enough
                if(totalcost > v.getMoney() && getConfigManager().useEconomy) {
                    msgPlayer(cs, gK("villagebankneedmore", totalcost));
                    return true;
                }

                //Expand town//
                int oldsize = v.getTownSize();

                msgConsole(ChatImportant + v.getName() + ChatDefault + " is expanding " + chunksToExpand + " chunks.");

                //Msg player, since this lags the server a tad
                msgPlayer(cs, gK("expandingvillage"));

                //Set new size
                v.setTownSize(v.getTownSize() + amount);

                if(VillageUtils.doVillagesOverlap(v)) {
                    v.setTownSize(oldsize);
                    msgPlayer(cs, gK("expandvillageoverlap"));
                    return true;
                }

                if(WorldGuardHook.instance.doesVillageOverlapRegion(v)) {
                    v.setTownSize(oldsize);
                    msgPlayer(cs, gK("expandregionoverlap"));
                    return true;
                }

                if(getConfigManager().useEconomy) {
                    v.addMoney(-totalcost);
                }
                v.sendMessage(gK("villageexpanded").replaceAll("%n%", "" + amount));
                saveAllData();
                return true;
            }

            if(command.equalsIgnoreCase("bank") && (cs instanceof Player)) {
                Player p = (Player) cs;
                Village v = VillageUtils.getPlayerVillage((Player) cs);
                if(v == null) {
                    msgPlayer(cs, gK("notinvillage"));
                    return true;
                }

                v.getItemBank().OpenAsInventory(p);
                return true;
            }

            //Village Plots
            if(command.equalsIgnoreCase("plot") && UsePlots) {
                if(!(cs instanceof Player)) {
                    msgPlayer(cs, gK("playeronly"));
                    return true;
                }

                if(args.length < 2) {
                    msgPlayer(cs, gK("notenougharguments"));
                    return true;
                }

                String iArg = args[1];  //Passed Argument
                Player p = (Player) cs;

                if(iArg.equalsIgnoreCase("claim")) {
                    Chunk c = p.getLocation().getChunk();

                    if(c == null) {
                        msgPlayer(cs, gK("error"));
                        return true;
                    }

                    Village v = VillageUtils.getPlayerVillage(p);
                    if(v == null) {
                        msgPlayer(cs, gK("notinvillage"));
                        return true;
                    }

                    if(!v.isChunkInTown(c)) {
                        msgPlayer(cs, gK("plotnotinvillage"));
                        return true;
                    }

                    //Check if Chunk is claimed
                    if(v.isChunkClaimed(c)) {
                        //Chunk has been claimed, get the owner.
                        msgPlayer(cs, gK("claimedchunkinfo", v.getPlayerFromChunk(c)));
                        return true;
                    }

                    //Chunk isn't claimed, try to claim.

                    if(getConfigManager().useEconomy) {
                        //Make sure they have the cash
                        double playerBalance = VillageEconomyUtils.economy.getBalance(p.getName());
                        double claimPrice = v.getChunkPrice(c);

                        if(playerBalance < claimPrice) {
                            msgPlayer(cs, gK("notenoughmoney", claimPrice));
                            return true;
                        }
                    }

                    //Try to claim chunk
                    if(!v.claimChunk(p, c)) {
                        msgPlayer(cs, gK("error"));
                        return true;
                    }

                    //Claimed chunk, charge player
                    if(getConfigManager().useEconomy) {
                        //Make sure they have the cash
                        double playerBalance = VillageEconomyUtils.economy.getBalance(p.getName());
                        double claimPrice = v.getChunkPrice(c);
                        VillageEconomyUtils.economy.withdrawPlayer(p.getName(), claimPrice);
                        v.addMoney(claimPrice);
                    }

                    msgPlayer(cs, gK("claimedchunk", c));
                    return true;
                }

                if(iArg.equalsIgnoreCase("set")) {
                    //Try to set chunk settings, price etc
                    if(args.length < 4) {
                        msgPlayer(cs, gK("notenougharguments"));
                        return true;
                    }

                    //Make sure player is in a villaeg
                    Village v = VillageUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        msgPlayer(cs, gK("notinvillage"));
                        return true;
                    }

                    //Make sure it's the mayor trying to do this.
                    if(!v.isMayor(p)) {
                        msgPlayer(cs, gK("onlymayorplot"));
                        return true;
                    }

                    //Get the players current chunk.
                    Chunk c = p.getLocation().getChunk();

                    if(!v.isChunkInTown(c)) {
                        msgPlayer(cs, gK("plotnotinvillage"));
                        return true;
                    }

                    String sArg = args[2];

                    if(sArg.equalsIgnoreCase("owner")) {
                        //Set the owner at the current chunk.
                        OfflinePlayer tplayer = getOfflinePlayer(cs, args[3]);
                        if(tplayer == null) {
                            msgPlayer(cs, gK("playernotfound").replaceAll("%p%", args[3]));
                            return true;
                        }

                        if(!v.isResident(tplayer)) {
                            msgPlayer(cs, gK("notresident", tplayer));
                            return true;
                        }

                        //Force claim chunk
                        v.forceClaim(tplayer, c);
                        msgPlayer(cs, gK("setplotowner", tplayer));

                        if(tplayer.isOnline()) {
                            msgPlayer(tplayer, gK("chunkclaimed", c));
                        }
                        return true;
                    }

                    if(sArg.equalsIgnoreCase("price")) {
                        //Set the price of the chunk.

                        //Try to parse the users input
                        double amount = -1;
                        try {
                            amount = Double.parseDouble(args[3]);
                        } catch(NumberFormatException ex) {
                            msgPlayer(cs, gK("notmoney"));
                            return true;
                        }

                        if(amount < 0) {
                            msgPlayer(cs, gK("mustbeone"));
                            return true;
                        }

                        //Set the chunk price
                        v.setChunkPrice(c, amount);
                        msgPlayer(cs, gK("setplotprice", amount));
                        return true;
                    }

                    msgPlayer(cs, gK("invalidargument"));
                    return true;
                }

                if(iArg.equalsIgnoreCase("check")) {
                    Chunk c = p.getLocation().getChunk();

                    if(c == null) {
                        msgPlayer(cs, gK("error"));
                        return true;
                    }

                    Village v = VillageUtils.getPlayerVillage((Player) cs);
                    if(v == null) {
                        msgPlayer(cs, gK("notinvillage"));
                        return true;
                    }

                    if(!v.isChunkInTown(c)) {
                        msgPlayer(cs, gK("plotnotinvillage"));
                        return true;
                    }

                    //Check if Chunk is claimed
                    if(v.isChunkClaimed(c)) {
                        //Chunk has been claimed, get the owner.
                        msgPlayer(cs, gK("claimedchunkinfo", v.getPlayerFromChunk(c)));
                        return true;
                    }

                    //Chunk isn't claimed, show pricing details.
                    String message = gK("chunkavailable");

                    if(getConfigManager().useEconomy) {
                        double d = v.getChunkPrice(c);
                        message += ChatDefault + " Cost: " + ChatImportant + VillageEconomyUtils.economy.format(d);
                    }

                    msgPlayer(cs, message);
                    return true;
                }

                msgPlayer(cs, gK("invalidargument"));
                return true;
            }

            if(command.equalsIgnoreCase("lookup")) {
                if(!cs.hasPermission("Villages.lookup")) {
                    msgPlayer(cs, gK("nopermission"));
                    return true;
                }
                if(args.length < 2) {
                    msgPlayer(cs, gK("enterplayer"));
                    return false;
                }
                OfflinePlayer p = getOfflinePlayer(cs, args[1]);
                if(p == null) {
                    msgPlayer(cs, gK("playernotfound").replaceAll("%p%", args[1]));
                    return true;
                }

                Village v = VillageUtils.getPlayerVillage(p);
                if(v == null) {
                    msgPlayer(cs, gK("playernotinvillage", p));
                    return true;
                }

                msgPlayer(cs, gK("player").replaceAll("%v%", v.getName()));
                return true;
            }

            if(command.equalsIgnoreCase("explode") && (cs instanceof Player)) {
                if(!cs.hasPermission("Villages.explode")) {
                    msgPlayer(cs, gK("nopermission"));
                    return true;
                }

                village = VillageUtils.getPlayerVillage((Player) cs);
                if(village == null) {
                    msgPlayer(cs, gK("notinvillage"));
                    return true;
                }

                if(!village.isMayor((Player) cs)) {
                    msgPlayer(cs, gK("onlymayorexplode"));
                    return true;
                }

                village.sendMessage(gK("villageexploded"));
                for(Chunk c : village.getTownArea()) {
                    if(!c.isLoaded()) {
                        c.load();
                    }

                    Block b = c.getBlock(8, 48, 8);
                    b.getWorld().createExplosion(b.getLocation(), 15f);
                    b = c.getBlock(8, 64, 8);
                    b.getWorld().createExplosion(b.getLocation(), 15f);
                    b = c.getBlock(8, 86, 8);
                    b.getWorld().createExplosion(b.getLocation(), 15f);
                }

                return true;
            }

            village = VillageUtils.getVillage(args[0]);
        }

        if(village == null && (cs instanceof Player) &&  VillageUtils.getPlayerVillage((Player) cs) == null) {
            msgPlayer(cs, gK("notinvillage"));
            return false;
        }

        if(village == null) {
            msgPlayer(cs, gK("cantfindvillage"));
            return false;
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

        if(getConfigManager().useEconomy) {
            msg.add(ChatDefault + "Village Wealth: " + ChatImportant + VillageEconomyUtils.economy.format(village.getMoney()));
        }

        msg.add(ChatDefault + "Located at: " + ChatImportant + getStringLocation(village.getTownSpawn()));
        msg.add(ChatDefault + "Village Size: " + ChatImportant + village.getTownSize());

        msgPlayer(cs, msg);
        return true;
    }
    
}

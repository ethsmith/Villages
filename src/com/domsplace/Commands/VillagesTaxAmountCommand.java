package com.domsplace.Commands;

import com.domsplace.DataManagers.VillageUpkeepManager;
import com.domsplace.Objects.Village;
import com.domsplace.Utils.VillageEconomyUtils;
import com.domsplace.Utils.VillageUtils;
import com.domsplace.Utils.VillageVillagesUtils;
import com.domsplace.VillageBase;
import com.domsplace.VillagesPlugin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VillagesTaxAmountCommand extends VillageBase implements CommandExecutor {
    
    private VillagesPlugin plugin;
    
    public VillagesTaxAmountCommand (VillagesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("taxamount")) {
            if(!(cs instanceof Player)) {
                VillageUtils.msgPlayer(cs, gK("playeronly"));
                VillageUtils.msgPlayer(cs, VillageUtils.getCommandDescription(cmd.getName()));
                return true;
            }
            
            Player p = (Player) cs;
            Village v = VillageVillagesUtils.getPlayerVillage(p);

            if(v == null) {
                VillageUtils.msgPlayer(cs, gK("notinvillage"));
                return true;
            }
            
            if(args.length <= 0) {
                int count = 0;
                VillageUtils.msgPlayer(cs, gK("taxes"));
                
                for(String upkeep : VillageUpkeepManager.upkeeps()) {
                    count ++;
                    
                    VillageUtils.msgPlayer(cs, upkeep);
                }
                
                if(count == 0) {
                    VillageUtils.msgPlayer(cs, gK("notaxes"));
                }
                
                return true;
            }
            
            List<String> cap = new ArrayList<String>();
            for(String s : VillageUpkeepManager.upkeeps()) {
                cap.add(s.toLowerCase());
            }
            
            if(!cap.contains(args[0].toLowerCase())) {
                VillageUtils.msgPlayer(cs, gK("cantfindtax"));
                return true;
            }
            
            String n = args[0];
            
            for(String s : VillageUpkeepManager.upkeeps()) {
                if(s.toUpperCase().equalsIgnoreCase(args[0])) {
                    n = s;
                    break;
                }
            }
            
            int times = 1;
            
            if(VillageUpkeepManager.Upkeep.contains(n + ".multiplier")) {
                if(VillageUpkeepManager.Upkeep.getString(n + ".multiplier.type").equalsIgnoreCase("chunk")) {
                    times = v.getTownChunks().size();
                } else if(VillageUpkeepManager.Upkeep.getString(n + ".multiplier.type").equalsIgnoreCase("size")) {
                    times = v.getTownSize();
                }

                times *= VillageUpkeepManager.Upkeep.getInt(n + ".multiplier.amount");
            }
            
            double moneyDue = 0.0d;
            List<ItemStack> itemsDue = new ArrayList<ItemStack>();
                
            for(int i = 0; i < times; i++) {
                double cost = VillageUpkeepManager.Upkeep.getDouble(n + ".money");

                List<ItemStack> items = VillageUtils.GetItemFromString(VillageUpkeepManager.Upkeep.getStringList(n + ".items"));
                
                for(ItemStack is : items) {
                    itemsDue.add(is);
                }
                
                moneyDue += cost;
            }
            
            VillageUtils.msgPlayer(cs, gK("taxesdue").replaceAll("%tax%", n));
            
            if(VillageUtils.useEconomy) {
                VillageUtils.msgPlayer(cs, ChatImportant + "Money: " + ChatDefault + VillageEconomyUtils.economy.format(moneyDue));
            }
            
            Map<Material, Integer> itemAmounts = new HashMap<Material, Integer>();
            
            for(ItemStack is : itemsDue) {
                int amount = 0;
                
                if(itemAmounts.containsKey(is.getType())) {
                    amount = itemAmounts.get(is.getType());
                }
                
                amount += is.getAmount();
                itemAmounts.put(is.getType(), amount);
            }
            
            for(Material m : itemAmounts.keySet()) {
                int am = itemAmounts.get(m);
                String nm = VillageUtils.CapitalizeFirstLetter(m.name().toLowerCase().replaceAll("_", " "));
                
                VillageUtils.msgPlayer(
                    cs,
                    ChatImportant + "[" + ChatDefault + am + "x" + ChatImportant + "] " +
                    ChatDefault + nm
                );
            }
            
            return true;
        }
        
        return false;
    }
}

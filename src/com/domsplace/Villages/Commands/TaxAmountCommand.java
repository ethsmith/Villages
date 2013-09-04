package com.domsplace.Villages.Commands;

import com.domsplace.Villages.DataManagers.UpkeepManager;
import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Utils.VillageEconomyUtils;
import com.domsplace.Villages.Utils.Utils;
import com.domsplace.Villages.Utils.VillageUtils;
import com.domsplace.Villages.Bases.CommandBase;
import com.domsplace.Villages.Bases.DataManagerBase;
import com.domsplace.Villages.Objects.SubCommand;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TaxAmountCommand extends CommandBase {
    public TaxAmountCommand () {
        super("taxamount");
        this.addSubCommand(SubCommand.make("tax"));
    }

    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        UpkeepManager VillageUpkeepManager = DataManagerBase.UPKEEP_MANAGER;
        if(!isPlayer(sender)) {
            Utils.msgPlayer(sender, gK("playeronly"));
            return false;
        }

        Player p = (Player) sender;
        Village v = VillageUtils.getPlayerVillage(p);

        if(v == null) {
            Utils.msgPlayer(sender, gK("notinvillage"));
            return true;
        }

        if(args.length <= 0) {
            int count = 0;
            Utils.msgPlayer(sender, gK("taxes"));

            for(String upkeep : VillageUpkeepManager.upkeeps()) {
                count ++;

                Utils.msgPlayer(sender, upkeep);
            }

            if(count == 0) {
                Utils.msgPlayer(sender, gK("notaxes"));
            }

            return true;
        }

        List<String> cap = new ArrayList<String>();
        for(String s : VillageUpkeepManager.upkeeps()) {
            cap.add(s.toLowerCase());
        }

        if(!cap.contains(args[0].toLowerCase())) {
            Utils.msgPlayer(sender, gK("cantfindtax"));
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

            List<ItemStack> items = Utils.GetItemFromString(VillageUpkeepManager.Upkeep.getStringList(n + ".items"));

            for(ItemStack is : items) {
                itemsDue.add(is);
            }

            moneyDue += cost;
        }

        Utils.msgPlayer(sender, gK("taxesdue").replaceAll("%tax%", n));

        if(Utils.useEconomy) {
            Utils.msgPlayer(sender, ChatImportant + "Money: " + ChatDefault + VillageEconomyUtils.economy.format(moneyDue));
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
            String nm = Utils.CapitalizeFirstLetter(m.name().toLowerCase().replaceAll("_", " "));

            Utils.msgPlayer(
                sender,
                ChatImportant + "[" + ChatDefault + am + "x" + ChatImportant + "] " +
                ChatDefault + nm
            );
        }

        return true;
    }
}

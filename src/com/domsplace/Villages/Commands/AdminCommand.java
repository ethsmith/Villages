package com.domsplace.Villages.Commands;

import com.domsplace.Villages.DataManagers.UpkeepManager;
import com.domsplace.Villages.DataManagers.ConfigManager;
import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Utils.Utils;
import com.domsplace.Villages.Utils.VillageUtils;
import static com.domsplace.Villages.Bases.Base.ChatDefault;
import static com.domsplace.Villages.Bases.Base.ChatError;
import static com.domsplace.Villages.Bases.Base.ChatImportant;
import static com.domsplace.Villages.Bases.Base.gK;
import com.domsplace.Villages.Bases.CommandBase;
import com.domsplace.Villages.Bases.DataManagerBase;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand extends CommandBase {
    public AdminCommand () {
        super("villageadmin");
    }

    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            List<String> msgs = new ArrayList<String>();

            msgs.add(ChatImportant + "Commands help: " + ChatDefault + "http://oxafemble.me/url/16");

            Utils.msgPlayer(sender, msgs);
            return true;
        }

        String arg = args[0].toLowerCase();
        if(arg.equals("reload")) {
            Utils.msgPlayer(sender, ChatDefault + "Reloading Config...");
            DataManagerBase.CONFIG_MANAGER.load();
            VillageUtils.LoadAllVillages();
            DataManagerBase.UPKEEP_MANAGER.load();
            DataManagerBase.LANGUAGE_MANAGER.load();
            Utils.msgPlayer(sender, ChatImportant + "Reloaded!");
            return true;
        }

        if(arg.equals("save")) {
            if(args.length == 2 && args[1].equalsIgnoreCase("yml") && Utils.useSQL) {
                Utils.msgPlayer(sender, "Force saving SQL data as YML.");

                for(Village v : VillageUtils.getVillages()) {
                    VillageUtils.SaveVillageYML(v);
                }

                Utils.msgPlayer(sender, ChatImportant + "Saved data as YML!");
                return true;
            }

            Utils.msgPlayer(sender, ChatDefault + "Flushing data...");
            VillageUtils.SaveAllVillages();
            Utils.msgPlayer(sender, ChatImportant + "Saved Data!");
            return true;
        }

        if(arg.equals("delete")) {
            if(args.length < 2) {
                Utils.msgPlayer(sender, gK("neednamedelete"));
                return true;
            }

            String name = args[1];
            Village village = VillageUtils.getVillage(name);
            if(village == null) {
                Utils.msgPlayer(sender, gK("villagedoesntexist"));
                return true;
            }

            Utils.msgPlayer(sender, gK("villagedelete", village));
            VillageUtils.DeleteVillage(village);
            return true;
        }

        if(arg.equalsIgnoreCase("kick")) {
            if(args.length < 2) {
                Utils.msgPlayer(sender, gK("notenougharguments"));
                return true;
            }

            OfflinePlayer p = Utils.getOfflinePlayer(sender, args[1]);
            if(p == null) {
                sender.sendMessage(ChatError + args[1] + " not found.");
                return true;
            }

            Village tp = VillageUtils.getPlayerVillage(p);
            if(tp == null) {
                Utils.msgPlayer(sender, gK("notinvillage"));
                return true;
            }

            if(tp.isMayor(p)) {
                Utils.msgPlayer(sender, gK("cantkickmayor"));
                return true;
            }

            tp.SendMessage(gK("residentkicked", p));
            tp.removeResident(p);
            VillageUtils.SaveAllVillages();
            Utils.msgPlayer(sender, gK("residentkicked", p));
            return true;
        }

        //Try to get Village based off argument(s)
        Village v = VillageUtils.getVillage(arg);

        if(v != null) {
            //Got Village, lets use the extra arguments

            if(args.length < 2) {
                Utils.msgPlayer(sender, gK("notenougharguments"));
                return true;
            }

            String ar = args[1];
            if(ar.equalsIgnoreCase("invite")) {
                if(args.length < 3) {
                    Utils.msgPlayer(sender, gK("notenougharguments"));
                    return true;
                }

                Player p = Utils.getPlayer(sender, args[2]);
                if(p == null) {
                    sender.sendMessage(ChatError + args[2] + " not found.");
                    return true;
                }

                Village tp = VillageUtils.getPlayerVillage(p);
                if(tp != null) {
                    Utils.msgPlayer(sender, ChatError + p.getDisplayName() + " is already in a Village.");
                    return true;
                }

                VillageUtils.townInvites.put(p, v);
                Utils.msgPlayer(p, gK("villageinvite", v).replaceAll("%p%", sender.getName()));
                Utils.msgPlayer(p, ChatDefault + "Type " + ChatImportant + "/villageaccept" + ChatDefault + " or " + ChatImportant + "/villagedeny");
                Utils.msgPlayer(sender, gK("residentinvited", p));
                return true;
            }

            if(ar.equalsIgnoreCase("mayor")) {
                if(args.length < 3) {
                    Utils.msgPlayer(sender, gK("notenougharguments"));
                    return true;
                }

                OfflinePlayer p = Utils.getOfflinePlayer(sender, args[2]);
                if(p == null) {
                    sender.sendMessage(ChatError + args[2] + " not found.");
                    return true;
                }

                Village tp = VillageUtils.getPlayerVillage(p);
                if(tp == null) {
                    Utils.msgPlayer(sender, ChatError + p.getName() + " isnt in a Village.");
                    return true;
                }

                if(tp != v) {
                    Utils.msgPlayer(sender, ChatError + p.getName() + " isnt in that Village.");
                    return true;
                }

                if(!v.isResident(v.getMayor())) {
                    v.addResident(v.getMayor());
                }
                v.setMayor(p);
                if(!v.isResident(v.getMayor())) {
                    v.addResident(v.getMayor());
                }

                Utils.msgPlayer(sender, ChatImportant + p.getName() + ChatDefault + " is the new Mayor of " + ChatImportant + v.getName());
                return true;
            }
        }


        Utils.msgPlayer(sender, gK("invalidargument"));
        return true;
    }
}

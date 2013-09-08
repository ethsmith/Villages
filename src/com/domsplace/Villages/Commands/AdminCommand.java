package com.domsplace.Villages.Commands;

import com.domsplace.Villages.Objects.Village;
import static com.domsplace.Villages.Bases.Base.ChatDefault;
import static com.domsplace.Villages.Bases.Base.ChatError;
import static com.domsplace.Villages.Bases.Base.ChatImportant;
import static com.domsplace.Villages.Bases.Base.gK;
import com.domsplace.Villages.Bases.CommandBase;
import com.domsplace.Villages.Bases.DataManagerBase;
import com.domsplace.Villages.Objects.SubCommand;
import com.domsplace.Villages.Utils.VillageUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand extends CommandBase {
    public AdminCommand () {
        super("villageadmin");
        this.addSubCommand(SubCommand.make("reload"));
        this.addSubCommand(SubCommand.make("save", "yml"));
        this.addSubCommand(SubCommand.make("delete", SubCommand.VILLAGE));
        this.addSubCommand(SubCommand.make("kick", SubCommand.PLAYER));
        this.addSubCommand(SubCommand.make(
            SubCommand.VILLAGE, 
                SubCommand.make("invite", SubCommand.PLAYER), 
                SubCommand.make("mayor", SubCommand.PLAYER))
        );
    }

    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            List<String> msgs = new ArrayList<String>();

            msgs.add(ChatImportant + "Commands help: " + ChatDefault + "http://oxafemble.me/url/16");

            msgPlayer(sender, msgs);
            return true;
        }

        String arg = args[0].toLowerCase();
        if(arg.equals("reload")) {
            msgPlayer(sender, ChatDefault + "Reloading Config...");
            DataManagerBase.CONFIG_MANAGER.load();
            getVillageManager().load();
            DataManagerBase.UPKEEP_MANAGER.load();
            DataManagerBase.LANGUAGE_MANAGER.load();
            msgPlayer(sender, ChatImportant + "Reloaded!");
            return true;
        }

        if(arg.equals("save")) {
            if(args.length == 2 && args[1].equalsIgnoreCase("yml") && getConfigManager().useSQL) {
                msgPlayer(sender, "Force saving SQL data as YML.");

                for(Village v : VillageUtils.getVillages()) {
                    getVillageManager().saveVillageYML(v);
                }

                msgPlayer(sender, ChatImportant + "Saved data as YML!");
                return true;
            }

            msgPlayer(sender, ChatDefault + "Flushing data...");
            saveAllData();
            msgPlayer(sender, ChatImportant + "Saved Data!");
            return true;
        }

        if(arg.equals("delete")) {
            if(args.length < 2) {
                msgPlayer(sender, gK("neednamedelete"));
                return false;
            }

            String name = args[1];
            Village village = VillageUtils.getVillage(name);
            if(village == null) {
                msgPlayer(sender, gK("villagedoesntexist"));
                return true;
            }

            msgPlayer(sender, gK("villagedelete", village));
            getVillageManager().deleteVillage(village);
            return true;
        }

        if(arg.equalsIgnoreCase("kick")) {
            if(args.length < 2) {
                msgPlayer(sender, gK("notenougharguments"));
                return false;
            }

            OfflinePlayer p = getOfflinePlayer(sender, args[1]);
            if(p == null) {
                sender.sendMessage(ChatError + args[1] + " not found.");
                return true;
            }

            Village tp = VillageUtils.getPlayerVillage(p);
            if(tp == null) {
                msgPlayer(sender, gK("notinvillage"));
                return true;
            }

            if(tp.isMayor(p)) {
                msgPlayer(sender, gK("cantkickmayor"));
                return true;
            }

            tp.sendMessage(gK("residentkicked", p));
            tp.removeResident(p);
            saveAllData();
            msgPlayer(sender, gK("residentkicked", p));
            return true;
        }

        //Try to get Village based off argument(s)
        Village v = VillageUtils.getVillage(arg);

        if(v != null) {
            //Got Village, lets use the extra arguments

            if(args.length < 2) {
                msgPlayer(sender, gK("notenougharguments"));
                return true;
            }

            String ar = args[1];
            if(ar.equalsIgnoreCase("invite")) {
                if(args.length < 3) {
                    msgPlayer(sender, gK("notenougharguments"));
                    return false;
                }

                Player p = getPlayer(sender, args[2]);
                if(p == null) {
                    sender.sendMessage(ChatError + args[2] + " not found.");
                    return true;
                }

                Village tp = VillageUtils.getPlayerVillage(p);
                if(tp != null) {
                    msgPlayer(sender, ChatError + p.getDisplayName() + " is already in a Village.");
                    return true;
                }

                VillageUtils.townInvites.put(p, v);
                msgPlayer(p, gK("villageinvite", v).replaceAll("%p%", sender.getName()));
                msgPlayer(p, ChatDefault + "Type " + ChatImportant + "/villageaccept" + ChatDefault + " or " + ChatImportant + "/villagedeny");
                msgPlayer(sender, gK("residentinvited", p));
                return true;
            }

            if(ar.equalsIgnoreCase("mayor")) {
                if(args.length < 3) {
                    msgPlayer(sender, gK("notenougharguments"));
                    return false;
                }

                OfflinePlayer p = getOfflinePlayer(sender, args[2]);
                if(p == null) {
                    sender.sendMessage(ChatError + args[2] + " not found.");
                    return true;
                }

                Village tp = VillageUtils.getPlayerVillage(p);
                if(tp == null) {
                    msgPlayer(sender, ChatError + p.getName() + " isnt in a Village.");
                    return true;
                }

                if(tp != v) {
                    msgPlayer(sender, ChatError + p.getName() + " isnt in that Village.");
                    return true;
                }

                if(!v.isResident(v.getMayor())) {
                    v.addResident(v.getMayor());
                }
                v.setMayor(p);
                if(!v.isResident(v.getMayor())) {
                    v.addResident(v.getMayor());
                }

                msgPlayer(sender, ChatImportant + p.getName() + ChatDefault + " is the new Mayor of " + ChatImportant + v.getName());
                return true;
            }
        }


        msgPlayer(sender, gK("invalidargument"));
        return true;
    }
}

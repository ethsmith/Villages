package com.domsplace.Villages.Commands.SubCommands.AdminCommands;

import static com.domsplace.Villages.Bases.Base.sk;
import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Commands.SubCommands.Mayor.VillageMayorSetDescription;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageAdminSetDescription extends SubCommand {
    public VillageAdminSetDescription() {
        super("village", "admin", "set", "description");
        this.setPermission("admin.setdescription");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sk(sender, "needvillagename");
            return false;
        }
        
        if(args.length < 2) {
            sk(sender, "enterdescription");
            return false;
        }
        
        Village v = Village.getVillage(args[0]);
        
        if(v == null) {sk(sender, "villagedoesntexist");return true;}
        
        String message = "";
        for(int i = 1; i < args.length; i++) {
            message += args[i];
            if(i < (args.length - 1)) message += " ";
        }
        
        if(message.length() > VillageMayorSetDescription.VILLAGE_DESCRIPTION_LENGTH) {
            sk(sender, "descriptionlong");
            return true;
        }
        
        if(!message.matches(VillageMayorSetDescription.VILLAGE_DESCRIPTION_REGEX)) {
            sk(sender, "invalidvillagedescription");
            return true;
        }
        
        sk(sender, "newdescription", message);
        v.setDescription(message);
        DataManager.saveAll();
        return true;
    }
}

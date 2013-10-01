package com.domsplace.Villages.Commands.SubCommands.Mayor;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageMayorSetDescription extends SubCommand {
    public static final String VILLAGE_DESCRIPTION_REGEX = "^[a-zA-Z0-9!@#$^&*(),.\\s]*$";
    public static final int VILLAGE_DESCRIPTION_LENGTH = 80;
    
    public VillageMayorSetDescription() {
        super("village", "mayor", "set", "description");
        this.setPermission("mayor.setdescription");
        this.addOption(SubCommand.PLAYERS_OPTION);
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {sk(sender, "playeronly");return true;}
        
        Resident r = Resident.getResident(getPlayer(sender));
        Village v = Village.getPlayersVillage(r);
        if(v == null) {sk(sender, "notinvillage");return true;}
        if(!v.isMayor(r)) {sk(sender, "notmayordescription"); return true;}
        
        if(args.length < 1) {
            sk(sender, "enterdescription");
            return true;
        }
        
        String message = "";
        for(int i = 0; i < args.length; i++) {
            message += args[i];
            if(i < (args.length - 1)) message += " ";
        }
        
        if(message.length() > VILLAGE_DESCRIPTION_LENGTH) {
            sk(sender, "descriptionlong");
            return true;
        }
        
        if(!message.matches(VILLAGE_DESCRIPTION_REGEX)) {
            sk(sender, "invalidvillagedescription");
            return true;
        }
        
        sk(sender, "newdescription", message);
        v.setDescription(message);
        DataManager.saveAll();
        return true;
    }
}

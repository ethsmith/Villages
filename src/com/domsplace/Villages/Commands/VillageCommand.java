package com.domsplace.Villages.Commands;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.SubCommandOption;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageCommand extends BukkitCommand {
    public VillageCommand() {
        super("village");
        
        //TOOD: Finish Sub Commands and Fix TAB options
        
        this.addSubCommandOption(new SubCommandOption("info", SubCommandOption.VILLAGES_OPTION));
        this.addSubCommandOption(new SubCommandOption("accept"));
        this.addSubCommandOption(new SubCommandOption("deny"));
        this.addSubCommandOption(new SubCommandOption("help"));
        this.addSubCommandOption(new SubCommandOption("create", "name"));
        this.addSubCommandOption(new SubCommandOption("invite", SubCommandOption.PLAYERS_OPTION));
        this.addSubCommandOption(new SubCommandOption("leave"));
        this.addSubCommandOption(new SubCommandOption("list"));
        this.addSubCommandOption(new SubCommandOption("lookup", SubCommandOption.PLAYERS_OPTION));
        this.addSubCommandOption(new SubCommandOption("message", "message"));
        this.addSubCommandOption(new SubCommandOption("spawn"));
        this.addSubCommandOption(new SubCommandOption("top"));
        this.addSubCommandOption(new SubCommandOption("admin",
            new SubCommandOption("add", 
                new SubCommandOption("player", 
                    new SubCommandOption(SubCommandOption.VILLAGES_OPTION.getOption(), 
                        SubCommandOption.PLAYERS_OPTION
                    )
                )
            ),
            new SubCommandOption("remove", 
                new SubCommandOption("player", 
                    new SubCommandOption(SubCommandOption.VILLAGES_OPTION.getOption(), 
                        SubCommandOption.PLAYERS_OPTION
                    )
                )
            ),
            new SubCommandOption("delete", 
                SubCommandOption.PLAYERS_OPTION
            ),
            new SubCommandOption("save", "yml"),
            new SubCommandOption("set",
                new SubCommandOption("description", "description"),
                new SubCommandOption("mayor", 
                    new SubCommandOption(SubCommandOption.VILLAGES_OPTION.getOption(), 
                        SubCommandOption.PLAYERS_OPTION
                    )
                ),
                new SubCommandOption("name", "newname")
            )
        ));
        this.addSubCommandOption(new SubCommandOption("bank", 
            new SubCommandOption("deposit", "amount"),
            new SubCommandOption("withdraw", "amount"),
            new SubCommandOption("open")
        ));
    }
    
    @Override
    public boolean cmd(CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) return false;
        
        Resident r = Resident.getResident(getPlayer(sender));
        Village v = Village.getPlayersVillage(r);
        if(v == null) {
            sk(sender, "notinvillage");
            return true;
        }
        
        return this.fakeExecute(sender, "village info");
    }
}

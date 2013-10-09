package com.domsplace.Villages.Commands.SubCommands;

import com.domsplace.Villages.Events.VillageCreatedEvent;
import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.PluginHook;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Objects.Region;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageCreate extends SubCommand {
    public static final String VILLAGE_NAME_REGEX = "^[a-zA-Z0-9]*$";
    public static final int VILLAGE_NAME_LENGTH = 12;
    
    public VillageCreate() {
        super("village", "create");
        this.setPermission("create");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        //Make sure it's a player running the command
        if(!isPlayer(sender)) {
            sk(sender, "playeronly");
            return false;
        }
        
        //Make sure player is in a Village world
        if(!inVillageWorld(sender)) {
            sk(sender, "notinthisworld");
            return true;
        }
        
        //Make sure they enter a village name
        if(args.length < 1) {
            sk(sender, "needvillagename");
            return false;
        }
        
        //Make sure name is valid
        String name = args[0];
        if(name.length() >= VILLAGE_NAME_LENGTH || name.length() < 3) {
            sk(sender, "invalidvillagename");
            return true;
        }
        
        if(!name.matches(VILLAGE_NAME_REGEX)) {
            sk(sender, "invalidvillagename");
            return true;
        }
        
        //Make sure name isn't taken
        if(Village.getVillage(name) != null) {
            sk(sender, "villagenameused");
            return true;
        }
        
        //Make sure player isn't in Village
        if(Village.getPlayersVillage(Resident.getResident(getPlayer(sender))) != null) {
            sk(sender, "alreadyinvillage");
            return true;
        }
        
        //Make sure they have enough
        if(!hasBalance(sender.getName(), getCost("createvillage"))) {
            sk(sender, "notenoughmoney", PluginHook.VAULT_HOOK.getEconomy().format(getCost("createvillage")));
            return true;
        }
        
        //"Create" a village
        Village village = new Village();
        Resident player = Resident.getResident(getPlayer(sender));
        Region spawn = Region.getRegion(getPlayer(sender));
        
        village.setName(name);
        village.setMayor(player);
        village.addRegion(spawn);
        village.setSpawn(spawn);
        
        if(player == null || spawn == null) return false;
        
        //Check for Overlapping
        if(Village.doesRegionOverlapVillage(spawn)) {
            sk(sender, "createvillageoverlap");
            return true;
        }
        
        //Check for WorldGuard Overlapping
        if(Base.useWorldGuard) {
            if(PluginHook.WORLD_GUARD_HOOK.isOverlappingRegion(spawn)) {
                sk(sender, "createvillageregionoverlap");
                return true;
            }
        }
        
        //Fire Events
        VillageCreatedEvent event = new VillageCreatedEvent(player, village);
        event.fireEvent();
        if(event.isCancelled()) return true;
        
        //Charge Players Wallet
        if(Base.useEconomy) {
            PluginHook.VAULT_HOOK.getEconomy().withdrawPlayer(sender.getName(), getCost("createvillage"));
        }
        
        //Finally Store Changes
        Village.registerVillage(village);
        DataManager.saveAll();
        bk("createdvillage", getPlayer(sender), village);
        return true;
    }
}

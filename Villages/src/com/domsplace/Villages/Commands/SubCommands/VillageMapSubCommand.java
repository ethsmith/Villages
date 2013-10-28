/*
 * Copyright 2013 Dominic.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.domsplace.Villages.Commands.SubCommands;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Exceptions.InvalidItemException;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Objects.VillageItem;
import com.domsplace.Villages.Objects.VillageMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author      Dominic
 * @since       13/10/2013
 */
public class VillageMapSubCommand extends SubCommand {
    public VillageMapSubCommand() {
        super("village", "map");
        this.setPermission("map");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        debug("THIS COMMAND IS IN DEVELOPMENT!!!");
        if(!isPlayer(sender)) {
            sk(sender, "playeronly");
            return true;
        }
        
        Player player = getPlayer(sender);
        //TODO: Figure out Wilderness Map
        Village v = Village.getPlayersVillage(Resident.getResident(player));
        if(v == null) {
            sk(sender, "notinvillage");
            return true;
        }
        
        VillageMap map;
        try {
            map = v.getVillageMap();
        } catch(IllegalArgumentException e) {
            //Village Map is not Available in this world
            sk(sender, "notinthisworld");
            return true;
        }
        
        VillageItem mapItem = map.getMapAsItem();
        try {
            mapItem.giveToPlayer(player);
        } catch(InvalidItemException e) {
            sendMessage(sender, ChatError + "An error occured...");
            return true;
        }
        
        sendMessage(sender, "Giving Village Map.");
        return true;
    }
}

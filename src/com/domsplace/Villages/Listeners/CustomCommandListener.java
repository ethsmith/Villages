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

package com.domsplace.Villages.Listeners;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.VillageListener;
import com.domsplace.Villages.Events.PreCommandEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

/**
 * @author      Dominic
 * @since       05/10/2013
 */
public class CustomCommandListener extends VillageListener {
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void handleCustomCommandAliasesForVillageCommand(PreCommandEvent e) {
        if(e.getPlayer() == null) return;
        
        if(!inVillageWorld(e.getPlayer())) return;
        
        for(String command : getConfig().getStringList("features.aliases.village")) {
            if(!command.equalsIgnoreCase(e.getCommand())) continue;
            
            BukkitCommand.getCommand("village").fakeExecute(e.getPlayer(), e.toFullCommand());
            e.setCancelled(true);
            return;
        }
    }
}

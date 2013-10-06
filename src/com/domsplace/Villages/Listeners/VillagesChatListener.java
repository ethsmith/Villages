/*
 * Copyright 2013 Dominic Masters.
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

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.VillageListener;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author      Dominic
 * @since       05/10/2013
 */
public class VillagesChatListener extends VillageListener {
    @EventHandler(ignoreCancelled=true, priority=EventPriority.LOWEST)
    public void handleChatPrefixingVillage(AsyncPlayerChatEvent e) {
        if(!getConfig().getBoolean("colors.prefix.chat", true)) return;
        if(!inVillageWorld(e.getPlayer())) return;
        Village village = Village.getPlayersVillage(Resident.getResident(e.getPlayer()));
        if(village == null) return;
        e.setFormat(Base.getVillagePrefix(village) + e.getFormat());
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.LOWEST)
    public void handleChatPrefixingWilderness(AsyncPlayerChatEvent e) {
        if(!getConfig().getBoolean("colors.prefix.chat", true)) return;
        if(!inVillageWorld(e.getPlayer())) return;
        Village village = Village.getPlayersVillage(Resident.getResident(e.getPlayer()));
        if(village != null) return;
        e.setFormat(Base.getVillagePrefix(village) + e.getFormat());
    }
}

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

import static com.domsplace.Villages.Bases.Base.Wilderness;
import com.domsplace.Villages.Bases.VillageListener;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author      Dominic
 * @since       14/10/2013
 */
public class EssentialsChatListener extends VillageListener {
    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void handleEssentialsChat(AsyncPlayerChatEvent e) {
        String format = e.getFormat();
        
        String village = Wilderness;
        Village vil = Village.getPlayersVillage(Resident.getResident(e.getPlayer()));
        if(vil != null) {
            village = vil.getName();
        }
        
        format = format.replaceAll("\\{VILLAGE\\}", village);
        e.setFormat(format);
    }
}

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

package com.domsplace.Villages.Commands.SubCommands.AdminCommands;

import com.domsplace.Villages.Bases.Base;
import static com.domsplace.Villages.Bases.Base.isPlayer;
import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.GUI.VillagesGUIManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * @author      Dominic
 * @since       14/10/2013
 */
public class VillageAdminGUI extends SubCommand {  
    public VillageAdminGUI() {
        super("village", "admin", "gui");
        this.setPermission("*");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(isPlayer(sender)) {
            sendMessage(sender, ChatError + "This command can only be run by the console!");
            return true;
        }
        
        if(!getConfig().getBoolean("features.guiscreen", true)) {
            sendMessage(sender, ChatError + "Villages GUI is Disabled.");
            return true;
        }
        
        if(Base.guiManager != null) {
            sendMessage(sender, ChatError + "GUI is already displayed!");
            return true;
        }
        
        Base.guiManager = new VillagesGUIManager();
        sendMessage(sender, "Opening the GUI.");
        return true;
    }
}
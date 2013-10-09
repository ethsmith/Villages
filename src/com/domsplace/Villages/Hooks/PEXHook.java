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

package com.domsplace.Villages.Hooks;

import com.domsplace.Villages.Bases.PluginHook;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * @author      Dominic
 * @since       09/10/2013
 */
public class PEXHook extends PluginHook {
    public PEXHook() {
        super("PermissionsEx");
        this.shouldHook(true);
    }
    
    public boolean hasPermission(Player player, String permission) {
        try {
            PermissionUser user = PermissionsEx.getUser(player);
            debug("Checking PEX Perms "  + player.getName() + " has " + permission + " = " + user.has(permission));
            return user.has(permission);
        } catch(Exception e) {
            return false;
        } catch(Error e) {
            return false;
        }
    }
}

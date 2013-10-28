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

package com.domsplace.Villages.Objects;

import com.domsplace.Villages.Bases.Base;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.map.MapView;

/**
 * @author      Dominic
 * @since       13/10/2013
 */
public class VillageMap {
    public static void invoke() {
        Base.debug("Map Invoked (DEBUG ONLY)");
    }
    
    //Instance
    private Village village;
    private MapView map;
    private VillageMapRenderer renderer;
    
    public VillageMap(Village village) throws IllegalArgumentException {
        this.village = village;
        this.map = Bukkit.createMap(village.getSpawn().getBukkitWorld());
        this.renderer = new VillageMapRenderer(this);
        this.map.addRenderer(this.renderer);
        
        //TODO: Make Logic Smarter ?
    }
    
    public Village getVillage() {return this.village;}
    public MapView getMapView() {return this.map;}
    public VillageMapRenderer getRenderer() {return this.renderer;}
    
    public VillageItem getMapAsItem() {
        VillageItem item =  new VillageItem(Material.MAP, this.map.getId());
        item.setName(Base.ChatDefault + "Map for " + Base.ChatImportant + village.getName());
        return item;
    }

    public void unload() {
        //TODO: Auto remove map from all Players
        while(getMapView().getRenderers().size() > 0) {
            getMapView().removeRenderer(village.getVillageMap().getMapView().getRenderers().get(0));
        }
    }
}

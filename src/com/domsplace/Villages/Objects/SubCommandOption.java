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
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

/**
 * @author      Dominic
 * @since       05/10/2013
 */
public class SubCommandOption extends Base {
    public static final SubCommandOption PLAYERS_OPTION = new SubCommandOption("[PLAYER]");
    public static final SubCommandOption VILLAGES_OPTION = new SubCommandOption("[VILLAGE]");
    /* Only for Other plugin of mine ;) shh */
    public static final SubCommandOption WORLD_OPTION = new SubCommandOption("[WORLD]");
    public static final SubCommandOption ENCHANTMENT_OPTION = new SubCommandOption("[ENCHANTS]");
    public static final SubCommandOption MOB_OPTION = new SubCommandOption("[MOB]");
    public static final SubCommandOption ITEM_OPTION = new SubCommandOption("[ITEM]");
    public static final SubCommandOption POTION_OPTION = new SubCommandOption("[POTION]");
    public static final SubCommandOption WEATHER_OPTION = new SubCommandOption("[WEATHER]");
    
    //Instance
    private String option;
    private List<SubCommandOption> subOptions;
    
    public SubCommandOption(String option) {
        this.option = option;
        this.subOptions = new ArrayList<SubCommandOption>();
    }
    
    public SubCommandOption(String option, SubCommandOption... options) {
        this(option);
        for(SubCommandOption o : options) {
            this.subOptions.add(o);
        }
    }
    
    public SubCommandOption(String option, String... options) {
        this(option);
        for(String s : options) {
            this.subOptions.add(new SubCommandOption(s));
        }
    }
    
    public String getOption() {return this.option;}
    public List<SubCommandOption> getSubCommandOptions() {return new ArrayList<SubCommandOption>(this.subOptions);}

    public List<String> getOptionsFormatted() {
        List<String> returnV = new ArrayList<String>();
        if(this.compare(SubCommandOption.PLAYERS_OPTION)) {
            for(OfflinePlayer p : getPlayersList()) {
                returnV.add(p.getName());
            }
        } else if(this.compare(SubCommandOption.ENCHANTMENT_OPTION)) {
            for(Enchantment e : Enchantment.values()) {
                returnV.add(e.getName());
            }
        } else if(this.compare(SubCommandOption.WORLD_OPTION)) {
            for(World w : Bukkit.getWorlds()) {
                returnV.add(w.getName());
            }
        } else if(this.compare(SubCommandOption.MOB_OPTION)) {
            for(EntityType et : EntityType.values()) {
                if(!et.isAlive()) continue;
                returnV.add(et.getName());
            }
        } else if(this.compare(SubCommandOption.ITEM_OPTION)) {
            for(Material m : Material.values()) {
                returnV.add(m.name());
            }
        } else if(this.compare(SubCommandOption.POTION_OPTION)) {
            for(PotionEffectType pet : PotionEffectType.values()) {
                returnV.add(pet.getName());
            }
        } else if(this.compare(SubCommandOption.WEATHER_OPTION)) {
            returnV.add("sun");
            returnV.add("rain");
            returnV.add("clear");
            returnV.add("fog");
            returnV.add("storm");
            returnV.add("lightning");
            returnV.add("sunny");
            returnV.add("sunshine");
        } else if(this.compare(SubCommandOption.VILLAGES_OPTION)) {
            for(Village v : Village.getVillages()) {
                returnV.add(v.getName());
            }
        } else {
            returnV.add(this.option);
        }
        return returnV;
    }
    
    public List<String> getOptionsAsStringList() {
        List<String> returnV = new ArrayList<String>();
        for(SubCommandOption sc : this.subOptions) {
            returnV.addAll(sc.getOptionsFormatted());
        }
        
        return returnV;
    }
    
    public List<String> tryFetch(String[] args, int lvl) {
        List<String> opts = new ArrayList<String>();
        
        lvl = lvl + 1;
        int targetLevel = args.length;
        
        if(targetLevel > lvl) {
            for(SubCommandOption sco : this.subOptions) {
                opts.addAll(sco.tryFetch(args, lvl));
            }
        } else {
            return this.getOptionsAsStringList();
        }
        
        return opts;
    }
    
    public boolean compare(SubCommandOption option) {
        if(option.getOption().equalsIgnoreCase(this.getOption())) return true;
        return false;
    }
}

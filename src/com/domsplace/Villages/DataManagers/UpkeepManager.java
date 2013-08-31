package com.domsplace.Villages.DataManagers;

import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Utils.Utils;
import com.domsplace.Villages.Utils.VillageUtils;
import com.domsplace.Villages.Bases.DataManagerBase;
import com.domsplace.Villages.Enums.ManagerType;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class UpkeepManager extends DataManagerBase {
    public File UpkeepFile;
    public YamlConfiguration Upkeep;
    
    public UpkeepManager () {
        super(ManagerType.UPKEEP);
    }
    
    @Override
    public void tryLoad() throws IOException {
        UpkeepFile = new File(getDataFolder(), "upkeep.yml");

        boolean CreateNew = false;

        if(!UpkeepFile.exists()) {
            UpkeepFile.createNewFile();
            CreateNew = true;
        }

        Upkeep = YamlConfiguration.loadConfiguration(UpkeepFile);

        //Comment YML
        if(CreateNew) {
            String comments = ""
                    + "This YML file contains all the configuration for how much a village has to pay"
                    + "\nTo maintain for each day, month year etc. Visit http://dev.bukkit.org/bukkit-plugins/villages/pages/configuration/"
                    + "\nFor more information about how to configure this YML file."
                    + "";
            Upkeep.options().header(comments);
            Upkeep.options().pathSeparator();
        }

        //Create Default Values.
        if(CreateNew) {
            String s = "example";
            Upkeep.set(s + ".message", "Tax day has arrived!");
            Upkeep.set(s + ".hours", 24);
            Upkeep.set(s + ".money", 100.0);

            Upkeep.set(s + ".multiplier.type", "chunk");
            Upkeep.set(s + ".multiplier.amount", 2);

            List<String> items = new ArrayList<String>();
            items.add("297:0:10");
            items.add("5:0:20");

            Upkeep.set(s + ".items", items);
        }

        Upkeep.save(UpkeepFile);
    }
    
    public List<String> upkeeps() {
        List<String> keys = new ArrayList<String>();
        for(String s : Upkeep.getKeys(false)) {
            keys.add(s);
        }
        return keys;
    }
    
    public void checkVillage(Village village) {
        for(String k : upkeeps()) {
            checkVillage(k, village);
        }
    }
    
    public void checkVillage(String key, Village village) {
        try {
            File upkeepF = new File(getDataFolder(), "upkeepdata.yml");
            if(!upkeepF.exists()) {
                upkeepF.createNewFile();
            }
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(upkeepF);
            
            String k = village.getName() + "." + key + ".time";
            
            if(!yml.contains(k)) {
                yml.set(k, Utils.getNow());
                yml.save(upkeepF);
                return;
            }
            
            long t = yml.getLong(k);
            
            long h = t + Upkeep.getLong(key + ".hours") * 60L * 60L * 1000L;
            
            if(h <= Utils.getNow()) {
                village.SendMessage(Upkeep.getString(key + ".message"));
                
                yml.set(k, Utils.getNow());
                
                int times = 1;
                
                /*** Version 1.12: Added multipliers ***/
                if(Upkeep.contains(key + ".multiplier")) {
                    if(Upkeep.getString(key + ".multiplier.type").equalsIgnoreCase("chunk")) {
                        times = village.getTownChunks().size();
                    } else if(Upkeep.getString(key + ".multiplier.type").equalsIgnoreCase("size")) {
                        times = village.getTownSize();
                    }
                    
                    times *= Upkeep.getInt(key + ".multiplier.amount");
                }
                
                for(int i = 0; i < times; i++) {
                    double cost = Upkeep.getDouble(key + ".money");
                    village.addMoney(-cost);

                    if(village.getMoney() < 0) {
                        village.SendMessage(gK("cantcontinuemoney"));
                        VillageUtils.DeleteVillage(village);
                        return;
                    }

                    List<ItemStack> items = Utils.GetItemFromString(Upkeep.getStringList(key + ".items"));

                    for(ItemStack item : items) {
                        if(!village.getItemBank().containsItem(item)) {
                            village.SendMessage(gK("cantcontinueitems"));
                            VillageUtils.DeleteVillage(village);
                            return;
                        }

                        village.getItemBank().removeItem(item);
                    }
                }
            }
            
            yml.save(upkeepF);
            VillageUtils.SaveVillage(village);
        } catch(Exception ex) {
            Utils.Error("Failed to save/load Upkeep data yml.", ex);
            return;
        }
    }
    
    public void DeleteUpkeep(Village village) {
        try {
            File upkeepF = new File(getDataFolder(), "upkeepdata.yml");
            if(!upkeepF.exists()) {
                upkeepF.createNewFile();
            }
            YamlConfiguration uyml = YamlConfiguration.loadConfiguration(upkeepF);
            YamlConfiguration yml = new YamlConfiguration();
            
            Set<String> keys = uyml.getKeys(false);
            
            keys.remove(village.getName());
            
            for(String k : keys) {
                yml.set(k, uyml.get(k));
            }
            
            yml.save(upkeepF);
        } catch(Exception ex) {
            Utils.Error("Failed to save Upkeep yml.", ex);
        }
    }
}

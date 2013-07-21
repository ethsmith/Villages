package com.domsplace.DataManagers;

import com.domsplace.Objects.Village;
import com.domsplace.Utils.VillageUtils;
import com.domsplace.Utils.VillageVillagesUtils;
import com.domsplace.VillageBase;
import static com.domsplace.VillageBase.gK;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class VillageUpkeepManager extends VillageBase {
    public static File UpkeepFile;
    public static YamlConfiguration Upkeep;
    
    public static boolean SetupUpkeep() {
        try {
            UpkeepFile = new File(VillageUtils.plugin.getDataFolder() + "/upkeep.yml");
            
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
                
                List<String> items = new ArrayList<String>();
                items.add("297:0:10");
                items.add("5:0:20");
                
                Upkeep.set(s + ".items", items);
            }
            
            Upkeep.save(UpkeepFile);
            return true;
        } catch(Exception ex) {
            VillageUtils.Error("Failed to load Upkeep yml.", ex);
            return false;
        }
    }
    
    public static List<String> upkeeps() {
        List<String> keys = new ArrayList<String>();
        for(String s : Upkeep.getKeys(false)) {
            keys.add(s);
        }
        return keys;
    }
    
    public static void checkVillage(Village village) {
        for(String k : upkeeps()) {
            checkVillage(k, village);
        }
    }
    
    public static void checkVillage(String key, Village village) {
        try {
            File upkeepF = new File(VillageUtils.plugin.getDataFolder() + "/upkeepdata.yml");
            if(!upkeepF.exists()) {
                upkeepF.createNewFile();
            }
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(upkeepF);
            
            String k = village.getName() + "." + key + ".time";
            
            if(!yml.contains(k)) {
                yml.set(k, VillageUtils.getNow());
                yml.save(upkeepF);
                return;
            }
            
            long t = yml.getLong(k);
            
            long h = t + Upkeep.getLong(key + ".hours") * 60L * 60L * 1000L;
            
            if(h <= VillageUtils.getNow()) {
                village.SendMessage(Upkeep.getString(key + ".message"));
                
                yml.set(k, VillageUtils.getNow());
                
                double cost = Upkeep.getDouble(key + ".money");
                village.addMoney(-cost);
                
                if(village.getMoney() < 0) {
                    village.SendMessage(gK("cantcontinuemoney"));
                    VillageVillagesUtils.DeleteVillage(village);
                    return;
                }
                
                List<ItemStack> items = VillageUtils.GetItemFromString(Upkeep.getStringList(key + ".items"));
                
                for(ItemStack item : items) {
                    if(!village.getItemBank().containsItem(item)) {
                        village.SendMessage(gK("cantcontinueitems"));
                        VillageVillagesUtils.DeleteVillage(village);
                        return;
                    }
                    
                    village.getItemBank().removeItem(item);
                }
            }
            
            yml.save(upkeepF);
            VillageVillagesUtils.SaveVillage(village);
        } catch(Exception ex) {
            VillageUtils.Error("Failed to save/load Upkeep data yml.", ex);
            return;
        }
    }
    
    public static void DeleteUpkeep(Village village) {
        try {
            File upkeepF = new File(VillageUtils.plugin.getDataFolder() + "/upkeepdata.yml");
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
            VillageUtils.Error("Failed to save Upkeep yml.", ex);
        }
    }
    
}

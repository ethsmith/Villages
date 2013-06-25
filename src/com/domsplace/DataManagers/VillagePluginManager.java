package com.domsplace.DataManagers;

import com.domsplace.Utils.VillageUtils;
import java.io.InputStream;
import org.bukkit.configuration.file.YamlConfiguration;

public class VillagePluginManager {
    
    public static InputStream PluginYMLIS;
    public static YamlConfiguration PluginYML;
    
    public static void LoadPluginYML() {
        PluginYMLIS = VillageUtils.plugin.getResource("plugin.yml");
        PluginYML = YamlConfiguration.loadConfiguration(PluginYMLIS);
        
        //Fix Values
    }
    
    public static String getVersion() {
        return PluginYML.getString("version");
    }
    
    public static String getName() {
        return PluginYML.getString("name");
    }
    
}

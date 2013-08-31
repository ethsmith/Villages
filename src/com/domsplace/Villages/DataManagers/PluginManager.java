package com.domsplace.Villages.DataManagers;

import com.domsplace.Villages.Bases.DataManagerBase;
import com.domsplace.Villages.Enums.ManagerType;
import java.io.IOException;
import java.io.InputStream;
import org.bukkit.configuration.file.YamlConfiguration;

public class PluginManager extends DataManagerBase {
    public InputStream PluginYMLIS;
    public YamlConfiguration PluginYML;
    
    public PluginManager() {
        super(ManagerType.PLUGIN);
    }
    
    @Override
    public void tryLoad() throws IOException {
        PluginYMLIS = getPlugin().getResource("plugin.yml");
        PluginYML = YamlConfiguration.loadConfiguration(PluginYMLIS);
    }
    
    public String getVersion() {
        return PluginYML.getString("version");
    }
    
    public String getString(String key) {
        return this.PluginYML.getString(key);
    }
}

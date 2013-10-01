package com.domsplace.Villages.DataManagers;

import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Enums.ManagerType;
import java.io.IOException;
import java.io.InputStream;
import org.bukkit.configuration.file.YamlConfiguration;

public class PluginManager extends DataManager {
    private YamlConfiguration plugin;
    
    public PluginManager() {
        super(ManagerType.PLUGIN);
    }
    
    @Override
    public void tryLoad() throws IOException {
        if(!getDataFolder().exists()) getDataFolder().mkdir();
        InputStream is = getPlugin().getResource("plugin.yml");
        plugin = YamlConfiguration.loadConfiguration(is);
        is.close();
    }

    public String getVersion() {
        return plugin.getString("version");
    }
}

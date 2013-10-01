package com.domsplace.Villages.DataManagers;

import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Enums.ManagerType;
import java.io.IOException;
import java.io.InputStream;
import org.bukkit.configuration.file.YamlConfiguration;

public class GUIManager extends DataManager {
    private YamlConfiguration config;
    
    public GUIManager() {
        super(ManagerType.CONFIG);
    }
    
    public YamlConfiguration getCFG() {
        return config;
    }
    
    @Override
    public void tryLoad() throws IOException {
        InputStream is = getPlugin().getResource("GUI.yml");
        this.config = YamlConfiguration.loadConfiguration(is);
        String gui = config.getString("gui");
        GUIScreen = gui.replaceAll("\\*", "\n").replaceAll("%x%", DataManager.PLUGIN_MANAGER.getVersion()).replaceAll("TAB", "-----");
    }
}

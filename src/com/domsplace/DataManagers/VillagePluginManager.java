package com.domsplace.DataManagers;

import com.domsplace.Utils.VillageUtils;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

public class VillagePluginManager {
    
    public static InputStream PluginYMLIS;
    public static YamlConfiguration PluginYML;
    
    public static void LoadPluginYML() {
        PluginYMLIS = VillageUtils.plugin.getResource("plugin.yml");
        PluginYML = YamlConfiguration.loadConfiguration(PluginYMLIS);
    }
    
    public static String getVersion() {
        return PluginYML.getString("version");
    }
    
    public static String getName() {
        return PluginYML.getString("name");
    }

    public static List<String> getCommands() {
        ArrayList<String> commands = new ArrayList<String>();
        
        for(String c : ((MemorySection) PluginYML.get("commands")).getKeys(false)) {
            commands.add(c);
        }
        
        return commands;
    }
    
    public static List<Command> getCmds() {
        List<Command> commands = new ArrayList<Command>();
        
        for(String command : getCommands()) {
            Command cmd = VillageUtils.plugin.getCommand(command);
            if(cmd == null) {
                continue;
            }
            
            commands.add(cmd);
        }
        
        return commands;
    }
}

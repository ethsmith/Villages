package com.minecraft.softegg;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Villages extends JavaPlugin {
    public static boolean isPluginEnabled = false;
    public static YamlConfiguration pluginYML;
    
    public final VillageDataManager dataManager = new VillageDataManager();
    public static VillageListener listener;
    
    private final VillagesCommands commands = new VillagesCommands(this);
    
    public PluginManager pluginManager;
    
    @Override
    public void onEnable() {
        VillageUtils.setDataFolder(this.getDataFolder());
        
        InputStream is = this.getResource("plugin.yml");
        pluginYML = YamlConfiguration.loadConfiguration(is);
        
        pluginManager = getServer().getPluginManager();
        
        if(!dataManager.checkConfig(this)) {
            disable();
            return;
        }
        
        List<String> cmds = new ArrayList<String>();
        MemorySection ms = (MemorySection) pluginYML.get("commands");
        for(String command : ms.getKeys(false)) {
            cmds.add(command);
        }
        
        //Register Commands
        for(String command : cmds) {
            getCommand(command).setExecutor(commands);
        }
        
        listener = new VillageListener(this);
        pluginManager.registerEvents(listener, this);
        
        VillageUtils.LoadVillages();
        
        Villages.isPluginEnabled = true;
        Bukkit.broadcastMessage("§dLoaded " + pluginYML.getString("name") + " version " + pluginYML.getString("version") + " successfully.");
    }
    
    @Override
    public void onDisable() {
        if(!Villages.isPluginEnabled) {
            VillageUtils.msgConsole(VillageBase.ChatError + "Plugin failed to load!");
            return;
        }
        
        VillageUtils.SaveAllVillages();
        VillageDataManager.dataManager.saveConfig();
        
        if(listener != null) {
            listener.AutoConfigSave.cancel();
        }
        
        if(VillageDataManager.useSQL()) {
            VillageUtils.sqlClose();
        }
    }
    
    public void disable() {
        pluginManager.disablePlugin(this);
    }
    
    public static com.minecraft.softegg.Villages getVillagesPlugin() {
        try {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Villages");

            if (plugin == null || !(plugin instanceof com.minecraft.softegg.Villages)) {
                return null;
            }

            return (com.minecraft.softegg.Villages) plugin;
        } catch(NoClassDefFoundError e) {
            return null;
        }
    }
}

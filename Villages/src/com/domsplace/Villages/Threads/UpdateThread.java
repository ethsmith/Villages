package com.domsplace.Villages.Threads;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.VillageThread;
import com.domsplace.Villages.DataManagers.PluginManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


public class UpdateThread extends VillageThread {
    public static String CheckUpdateURL = "https://api.curseforge.com/servermods/files?projectIds=59326";
    public static String LatestVersionURL = "http://dev.bukkit.org/bukkit-plugins/villages/";
    public static String APIKey = "5e93f30f51b0fa39fe96360179fd759f09172279";
    
    public UpdateThread() {
        super(10, 3600, true);
    }
    
    @Override
    public void run() {
        if(!getConfig().getBoolean("features.updates", true)) return;
        //Download JSON
        Base.debug("Checking for updates...");
        try {
            URL url = new URL(CheckUpdateURL);
            
            URLConnection urlCon = url.openConnection();
            urlCon.addRequestProperty("X-API-Key", APIKey);
            
            urlCon.addRequestProperty("User-Agent", getPlugin().getName() + "/v"
                    + PluginManager.PLUGIN_MANAGER.getVersion() + " (by " + 
                    PluginManager.PLUGIN_MANAGER.getAuthor() + ")");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
            String response = reader.readLine();
            
            JSONArray array = (JSONArray) JSONValue.parse(response);
            JSONObject latestFile = (JSONObject) array.get(array.size() - 1);
            
            String name = (String) latestFile.get("name");
            name = name.toLowerCase();
            name = name.replaceAll(getPlugin().getName().toLowerCase(), "");
            name = name.replaceAll("version", "");
            name = name.replaceAll(" ", "");
            name = name.replaceAll("v", "");
            
            double onlineVersion = getDouble(name);
            double thisVersion = getDouble(PluginManager.PLUGIN_MANAGER.getVersion());
            
            Base.debug("This Version: " + thisVersion);
            Base.debug("Online Version: " + onlineVersion);
            
            if(thisVersion >= onlineVersion) {
                Base.debug("No Updates Required!");
                return;
            }
            
            broadcast("Villages.admin", new String[]{
                ChatImportant + "The new Version of " + getPlugin().getName() + " is available to download!",
                "Download " + getPlugin().getName() + " v" + onlineVersion + " from: " + LatestVersionURL
            });
            this.stopThread();
        } catch(Exception e) {
            Base.error("Failed to check for updates.", e);
            this.stopThread();
        }
    }
}

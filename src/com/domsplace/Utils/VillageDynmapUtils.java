package com.domsplace.Utils;

import com.domsplace.Objects.Village;
import com.domsplace.VillageBase;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class VillageDynmapUtils extends VillageBase {
    
    public static org.dynmap.markers.MarkerSet set;
    public static HashMap<String, org.dynmap.markers.AreaMarker> markers;
    
    public static boolean setupDynmap() {
        try {
            if(getDynmapAPI() == null) {
                return false;
            }

            org.dynmap.DynmapAPI api = getDynmapAPI();
            org.dynmap.markers.MarkerAPI mApi = api.getMarkerAPI();

            if(mApi == null) {
                dmfail();
                return false;
            }

            set = mApi.getMarkerSet("villages.markerset");
            if(set == null) {
                set = mApi.createMarkerSet("villages.markerset", "Villages", null, false);
            } else {
                set.setMarkerSetLabel("Villages");
            }

            if(set == null) {
                dmfail();
                return false;
            }

            set.setLayerPriority(10);
            set.setHideByDefault(false);

            markers = new HashMap<String, org.dynmap.markers.AreaMarker>();

            return true;
        } catch(NoClassDefFoundError e) {
            return false;
        }
    }
    
    private static void dmfail() {
        VillageUtils.Error("Failed to load Dynmap.", null);
    }
    
    public static void UnloadDynmapRegions() {
        for(org.dynmap.markers.AreaMarker marker : markers.values()) {
            marker.deleteMarker();
        }
    }
    
    public static void FixDynmapRegions() {
        HashMap<String, org.dynmap.markers.AreaMarker> newMarkers = new HashMap<String, org.dynmap.markers.AreaMarker>();
        
        //Iterate over villages and make a marker
        for(Village v : VillageVillagesUtils.getVillages()) {
            String id = v.getTownSpawn().getWorld().getName() + "_" + v.getName();

            double[] x = new double[4];
            double[] z = new double[4];
            
            int minX = v.getLowestChunk().getX() * 16;
            int maxX = v.getHighestChunk().getX() * 16 + 16;
            
            int minZ = v.getLowestChunk().getZ() * 16;
            int maxZ = v.getHighestChunk().getZ() * 16 + 16;
            
            x[0] = minX;
            x[1] = minX;
            x[2] = maxX;
            x[3] = maxX;

            z[0] = minZ;
            z[1] = maxZ;
            z[2] = maxZ;
            z[3] = minZ;

            org.dynmap.markers.AreaMarker marker = markers.remove(id);
            if(marker == null) {
                marker = set.createAreaMarker(id, v.getName(), false, v.getTownSpawn().getWorld().getName(), x, z, false);
                if(marker == null) {
                    continue;
                }
            } else {
                marker.setCornerLocations(x, z);
                marker.setLabel(v.getName());
            }
            marker.setDescription(v.getDescription());

            marker.setLineStyle(5, 0.8, 0x5555FF);
            marker.setFillStyle(0.0, 0);

            newMarkers.put(id, marker);
        }

        for(org.dynmap.markers.AreaMarker marker : markers.values()) {
            marker.deleteMarker();
        }

        markers = newMarkers;
    }
    
    public static boolean canGetDynmapPlugin() {
        try {
            if(getDynmapPlugin() != null) {
                return true;
            }
            return false;
        } catch(NoClassDefFoundError e) {
            return false;
        }
    }
    
    public static org.dynmap.bukkit.DynmapPlugin getDynmapPlugin() {
        try {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("dynmap");

            if (plugin == null || !(plugin instanceof org.dynmap.bukkit.DynmapPlugin)) {
                return null;
            }
            
            if(!plugin.isEnabled()) {
                return null;
            }

            return (org.dynmap.bukkit.DynmapPlugin) plugin;
        } catch(NoClassDefFoundError e) {
            return null;
        }
    }
    
    public static org.dynmap.DynmapAPI getDynmapAPI() {
        try {
            org.dynmap.bukkit.DynmapPlugin plugin = getDynmapPlugin();
            if(plugin == null) {
                return null;
            }

            return (org.dynmap.DynmapAPI) plugin;
        } catch(NoClassDefFoundError e) {
            return null;
        }
    }
}

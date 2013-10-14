/*
 * Copyright 2013 Dominic.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.domsplace.Villages.Objects;

import com.domsplace.Villages.Bases.Base;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapCursor.Type;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;
import org.bukkit.map.MinecraftFont;

/**
 * @author      Dominic
 * @since       13/10/2013
 */
public class VillageMapRenderer extends MapRenderer {
    private static final String MAP_TEXT_COLOR = "\u00A748;";
    private static final Scale USED_SCALE = Scale.NORMAL;
    private static final byte REGION_COLOR = MapPalette.LIGHT_GREEN;
    private static final int MAP_SIZE = 128;
    private static final int HALF_MAP_SIZE = 64;
    public static int REGION_DIVIDE = 1;
    public static int PLAYER_MULTIPLY = 2;
    
    private VillageMap map;
    
    public VillageMapRenderer(VillageMap map) {
        super(false);
        this.map = map;
        this.initialize(this.map.getMapView());
    }
    
    @Override
    public void render(MapView mv, MapCanvas mc, Player player) {
        if(this.map == null) return;
        if(!this.map.getMapView().getRenderers().contains(this)) return;
        //TODO: Finish Rendering.
        mv.setScale(USED_SCALE);
        
        
        Location spawn = map.getVillage().getSpawn().getSafeMiddle();
        mv.setCenterX(Integer.MIN_VALUE);
        mv.setCenterZ(Integer.MIN_VALUE);
        
        for (int x = 0; x < MAP_SIZE; x++) {
            for (int y = 0; y < MAP_SIZE; y++) {
                mc.setPixel(x, y, MapPalette.PALE_BLUE);
            }
        }
        
        MapCursorCollection cursors = mc.getCursors();
        while (cursors.size() > 0) {
            cursors.getCursor(0).setVisible(false);
            cursors.removeCursor(cursors.getCursor(0));
        }
        
        MapCursor spawnCursor = new MapCursor(new Byte("0"), new Byte("0"), new Byte("0"), Type.WHITE_CROSS.getValue(), true);        
        cursors.addCursor(spawnCursor);
        
        //Create Player Cursor
        MapCursor playerCursor = new MapCursor(new Byte("0"), new Byte("0"), new Byte("0"), Type.RED_POINTER.getValue(), true);
        cursors.addCursor(playerCursor);
        
        byte yaw = translate(player.getLocation().getYaw());
        
        playerCursor.setDirection((byte) yaw);
        renderPlayer(player.getLocation(), playerCursor, spawn);
        
        //Draw Regions
        for(Region r : this.map.getVillage().getRegions()) {
            drawRegion(r, spawn, mc);
        }
        //Draws Spawn, but shouldn't be needed drawRegion(this.map.getVillage().getSpawn(), spawn, mc);
        
        mc.setCursors(cursors);
        mc.drawText(2, 2, MinecraftFont.Font, MAP_TEXT_COLOR + "Map for\n" + this.map.getVillage().getName() + ".");
    }
    
    private void drawRegion(Region r, Location l, MapCanvas mc) {
        int endX = translateX(l, r.getX()) / REGION_DIVIDE;
        int endZ = translateZ(l, r.getZ()) / REGION_DIVIDE;
        int startX = translateX(l, r.getMaxX()) / REGION_DIVIDE;
        int startZ = translateZ(l, r.getMaxZ()) / REGION_DIVIDE;
        
        //Base.log("Drawing Region: " + r.toString() + " from " + startX + "," + startZ + " TO " + endX + "," + endZ);
        
        for(int x = startX; x <= endX; x++) {
            for(int z = startZ; z <= endZ; z++) {
                mc.setPixel(HALF_MAP_SIZE + x, HALF_MAP_SIZE + z, REGION_COLOR);
            }
        }
    }
    
    private byte translate(float yaw) {
        while(yaw < 0) yaw = 360+yaw;
        while(yaw >= 360) yaw = yaw-360;
        yaw = -yaw;
        yaw = yaw / 22.5f;
        yaw = (yaw - 8);
        while(yaw < 0) yaw = 15 - yaw;
        while(yaw > 15) yaw = yaw - 15;
        return (byte) yaw;
    }
    
    private void renderPlayer(Location location, MapCursor cursor, Location spawn) {
        int x = translateX(spawn, location) * PLAYER_MULTIPLY;
        int z = translateZ(spawn, location) * PLAYER_MULTIPLY;
        
        if(!Base.isByte(x) || !Base.isByte(z)) {
            cursor.setVisible(false);
            return;
        }
        
        cursor.setVisible(true);
        
        cursor.setX((byte) x);
        cursor.setY((byte) z);
    }
    
    private int translateX(Location spawn, Location toTranslate) {return translateX(spawn, toTranslate.getX());}
    private int translateZ(Location spawn, Location toTranslate) {return translateZ(spawn, toTranslate.getZ());}
    
    private int translateX(Location spawn, double to) {return translate(spawn.getX(), (int) to);}
    private int translateZ(Location spawn, double to) {return translate(spawn.getZ(), (int) to);}
    
    private int translateX(Location spawn, int to) {return translate(spawn.getX(), to);}
    private int translateZ(Location spawn, int to) {return translate(spawn.getZ(), to);}
    
    private int translate(int i, int t) {return (int) translate(i, t);}
    
    private int translate(double spawn, int to) {
        double diff = spawn - to;
        diff = Math.floor(diff / VillageMapRenderer.USED_SCALE.getValue());
        return (int) diff;
    }
}

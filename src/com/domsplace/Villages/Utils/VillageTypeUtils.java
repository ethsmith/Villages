package com.domsplace.Villages.Utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.EntityType;

public class VillageTypeUtils {
    public static List<EntityType> monsterTypes() {
        List<EntityType> type = new ArrayList<EntityType>();
        
        type.add(EntityType.BLAZE);
        type.add(EntityType.CAVE_SPIDER);
        type.add(EntityType.CREEPER);
        type.add(EntityType.ENDERMAN);
        type.add(EntityType.ENDER_DRAGON);
        type.add(EntityType.GHAST);
        type.add(EntityType.GIANT);
        type.add(EntityType.MAGMA_CUBE);
        type.add(EntityType.PIG_ZOMBIE);
        type.add(EntityType.SILVERFISH);
        type.add(EntityType.SKELETON);
        type.add(EntityType.SLIME);
        type.add(EntityType.SPIDER);
        type.add(EntityType.WITHER);
        type.add(EntityType.WITCH);
        type.add(EntityType.WITHER_SKULL);
        type.add(EntityType.ZOMBIE);
        
        return type;
    }
    
    public static boolean isTypeMonster(EntityType ent) {
        for(EntityType ty : monsterTypes()) {
            if(!ty.equals(ent)) continue;
            return true;
        }
        
        return false;
    }
}

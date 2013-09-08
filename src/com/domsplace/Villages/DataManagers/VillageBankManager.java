package com.domsplace.Villages.DataManagers;

import com.domsplace.Villages.Bases.DataManagerBase;
import com.domsplace.Villages.Enums.ManagerType;
import com.domsplace.Villages.Objects.ItemBank;
import com.domsplace.Villages.Objects.Village;
import com.domsplace.Villages.Utils.VillageSQLUtils;
import java.io.File;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class VillageBankManager extends DataManagerBase {
    public VillageBankManager() {
        super(ManagerType.VILLAGE_BANK);
    }
    
    public void saveVillageBank(Village village) {
        ItemBank vBank = village.getItemBank();
        
        if(!getConfigManager().useSQL) return;
        //Dont need to save bank using YML, since the normal village save does that.
        
        //Clear Items
        if(village.idSQL == -1) {
            village.idSQL = VillageSQLUtils.getVillageIDByName(village.getName());
        }
        
        String stmt = "DELETE FROM `VillageBankItems` WHERE VillageID='" + village.idSQL + "';";
        VillageSQLUtils.sqlQuery(stmt);
        
        if(vBank.getItems().size() < 1) {
            return;
        }
        
        stmt = "INSERT INTO `VillageBankItems` ("
                + "`ItemID`,"
                + "`ItemData`,"
                + "`ItemAmount`,"
                + "`VillageID`"
                + ") VALUES (";
        
        //Now add records
        for(ItemStack is : vBank.getItems()) {
            stmt += "'" + is.getTypeId() + "', '" + is.getData().getData() + "', '" + is.getAmount() + "', '" + village.idSQL + "'), (";
        }
        
        stmt = stmt.substring(0, stmt.length() - 3);
        stmt += ";";
        
        VillageSQLUtils.sqlQuery(stmt);
    }
    
    public void loadVillageBank(Village village) {
        if(getConfigManager().useSQL) {
            loadVillageBankSQL(village);
            return;
        }
        
        loadVillageBankYML(village);
    }
    
    public void loadVillageBankSQL(Village village) {
        village.getItemBank().setItems(VillageSQLUtils.getVillageItems(village));
    }
    
    public void loadVillageBankYML(Village village) {
        File villageFile = new File(getDataFolder(), "data/villages/" + village.getName() + ".yml");
        YamlConfiguration vil = YamlConfiguration.loadConfiguration(villageFile);
        
        List<ItemStack> items = getItemFromString(vil.getStringList("bank"));
        village.getItemBank().setItems(items);
    }
}

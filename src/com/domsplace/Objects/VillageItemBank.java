package com.domsplace.Objects;

import com.domsplace.Utils.VillageUtils;
import com.domsplace.VillageBase;
import static com.domsplace.VillageBase.ChatDefault;
import static com.domsplace.VillageBase.ChatImportant;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class VillageItemBank extends VillageBase {
    /* For testing only */
    
    public static VillageItemBank getExampleBank() {
        VillageItemBank itemBank = new VillageItemBank("Test Bank");
        return itemBank;
    }
    
    private String name;
    private Inventory bank;
    
    public VillageItemBank(String name) {
        this.name = name;
        bank = Bukkit.createInventory(null, 27, ChatDefault + "Bank for " + ChatImportant + this.getName());
    }
    
    public VillageItemBank setName(String name) {
        this.name = name;
        return this;
    }
    
    public String getName() {
        return this.name;
    }
    
    public VillageItemBank addItem(ItemStack item) {
        this.getInventory().addItem(item);
        return this;
    }
    
    public VillageItemBank setItems(List<ItemStack> items) {
        this.getInventory().clear();
        for(ItemStack item : items) {
            this.addItem(item);
        }
        return this;
    }
    
    public VillageItemBank addItems(List<ItemStack> items) {
        for(ItemStack item : items) {
            this.addItem(item);
        }
        return this;
    }
    
    public List<ItemStack> getItems() {
        ItemStack[] items = this.getInventory().getContents();
        
        List<ItemStack> list = new ArrayList<ItemStack>();
        
        for(ItemStack is : items) {
            if(is == null) {
                continue;
            }
            list.add(is);
        }
        
        return list;
    }
    
    public List<String> getItemsAsString() {
        List<String> items = new ArrayList<String>();
        
        for(ItemStack is : this.getItems()) {
            if(is == null) {
                continue;
            }
            items.add(is.getTypeId() + ":" + is.getData().getData() + ":" + is.getAmount());
        }
        return items;
    }
    
    public Inventory getInventory() {
        return this.bank;
    }
    
    public VillageItemBank OpenAsInventory(Player opener) {
        opener.openInventory(this.getInventory());
        
        return this;
    }    

    public boolean containsItem(ItemStack item) {
        Material itemNeeded = item.getType();
        byte dataNeeded = item.getData().getData();
        int amountNeeded = item.getAmount();
        
        int amountfound = 0;
        
        for(ItemStack is : this.getItems()) {
            if(is.getType() != itemNeeded) {
                continue;
            }
            
            if(is.getData().getData() != dataNeeded) {
                continue;
            }
            
            amountfound += is.getAmount();
        }
        
        if(amountfound < amountNeeded) {
            return false;
        }
        
        return true;
    }

    public void removeItem(ItemStack item) {
        Material itemNeeded = item.getType();
        byte dataNeeded = item.getData().getData();
        int amountNeeded = item.getAmount();
        
        ArrayList<ItemStack> copy = new ArrayList<ItemStack>(this.getItems());
        ArrayList<ItemStack> shadow = new ArrayList<ItemStack>(copy);
        
        for(ItemStack is : copy) {
            if(is.getType() != itemNeeded) {
                continue;
            }
            
            if(is.getData().getData() != dataNeeded) {
                continue;
            }
            
            //Check whether I need to remove the whole stack or not
            if(is.getAmount() <= amountNeeded) {
                shadow.remove(is);
                amountNeeded -= is.getAmount();
                continue;
            }
            is.setAmount(is.getAmount() - amountNeeded);
            break;
        }
        
        this.setItems(shadow);
    }
}

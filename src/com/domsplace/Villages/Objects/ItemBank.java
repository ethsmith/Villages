package com.domsplace.Villages.Objects;

import com.domsplace.Villages.Bases.ObjectBase;
import static com.domsplace.Villages.Bases.Base.ChatDefault;
import static com.domsplace.Villages.Bases.Base.ChatImportant;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemBank extends ObjectBase {
    public static ItemBank getExampleBank() {
        ItemBank itemBank = new ItemBank("Test Bank");
        return itemBank;
    }
    
    private String name;
    private Inventory bank;
    
    public ItemBank(String name) {
        this.name = name;
        
        int size = 27;
        if(getConfig().getBoolean("largebanks")) {
            size = 54;
        }
        
        bank = Bukkit.createInventory(null, size, ChatDefault + "Bank for " + ChatImportant + this.getName());
    }
    
    public ItemBank setName(String name) {
        this.name = name;
        return this;
    }
    
    public String getName() {
        return this.name;
    }
    
    public ItemBank addItem(ItemStack item) {
        this.getInventory().addItem(item);
        return this;
    }
    
    public ItemBank setItems(List<ItemStack> items) {
        this.getInventory().clear();
        for(ItemStack item : items) {
            this.addItem(item);
        }
        return this;
    }
    
    public ItemBank addItems(List<ItemStack> items) {
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
    
    public ItemBank OpenAsInventory(Player opener) {
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

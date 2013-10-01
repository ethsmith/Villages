package com.domsplace.Villages.Objects;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Exceptions.InvalidItemException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Bank {
    public static Bank getBank(Inventory inv) {
        return Bank.getBank(inv.getTitle());
    }
    
    public static Bank getBank(String title) {
        for(Village v : Village.getVillages()) {
            if(v == null) continue;
            if(v.getBank() == null) continue;
            if(v.getBank().getGUI() == null) continue;
            
            if(v.getBank().getGUI().getName().equalsIgnoreCase(title)) return v.getBank();
        }
        
        return null;
    }
    
    //Instance
    private double wealth;
    private Village village;
    private Inventory bankGUI;
    
    public Bank(Village v) {
        this.wealth = 0d;
        this.village = v;
        this.updateGUI();
    }
    
    public double getWealth() {return this.wealth;}
    public Village getVillage() {return this.village;}
    public Inventory getGUI() {return this.bankGUI;}
    
    public void setWealth(double wealth) {this.wealth = wealth;}

    public void addWealth(double d) {
        this.setWealth(this.getWealth() + d);
    }

    public void delete() {
    }
    
    protected void updateGUI() {
        if(this.bankGUI != null) {
            List<HumanEntity> ents = new ArrayList<HumanEntity>(this.bankGUI.getViewers());
            for(HumanEntity e : ents) {
                if(e == null) continue;
                e.closeInventory();
            }
            
            this.bankGUI.clear();
        }
        
        this.bankGUI = Bukkit.createInventory(null, 54, Base.ChatImportant + this.village.getName() + Base.ChatImportant + " Bank");
    }
    
    private void initGUI() {
        if(this.bankGUI != null) return;
        this.updateGUI();
    }
    
    public List<VillageItem> getItemsFromInventory() {
        this.initGUI();
        List<VillageItem> items = new ArrayList<VillageItem>();
        
        for(ItemStack is : this.bankGUI.getContents()) {
            if(is == null || is.getType() == null) continue;
            items.addAll(VillageItem.itemStackToVillageItems(is));
        }
        
        return items;
    }

    public boolean containsItems(List<VillageItem> relativeItemsCost) {
        return VillageItem.contains(this.getItemsFromInventory(), relativeItemsCost);
    }

    public void addItems(List<VillageItem> items) throws InvalidItemException {
        this.initGUI();
        List<ItemStack> is = VillageItem.toItemStackArray(items);
        for(ItemStack i : is) {
            this.bankGUI.addItem(i);
        }
    }
    
    public void removeItems(List<VillageItem> relativeItemsCost) {
        this.initGUI();
        for(VillageItem i : relativeItemsCost) {
            this.removeItem(i);
        }
    }
    
    public void removeItem(VillageItem item) {
        this.initGUI();
        ItemStack is = null;
        for(ItemStack i : this.bankGUI.getContents()) {
            if(i == null || i.getType() == null || i.getType().equals(Material.AIR)) continue;
            List<VillageItem> isc = VillageItem.itemStackToVillageItems(i);
            if(!VillageItem.contains(isc, item)) continue;
            is = i;
        }
        
        if(is == null) return;
        if(is.getAmount() > 1) {is.setAmount(is.getAmount()- 1); return;}
        
        this.bankGUI.remove(is);
    }

    public void addItem(VillageItem item) throws InvalidItemException {
        List<VillageItem> items = new ArrayList<VillageItem>();
        items.add(item);
        this.addItems(items);
    }
}

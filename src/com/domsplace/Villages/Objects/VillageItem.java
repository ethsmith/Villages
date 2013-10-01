package com.domsplace.Villages.Objects;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Exceptions.InvalidItemException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class VillageItem {
    
    /*
     * String Serialization: 
     * {size:4},{id:17},{data:2},{name:"Hey, this is cool {right?}",{author:DOMIN8TRIX25},{page:"This is my page, I love{it}"},{page:"Hey another page!"},{lore:"Some lore, it's cool {ik}"},{lore:"Anotherlore"},{enchantment:ARROW_DAMAGE*3},{enchantment:OXYGEN*3}
     */
    
    public static final Pattern ITEM_META_SEPERATOR_REGEX = Pattern.compile(",\\s*(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    public static final String ITEM_META_ATTRIBUTE_SEPERATOR_REGEX = "\\{(\\s*)(\\w+)\\:(\\s*)(\".*?(?<!\\\\)(\"))(\\s*)\\}";
    
    public static List<VillageItem> createAllItems(List<String> list) throws InvalidItemException {
        List<VillageItem> items = new ArrayList<VillageItem>();
        
        for(String s : list) {
            items.addAll(VillageItem.createItems(s));
        }
        
        return items;
    }
    
    public static VillageItem createItem(String line) throws InvalidItemException {
        return createItems(line).get(0);
    }
    
    public static List<VillageItem> createItems(String line) throws InvalidItemException {
        try {
            line = line.replaceAll("\\n","\\\\n");
            String[] parts = line.split(ITEM_META_SEPERATOR_REGEX.pattern());
            
            Map<String, String> data = new HashMap<String, String>();
            List<String> lores = new ArrayList<String>();
            List<String> pages = new ArrayList<String>();
            Map<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
            
            for(String s : parts) {
                Matcher m = Pattern.compile(ITEM_META_ATTRIBUTE_SEPERATOR_REGEX).matcher(s);
                Base.debug("GOT: " + s);
                m.find();
                
                String key = m.group(2).toLowerCase();
                String value = m.group(4).replaceFirst("\"", "");
                value = value.substring(0, value.length()-1);
                value = value.replaceAll("&q", "\"");
                
                if(key.equals("page")) {
                    pages.add(value);
                } else if(key.equals("lore")) {
                    lores.add(value);
                } else if(key.equals("enchantment")) {
                    String[] e = value.split("\\*");
                    Enchantment enc = Enchantment.getByName(e[0]);
                    int i = Base.getInt(e[1]);
                    enchants.put(enc, i);
                }
                
                data.put(key, value);
            }
            
            int count = 1;
            int id = 0;
            byte idata = -1;
            String author = null;
            String name = null;
            
            if(data.containsKey("size")) {
                count = Base.getInt(data.get("size"));
            }
            
            if(data.containsKey("id")) {
                id = Base.getInt(data.get("id"));
            }
            
            if(data.containsKey("data")) {
                idata = Base.getByte(data.get("data"));
            }
            
            if(data.containsKey("author")) {
                author = data.get("author");
            }
            
            if(data.containsKey("name")) {
                name = data.get("name");
            }
            
            List<VillageItem> items = new ArrayList<VillageItem>();
            for(int i = 0; i < count; i++) {
                VillageItem item = new VillageItem(id, idata);
                item.setPages(pages);
                item.setLores(lores);
                item.setEnchantments(enchants);
                item.setAuthor(author);
                item.setName(name);
                
                items.add(item);
            }
            
            return items;
        } catch(Exception e) {
                e.printStackTrace();
            throw new InvalidItemException(line);
        }
    }
    
    public static VillageItem copy(VillageItem from) throws InvalidItemException {
        return VillageItem.createItem(from.toString());
    }

    public static List<VillageItem> itemStackToVillageItems(ItemStack is) {
        List<VillageItem> items = new ArrayList<VillageItem>();
        
        VillageItem copy = new VillageItem(is);
        
        for(int i = 0; i < is.getAmount(); i++) {
            try {
                items.add(VillageItem.copy(copy));
            } catch(InvalidItemException e) {
                continue;
            }
        }
        
        return items;
    }
    
    public static boolean contains(List<VillageItem> doesThis, VillageItem containThis) {
        List<VillageItem> items = new ArrayList<VillageItem>();
        return contains(doesThis, items);
    }
    
    public static boolean contains(List<VillageItem> doesThis, List<VillageItem> containThis) {
        if(containThis.size() > doesThis.size()) return false;
        
        List<VillageItem> doesCopy = new ArrayList<VillageItem>(doesThis);
        
        for(VillageItem item : containThis) {
            Base.debug("Checking if bank contains " + item.toHumanString());
            
            boolean found = false;
            VillageItem remove = null;
            for(VillageItem i : doesCopy) {
                Base.debug("Bank contains " + i.toHumanString());
                if(i.compare(item)) found = true;
                remove = i;
                if(found) break;
            }
            
            if(found) {
                doesCopy.remove(remove);
            }
            
            if(!found) return false;
        }
        
        return true;
    }
    
    public static List<String> getHumanMessages(List<VillageItem> items) {
        List<String> s1 = new ArrayList<String>();
        
        for(VillageItem i : items) {
            s1.add(i.toHumanString());
        }
        
        Map<String, Integer> count = new HashMap<String, Integer>();
        
        for(String s : s1) {
            if(!count.containsKey(s)) count.put(s, 0);
            count.put(s, count.get(s)+1);
        }
        
        List<String> s2 = new ArrayList<String>();
        
        for(String s : count.keySet()) {
            s2.add(count.get(s) + " lots of " + s);
        }
        
        return s2;
    }
    
    private static String escape(String s) {
        return s.replaceAll("\\\"", "&q").replaceAll("\\\\n", "\\n");
    }

    static List<ItemStack> toItemStackArray(List<VillageItem> items) throws InvalidItemException {
        List<ItemStack> i = new ArrayList<ItemStack>();
        List<String> s1 = new ArrayList<String>();
        
        for(VillageItem it : items) {
            s1.add(it.toString());
        }
        
        Map<String, Integer> count = new HashMap<String, Integer>();
        
        for(String s : s1) {
            if(!count.containsKey(s)) count.put(s, 0);
            count.put(s, count.get(s)+1);
        }
        
        for(String s : count.keySet()) {
            VillageItem item = VillageItem.createItem(s);
            int amtneeded = count.get(s);
            while(amtneeded > 0) {
                int amttoadd = amtneeded;
                if(amttoadd > 64) amttoadd = 64;
                i.add(item.getItemStack(amttoadd));
                amtneeded -= amttoadd;
            }
        }
        return i;
    }
    
    //Instance
    private int id;
    private byte data = -1;
    private Map<Enchantment, Integer> enchants;
    private List<String> bookPages;
    private String author;
    private String name;
    private List<String> lores;
    
    public VillageItem(int id, byte data, Map<Enchantment, Integer> enchants, List<String> pages, String name, List<String> lores) {
        this.id = id;
        this.data = data;
        this.enchants = enchants;
        this.bookPages = pages;
        this.name = name;
        this.lores = lores;
    }
    
    public VillageItem(int id, byte data, Map<Enchantment, Integer> enchants, List<String> pages, String name) {
        this(id, data, enchants, pages, name, null);
    }
    
    public VillageItem(int id, byte data, Map<Enchantment, Integer> enchants, List<String> pages, List<String> lores) {
        this(id, data, enchants, pages, null, lores);
    }
    
    public VillageItem(int id, byte data, Map<Enchantment, Integer> enchants, String name, List<String> lores) {
        this(id, data, enchants, null, name, lores);
    }
    
    public VillageItem(int id, byte data, Map<Enchantment, Integer> enchants, String name) {
        this(id, data, enchants, null, name, null);
    }
    
    public VillageItem(int id, byte data, Map<Enchantment, Integer> enchants, List<String> lores) {
        this(id, data, enchants, null, null, lores);
    }
    
    public VillageItem(int id, byte data, List<String> pages, String name, List<String> lores) {
        this(id, data, null, pages, name, lores);
    }
    
    public VillageItem(int id, byte data, String name, List<String> lores) {
        this(id, data, null, null, name, lores);
    }
    
    public VillageItem(int id, byte data, String name) {
        this(id, data, name, null);
    }
    
    public VillageItem(int id, byte data, List<String> lores) {
        this(id, data, null, null, null, lores);
    }
    
    public VillageItem(int id, byte data) {
        this(id, data, null, null, null, null);
    }
    
    public VillageItem(int id) {
        this(id, new Byte("-1"));
    }
    
    public VillageItem(Material m) {
        this(m.getId());
    }
    
    public VillageItem(ItemStack is) {
        this(
            is.getType().getId(),
            is.getData().getData()
        );
        
        if(is.getItemMeta() != null) {
            if(is.getItemMeta().getLore() != null) {
                if(is.getItemMeta().getLore().size() > 0) {
                    this.lores = new ArrayList<String>(is.getItemMeta().getLore());
                }
            }
            if(is.getItemMeta().getDisplayName() != null) {
                if(!is.getItemMeta().getDisplayName().equalsIgnoreCase("")) {
                    this.name = is.getItemMeta().getDisplayName();
                }
            }
            
            if(is.getItemMeta() instanceof BookMeta) {
                BookMeta book = (BookMeta) is.getItemMeta();
                this.bookPages = new ArrayList<String>(book.getPages());
                this.author = book.getAuthor();
            }
        }
        
        this.enchants = new HashMap<Enchantment, Integer>(is.getEnchantments());
    }
    
    public int getID() {return this.id;}
    public byte getData() {return this.data;}
    public Map<Enchantment, Integer> getEnchantments() {return this.enchants;}
    public List<String> getBookPages() {return this.bookPages;}
    public String getBookAuthor() {return this.author;}
    public String getName() {return this.name;}
    public List<String> getLores() {return this.lores;}
    public String getTypeName() {
        return Base.capitalizeEachWord(this.getMaterial().name().replaceAll("_", " ").toLowerCase());
    }
    
    public void setID(int id) {this.id = id;}
    public void setData(byte data) {this.data = data;}
    public void setLores(List<String> lores) {this.lores = lores;}
    public void setPages(List<String> pages) {this.bookPages = pages;}
    public void setAuthor(String author) {this.author = author;}
    public void setName(String name) {this.name = name;}
    public void setEnchantments(Map<Enchantment, Integer> e) {this.enchants = e;}

    public void setPage(int page, String l) {this.bookPages.set(page, l);}
    
    public Material getMaterial() {return Material.getMaterial(this.id);}
    public MaterialData getMaterialData() {return getMaterial().getNewData(this.data);}
    public ItemMeta getItemMeta(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        if(this.name != null && !this.name.equals("")) {
            im.setDisplayName(this.name);
        }
        
        if(im instanceof BookMeta) {
            BookMeta bm = (BookMeta) im;
            if(this.author != null && !this.author.equals("")) {
                bm.setAuthor(this.author);
            }
            
            if(this.bookPages != null) {
                bm.setPages(this.bookPages);
            }
        }
        
        if(this.lores != null) {
            im.setLore(this.lores);
        }
        
        return im;
    }
    public ItemStack getItemStack() {return getItemStack(64);}
    public ItemStack getItemStack(int amt) {
        ItemStack is = new ItemStack(this.id);
        is.setAmount(amt);
        is.setData(this.getMaterialData());
        is.setItemMeta(this.getItemMeta(is));
        return is;
    }
    public Block setBlock(Block b) {
        b.setTypeIdAndData(id, data, true);
        return b;
    }
    
    public boolean compare(VillageItem item) {
        if(item.id != this.id) return false;
        if(this.data >= 0 && item.data >= 0) {
            if(this.data != item.data) return false;
        }
        
        if(this.name != null || item.name != null) {
            if(this.name == null || item.name == null) return false;
            if(!this.name.equals(item.name)) return false;
        }
        
        if(this.author != null || item.author != null) {
            if(this.author == null || item.author == null) return false;
            if(!this.author.equals(item.author)) return false;
        }
        
        if(this.lores != null || item.lores != null) {
            if(this.lores == null || item.lores == null) return false;
            if(this.lores.size() != item.lores.size()) return false;
            for(String s : this.lores) {
                if(!item.lores.contains(s)) return false;
            }
        }
        
        if(this.bookPages != null || item.bookPages != null) {
            if(this.bookPages == null || item.bookPages == null) return false;
            if(this.bookPages.size() != item.bookPages.size()) return false;
            for(String s : this.bookPages) {
                if(!item.bookPages.contains(s)) return false;
            }
        }
        
        if(this.enchants != null || item.enchants != null) {
            if(this.enchants == null || item.enchants == null) return false;
            if(this.enchants.size() != item.enchants.size()) return false;
            for(Enchantment e : this.enchants.keySet()) {
                if(!item.enchants.containsKey(e)) return false;
                if(item.enchants.get(e) != this.enchants.get(e)) return false;
            }
        }
        
        return true;
    }
    
    public void addLore(String l) {this.lores.add(l);}
    public void addEnchantment(Enchantment byId, int lvl) {this.enchants.put(byId, lvl);}
    public void addPage(String l) {this.bookPages.add(l);}
    
    @Override
    public String toString() {
        String msg = "{id:\"" + this.id + "\"}";
        
        if(this.data >= 0) {
            msg += ",{data:\"" + this.data + "\"}";
        }
        
        if(this.lores != null) {
            for(String lore : this.lores) {
                msg += ",{lore:\"" + escape(lore) + "\"}";
            }
        }
        
        if(this.bookPages != null) {
            for(String page : this.bookPages) {
                msg += ",{page:\"" + escape(page) + "\"}";
            }
        }
        
        if(this.name != null && !this.name.equals("")) {
            msg += ",{name:\"" + escape(this.name) + "\"}";
        }
        
        if(this.author != null && !this.author.equals("")) {
            msg += ",{author:\"" + this.author + "\"}";
        }
        
        if(this.enchants != null) {
            for(Enchantment e : this.enchants.keySet()) {
                if(e == null) continue;
                msg += ",{enchantment: \"" + e.getName() + "*" + this.enchants.get(e) + "\"}";
            }
        }
        
        return msg;
    }

    public String toHumanString() {
        String s = this.getTypeName();
        
        if(this.data >= 0) {
            s += ", with type of " + this.data;
        }
        
        if(this.name != null && !this.name.equals("")) {
            s += ", named " + this.name;
        }
        
        if(this.enchants != null && this.enchants.size() > 0) {
            s += ", with the enchantments";
            for(Enchantment e : this.enchants.keySet()) {
                if(e == null) continue;
                s += ", " + Base.capitalizeEachWord(e.getName().replaceAll("_", " ").toLowerCase()) + " at level " + enchants.get(e)    ;
            }
        }
        
        if(this.lores != null && this.lores.size() > 0) {
            s += ", with the lores";
            for(String l : lores) {
                s += ", " + l;
            }
        }
        
        if(this.bookPages != null && this.bookPages.size() > 0) {
            s += ", and with the pages ";
            for(String p : this.bookPages) {
                s += ", \"" + p + "\"";
            }
        }
        
        if(this.author != null && !this.author.equals("")) {
            s += ", written by " + this.author;
        }
        
        return s;
    }
}

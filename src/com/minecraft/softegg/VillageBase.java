package com.minecraft.softegg;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VillageBase {
    public static String ChatError = ChatColor.RED + "";
    public static String ChatDefault = ChatColor.GRAY + "";
    public static String ChatImportant = ChatColor.BLUE + "";
    public static String ChatPrefix = "§7[§9Villages§7]";
    
    public static void SendMessage(CommandSender cs, String message) {
        cs.sendMessage(ChatPrefix + ChatDefault + message);
    }
    public static void SendMessage(CommandSender cs, String[] message) {
        for(String msg : message) {
            SendMessage(cs, msg);
        }
    }
    
    public static void SendMessage(CommandSender cs, List<String> message) {
        for(String msg : message) {
            SendMessage(cs, msg);
        }
    }
    
    public static void NoPermission(CommandSender cs) {
        SendMessage(cs, ChatError + Villages.pluginYML.getString("permission"));
    }
    
    public static void Broadcast(String msg) {
        for(World w : VillageDataManager.getVillageWorlds()) {
            for(Player p : w.getPlayers()) {
                p.sendMessage(msg);
            }
        }
        Bukkit.getConsoleSender().sendMessage(msg);
    }
    
    public static void Broadcast(String[] msgs) {
        for(World w : VillageDataManager.getVillageWorlds()) {
            for(Player p : w.getPlayers()) {
                for(String msg : msgs) {
                    p.sendMessage(msg);
                }
            }
        }
        Bukkit.getConsoleSender().sendMessage(msgs);
    }
    
    public static String gK(String key) {
        return VillageLanguageManager.getKey(key);
    }
    
    public static String gK(String key, Village village) {
        return VillageLanguageManager.getKey(key, village);
    }
    
    public static String gK(String key, double money) {
        return VillageLanguageManager.getKey(key, money);
    }
    
    public static String gK(String key, OfflinePlayer player) {
        return VillageLanguageManager.getKey(key, player);
    }
    
    public static String gK(String key, OfflinePlayer player, double amount) {
        return VillageLanguageManager.getKey(key, player, amount);
    }
}

package com.domsplace.Utils;

import org.bukkit.plugin.RegisteredServiceProvider;

public class VillageEconomyUtils {
    public static net.milkbowl.vault.economy.Economy economy;
    
    public static Boolean setupEconomy() {
        try {
            RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> economyProvider = VillageUtils.plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (economyProvider != null) {
                economy = economyProvider.getProvider();
            }
            return (economy != null);
        } catch(NoClassDefFoundError e) {
            economy = null;
            return false;
        } catch(Exception ex) {
            economy = null;
            return false;
        }
    }
}

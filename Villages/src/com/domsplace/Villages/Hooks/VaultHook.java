package com.domsplace.Villages.Hooks;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.PluginHook;
import com.domsplace.Villages.Commands.SubCommands.Bank.VillageBankDeposit;
import com.domsplace.Villages.Commands.SubCommands.Bank.VillageBankWithdraw;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook extends PluginHook {
    private Economy economy = null;
    
    //Economy Based Commands
    VillageBankDeposit bankDeposit;
    VillageBankWithdraw bankWithdraw;
    
    public VaultHook() {
        super("Vault");
    }
    
    public Economy getEconomy() {
        try {
            return economy;
        } catch(NoClassDefFoundError e) {
            return null;
        }
    }
    
    private boolean setupEconomy() {
        try {
            RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
            if (economyProvider != null) {
                economy = economyProvider.getProvider();
            }

            return (economy != null);
        } catch(NoClassDefFoundError e) {
            economy = null;
            return false;
        }
    }
    
    @Override
    public void onHook() {
        super.onHook();
        this.setupEconomy();
        Base.useEconomy = this.economy != null;
        if(Base.useEconomy) {
            if(getConfig().getBoolean("features.banks.money", true)) {
                bankDeposit = new VillageBankDeposit();
                bankWithdraw = new VillageBankWithdraw();
            }
        } else if(this.isHooked()) {
            log("Hooked into Vault, but can't find any Economy!");
        }
    }
    
    @Override
    public void onUnhook() {
        super.onUnhook();
        economy = null;
        Base.useEconomy = false;
        
        if(bankDeposit != null) {
            bankDeposit.deRegister();
            bankDeposit = null;
        }
        
        if(bankWithdraw != null) {
            bankWithdraw.deRegister();
            bankWithdraw = null;
        }
    }
    
    public String formatEconomy(double amt) {
        if(!Base.useEconomy) return "\\\\\\\\\\\\$" + amt;
        String formatted = this.getEconomy().format(amt);
        formatted = formatted.replaceAll("\\$", "\\\\\\$");  
        return formatted;
    }
}

package me.andrej123456789.transitcard;

import me.andrej123456789.transitcard.commands.BuyCard;
import me.andrej123456789.transitcard.commands.Help;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

import java.io.File;

public final class TransitCard extends JavaPlugin {

    private static Economy econ = null;

    @Override
    public void onEnable() {
        // Plugin startup logic

        /* Load default config */
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        File tomlConfig = new File(getDataFolder(), "config.toml");
        if (!tomlConfig.exists()) {
            saveResource("config.toml", false);
        }

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("transit_card").setExecutor(new Help());
        getCommand("buy_card").setExecutor(new BuyCard());

        getLogger().info("Initialization of TransitCard is done!");
        getServer().getConsoleSender().sendMessage("[TransitCard] If you like this plugin, give it a star on Github: "
                + ChatColor.AQUA + "https://github.com/Andrej123456789/TransitCard" + ChatColor.RESET);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }
}

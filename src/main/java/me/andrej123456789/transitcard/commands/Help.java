package me.andrej123456789.transitcard.commands;

import me.andrej123456789.transitcard.TransitCard;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Help implements CommandExecutor, TabExecutor {

    private static final Plugin plugin = JavaPlugin.getProvidingPlugin(TransitCard.class);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage("GitHub: " + ChatColor.AQUA + "https://github.com/Andrej123456789/TransitCard\n" + ChatColor.RESET);

        sender.sendMessage("/metro_card [hours] - buy metro card (if hours argument is not passed, card is valid for 1 hour)");
        sender.sendMessage("/monorail_card [hours] - buy monorail card");

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>(); /* null = all player names */
    }
}

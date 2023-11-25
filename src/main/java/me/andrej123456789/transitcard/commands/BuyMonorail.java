package me.andrej123456789.transitcard.commands;

import me.andrej123456789.transitcard.TransitCard;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.andrej123456789.transitcard.TransitCard.getEconomy;

public class BuyMonorail implements CommandExecutor, TabExecutor {

    private static final Plugin plugin = JavaPlugin.getProvidingPlugin(TransitCard.class);

    public static String removeLetterH(String input) {
        // Use regular expression to replace all occurrences of 'h' (case-insensitive)
        String result = input.replaceAll("(?i)h", "");
        return result;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only in-game players can use this command!");

            return true;
        }

        int hours = 0;

        args[0] = removeLetterH(args[0]);

        if (args.length == 0)
        {
            /* buy for 1h */
            hours = 1;
        }

        else
        {
            /* buy for args[0] hours */
            hours = Integer.parseInt(args[0]);
        }

        Double balance = getEconomy().getBalance((OfflinePlayer) sender);
        getEconomy().withdrawPlayer((OfflinePlayer) sender, plugin.getConfig().getDouble("monorail_card_price") * hours);

        ItemStack minecart = new ItemStack(Material.MINECART);
        ItemMeta minecart_meta = minecart.getItemMeta();

        minecart_meta.setDisplayName("Monorail Minecart (" + Integer.toString(hours) + "h)");
        minecart.setItemMeta(minecart_meta);

        ((Player) sender).getInventory().addItem(minecart);

        // Use paper cards too
        if (!plugin.getConfig().getBoolean("electronic_card"))
        {
            ItemStack paper_card = new ItemStack(Material.PAPER);
            ItemMeta paper_card_meta = paper_card.getItemMeta();

            paper_card_meta.setDisplayName("Monorail card (" + Integer.toString(hours) + "h)");
            paper_card.setItemMeta(paper_card_meta);

            ((Player) sender).getInventory().addItem(paper_card);
        }

        sender.sendMessage(ChatColor.GREEN + "You have received your monorail card for " + Integer.toString(hours) + "h" + ChatColor.RESET);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("1h", "2h", "3h", "4h", "5h", "6h", "7h", "8h");
        }

        return new ArrayList<>(); /* null = all player names */
    }
}

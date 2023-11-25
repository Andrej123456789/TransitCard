package me.andrej123456789.transitcard.commands;

import com.moandjiezana.toml.Toml;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static me.andrej123456789.transitcard.TransitCard.getEconomy;

public class BuyCard implements CommandExecutor, TabExecutor {

    private static final Plugin plugin = JavaPlugin.getProvidingPlugin(TransitCard.class);

    private static String readFileToString(String filePath) throws IOException {
        Path file = Paths.get(filePath);
        byte[] fileBytes = Files.readAllBytes(file);
        return new String(fileBytes, StandardCharsets.UTF_8);
    }

    private static String removeLetterH(String input) {
        // Use regular expression to replace all occurrences of 'h' (case-insensitive)
        return input.replaceAll("(?i)h", "");
    }

    private static String capitalizeString(String input) {
        String[] words = input.split("_");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        return result.toString().trim();
    }

    // Method to remove underscore characters
    private static String removeUnderscores(String input) {
        return input.replace("_", "");
    }

    private static String capitalizeAndRemoveUnderscore(String input) {
        String result = input;

        result = capitalizeString(result);
        result = removeUnderscores(result);

        return result;
    }

    private static Map<String, Object> getPrices(Toml toml, @NotNull CommandSender sender) {
        // Get all keys under the 'prices' sub-config
        Toml tomlPrices = toml.getTable("prices");
        Map<String, Object> prices;

        if (tomlPrices == null) {
            sender.sendMessage(ChatColor.YELLOW + "Prices sub-config in config.toml not found!" + ChatColor.RESET);
            return null;
        }

        prices = tomlPrices.toMap();
        return prices;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only in-game players can use this command!");

            return true;
        }

        Integer hours = Integer.valueOf(removeLetterH(args[1]));

        if (args.length < 1) {
            return false;
        }

        if (args.length == 1) {
            hours = 1;
        }

        Toml toml;

        try {
            toml = new Toml().read(readFileToString(plugin.getDataFolder() + "/config.toml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> prices = getPrices(toml, sender);

        if (!prices.containsKey(args[0])) {
            sender.sendMessage(ChatColor.YELLOW + "Transit not found in prices list!" + ChatColor.RESET);
            return true;
        }

        String transit = args[0];

        if (!sender.hasPermission("transit_card.buy." + transit)) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to buy this transit card!" + ChatColor.RESET);
        }

        /* Give minecart to player */

        getEconomy().withdrawPlayer((OfflinePlayer) sender, toml.getDouble("prices." + transit) * hours);

        ItemStack minecart = new ItemStack(Material.MINECART);
        ItemMeta minecart_meta = minecart.getItemMeta();

        minecart_meta.setDisplayName(capitalizeAndRemoveUnderscore(transit) + " Minecart (" + Integer.toString(hours) + "h)");
        minecart.setItemMeta(minecart_meta);

        ((Player) sender).getInventory().addItem(minecart);

        // Use paper cards too
        if (!plugin.getConfig().getBoolean("electronic_card"))
        {
            ItemStack paper_card = new ItemStack(Material.PAPER);
            ItemMeta paper_card_meta = paper_card.getItemMeta();

            paper_card_meta.setDisplayName(capitalizeAndRemoveUnderscore(transit) + " card (" + Integer.toString(hours) + "h)");
            paper_card.setItemMeta(paper_card_meta);

            ((Player) sender).getInventory().addItem(paper_card);
        }

        sender.sendMessage(ChatColor.GREEN + "You have received your " + capitalizeAndRemoveUnderscore(transit) + " card for " + Integer.toString(hours) + "h" + ChatColor.RESET);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            Toml toml;

            try {
                toml = new Toml().read(readFileToString(plugin.getDataFolder() + "/config.toml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Map<String, Object> prices = getPrices(toml, sender);

            ArrayList<String> keys = new ArrayList<>();

            if (prices != null) {
                prices.forEach((key, value) -> keys.add(key));
            }

            return keys;
        }

        if (args.length == 2) {
            return Arrays.asList("1h", "2h", "3h", "4h", "5h", "6h", "7h", "8h");
        }

        return new ArrayList<>(); /* null = all player names */
    }
}

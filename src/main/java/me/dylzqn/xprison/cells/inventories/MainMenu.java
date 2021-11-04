package me.dylzqn.xprison.cells.inventories;

import me.dylzqn.xprison.cells.xCell;
import me.dylzqn.xprison.inventory.InventoryBuilder;
import me.dylzqn.xprison.inventory.InventoryPermission;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainMenu implements InventoryBuilder {

    private xPrisonCore prisonCore;
    private xCell cell;
    private Inventory inventory;
    private HashMap<Integer, ItemStack> itemStacks;

    public MainMenu(xPrisonCore prisonCore, xCell cell) {
        this.prisonCore = prisonCore;
        this.cell = cell;
        this.itemStacks = new HashMap<>();
        inventory = prisonCore.getServer().createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&eCELL MENU - " + cell.getID()));
    }

    @Override
    public String getName() {
        return "Main Menu";
    }

    @Override
    public InventoryPermission getInventoryPerm() {
        return InventoryPermission.USE;
    }

    @Override
    public HashMap<Integer, ItemStack> getItemStacks() {
        return itemStacks;
    }

    @Override
    public Inventory getInventory() {

        ItemStack clock = new ItemStack(Material.WATCH);
        ItemMeta clockMeta = clock.getItemMeta();
        clockMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&l>> &e&lCELL RENTAL"));
        List<String> clockLore = new ArrayList<>();
        clockLore.add("");
        clockLore.add("");
        clockLore.add(ChatColor.translateAlternateColorCodes('&', "&6&l> &eTime Remaining"));
        clockLore.add(ChatColor.translateAlternateColorCodes('&', "&7" + cell.getFormattedTimeRemaining()));
        clockMeta.setLore(clockLore);
        clock.setItemMeta(clockMeta);
        inventory.setItem(10, clock);

        ItemStack anvil = new ItemStack(Material.ANVIL);
        ItemMeta anvilMeta = anvil.getItemMeta();
        anvilMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&l>> &e&lCELL MEMBERS &8&o(Coming Soon)"));
        anvil.setItemMeta(anvilMeta);
        inventory.setItem(11, anvil);

        ItemStack beacon = new ItemStack(Material.BEACON);
        ItemMeta beaconMeta = beacon.getItemMeta();
        beaconMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&l>> &e&lCELL HOME &8&o(Coming Soon)"));
        beacon.setItemMeta(beaconMeta);
        inventory.setItem(13, beacon);

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        barrierMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&l>> &c&lUNCLAIM CELL"));
        barrier.setItemMeta(barrierMeta);
        inventory.setItem(16, barrier);

        ItemStack blackglass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3);
        ItemMeta glassMeta = blackglass.getItemMeta();
        glassMeta.setDisplayName(" ");
        blackglass.setItemMeta(glassMeta);
        for (int i = 0; i < 27; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, blackglass);
            }
        }

        return inventory;
    }

    public xCell getCell() {
        return cell;
    }

}

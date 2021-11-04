package me.dylzqn.xprison.cells.inventories;

import me.dylzqn.xprison.ItemBuilder;
import me.dylzqn.xprison.cells.xCell;
import me.dylzqn.xprison.inventory.InventoryBuilder;
import me.dylzqn.xprison.inventory.InventoryPermission;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class CellTime implements InventoryBuilder {

    private xPrisonCore prisonCore;
    private xCell cell;
    private Inventory inventory;
    private HashMap<Integer, ItemStack> itemStacks;

    public CellTime(xPrisonCore prisonCore, xCell cell) {
        this.prisonCore = prisonCore;
        this.cell = cell;
        this.inventory = prisonCore.getServer().createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&d&lCell Time"));
        this.itemStacks = new HashMap<>();
    }

    @Override
    public String getName() {
        return "Cell Time";
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

        ItemStack clock1 = new ItemStack(Material.WATCH);
        ItemMeta clock1Meta = clock1.getItemMeta();
        clock1Meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&l+ 1 DAY"));
        clock1.setItemMeta(clock1Meta);
        inventory.setItem(10, clock1);

        ItemBuilder clock2 = new ItemBuilder(Material.WATCH).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&l+ 2 DAYS"));
        inventory.setItem(12, clock2.build());

        ItemBuilder clock3 = new ItemBuilder(Material.WATCH).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&l+ 5 DAYS"));
        inventory.setItem(14, clock3.build());

        ItemBuilder clock4 = new ItemBuilder(Material.WATCH).setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&l+ 7 DAYS"));
        inventory.setItem(16, clock4.build());

        return inventory;
    }
}

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

import java.util.HashMap;

public class Confirm implements InventoryBuilder {

    private xPrisonCore prisonCore;
    private xCell cell;
    private HashMap<Integer, ItemStack> itemStacks;
    private Inventory inventory;

    public Confirm(xPrisonCore prisonCore, xCell cell) {
        this.prisonCore = prisonCore;
        this.cell = cell;
        this.itemStacks = new HashMap<>();
        this.inventory = prisonCore.getServer().createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&aConfirm              &cDecline"));
    }

    @Override
    public String getName() {
        return "Confirm";
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

        ItemStack confirm = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aCONFIRM"));
        confirm.setItemMeta(confirmMeta);

        inventory.setItem(0, confirm);
        inventory.setItem(1, confirm);
        inventory.setItem(2, confirm);
        inventory.setItem(3, confirm);
        inventory.setItem(9, confirm);
        inventory.setItem(10, confirm);
        inventory.setItem(11, confirm);
        inventory.setItem(12, confirm);
        inventory.setItem(18, confirm);
        inventory.setItem(19, confirm);
        inventory.setItem(20, confirm);
        inventory.setItem(21, confirm);

        ItemStack decline = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta declineMeta = decline.getItemMeta();
        declineMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cDECLINE"));
        decline.setItemMeta(declineMeta);

        inventory.setItem(5, decline);
        inventory.setItem(6, decline);
        inventory.setItem(7, decline);
        inventory.setItem(8, decline);
        inventory.setItem(14, decline);
        inventory.setItem(15, decline);
        inventory.setItem(16, decline);
        inventory.setItem(17, decline);
        inventory.setItem(23, decline);
        inventory.setItem(24, decline);
        inventory.setItem(25, decline);
        inventory.setItem(26, decline);

        ItemStack ironBar = new ItemStack(Material.IRON_FENCE);
        ItemMeta ironMeta = ironBar.getItemMeta();
        ironMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6" + cell.getID()));
        ironBar.setItemMeta(ironMeta);
        inventory.setItem(13, ironBar);

        return inventory;
    }

    public xCell getCell() {
        return cell;
    }
}

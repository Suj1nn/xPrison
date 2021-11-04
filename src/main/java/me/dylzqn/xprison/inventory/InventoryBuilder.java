package me.dylzqn.xprison.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public interface InventoryBuilder {

    String getName();
    InventoryPermission getInventoryPerm();
    HashMap<Integer, ItemStack> getItemStacks();
    Inventory getInventory();
}

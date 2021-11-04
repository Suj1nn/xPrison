package me.dylzqn.xprison.kits;

import me.dylzqn.xcraftapi.api.game.xKit;
import me.dylzqn.xcraftapi.api.player.Rank;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StarterKit implements xKit {

    private xPrisonCore prisonCore;
    private List<ItemStack> itemStacks;

    public StarterKit(xPrisonCore prisonCore) {
        this.prisonCore = prisonCore;
        this.itemStacks = new ArrayList<>();

        this.itemStacks.add(new ItemStack(Material.STONE_PICKAXE, 1));
        this.itemStacks.add(new ItemStack(Material.STONE_SPADE, 1));
        this.itemStacks.add(new ItemStack(Material.STONE_AXE, 1));

        for (ItemStack itemStack : itemStacks) {
            itemStack.addEnchantment(Enchantment.DIG_SPEED, 1);
            itemStack.addEnchantment(Enchantment.DURABILITY, 1);
        }
    }

    @Override
    public String getKitName() {
        return "Starter";
    }

    @Override
    public Rank getRequiredRank() {
        return Rank.MEMBER;
    }

    @Override
    public int getCoolDown() {
        return 600;
    }

    @Override
    public List<ItemStack> getItems() {
        return itemStacks;
    }
}

package me.dylzqn.xprison.kits;

import me.dylzqn.xcraftapi.api.game.xKit;
import me.dylzqn.xcraftapi.api.player.Rank;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * *******************************************************************
 * » Copyright Dylzqn (c) 2015. All rights Reserved.
 * » Any code contained within this document, and any associated APIs with similar branding
 * » are the sole property of Dylzqn. Distribution, reproduction, taking snippets, or
 * » claiming any contents as your own will break the terms of the licence, and void any
 * » agreements with you. the third party.
 * » Thanks :D
 * ********************************************************************
 */
public class DiamondKit implements xKit {

    private xPrisonCore prisonCore;
    private List<ItemStack> itemStacks;

    public DiamondKit(xPrisonCore prisonCore) {
        this.prisonCore = prisonCore;
        this.itemStacks = new ArrayList<>();
    }

    @Override
    public String getKitName() {
        return "Diamond";
    }

    @Override
    public Rank getRequiredRank() {
        return Rank.DIAMOND;
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

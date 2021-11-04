package me.dylzqn.xprison;

import me.dylzqn.xprison.ward.Ward;
import org.bukkit.ChatColor;

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
public enum WardType {

    X(10, "&7[&5X&7]", 10000),
    C(9, "&7[&dC&7]", 20000),
    R(8, "&7[&2R&7]", 30000),
    A(7, "&7[&cA&7]", 50000),
    F(6, "&7[&aF&7]", 75000),
    T(5, "&7[&6T&7]", 100000),

    ;

    int id;
    String prefix;
    int amount;
    Ward ward;

    WardType(int id, String prefix, int amount) {
        this.id = id;
        this.prefix = prefix;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', prefix);
    }

    public int getAmount() {
        return amount;
    }
}

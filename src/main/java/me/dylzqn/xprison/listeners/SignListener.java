package me.dylzqn.xprison.listeners;

import me.dylzqn.xcraftapi.api.locations.Region;
import me.dylzqn.xcraftapi.api.locations.Selection;
import me.dylzqn.xcraftapi.api.player.Rank;
import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xcraftapi.api.utils.MSGType;
import me.dylzqn.xprison.cells.xCell;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

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
public class SignListener implements Listener {

    private xPrisonCore prisonCore;

    public SignListener(xPrisonCore prisonCore) {
        this.prisonCore = prisonCore;
    }

    @EventHandler
    public void onSignPlace(SignChangeEvent e) {

        if (e.getBlock().getState() instanceof Sign) {
            Location signLocation = e.getBlock().getLocation();
            Player player = e.getPlayer();
            xCraftPlayer craftPlayer = prisonCore.getApiCore().getxCraftPlayer(player.getUniqueId());

            if (Objects.requireNonNull(e.getLine(0)).equalsIgnoreCase("[xCell]")) {
                if (craftPlayer.getRank().getID() <= Rank.DEVELOPER.getID()) {
                    if (e.getLines().length < 3) {
                        craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "You require more lines");
                        return;
                    }
                    int price = Integer.parseInt(Objects.requireNonNull(e.getLine(2)));

                    xCell cell = prisonCore.getCellManager().getCell(e.getLine(1));

                    if (cell == null) {
                        Selection selection = (Selection) craftPlayer.getOtherData().get("Selection");
                        if (!selection.isComplete()) {
                            craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "You have finish your selection first");
                            return;
                        }
                        Region region = prisonCore.getApiCore().getRegionManager().createRegion(e.getLine(1), selection, false);
                        cell = prisonCore.getCellManager().createCell(e.getLine(1), region, price, signLocation, null);

                        e.setLine(0, ChatColor.translateAlternateColorCodes('&', "&6&lxCELL"));
                        e.setLine(1, ChatColor.translateAlternateColorCodes('&', "&b" + cell.getID()));
                        e.setLine(2, ChatColor.translateAlternateColorCodes('&', "&b$" + cell.getPrice()));
                        e.setLine(3, ChatColor.translateAlternateColorCodes('&', "&7" + "Click to Buy"));

                    } else {

                        if (!cell.isOccupied()) {
                            e.setLine(0, ChatColor.translateAlternateColorCodes('&', "&6&lxCELL"));
                            e.setLine(1, ChatColor.translateAlternateColorCodes('&', "&b" + cell.getID()));
                            e.setLine(2, ChatColor.translateAlternateColorCodes('&', "&b$" + cell.getPrice()));
                            e.setLine(3, ChatColor.translateAlternateColorCodes('&', "&7" + "Click to Buy"));
                            return;
                        }

                        e.setLine(0, ChatColor.translateAlternateColorCodes('&', "&6&lxCELL"));
                        e.setLine(1, ChatColor.translateAlternateColorCodes('&', "&b" + cell.getID()));
                        e.setLine(2, ChatColor.translateAlternateColorCodes('&', "&b" + prisonCore.getServer().getOfflinePlayer(cell.getOwnerUUID()).getName()));
                        e.setLine(3, ChatColor.translateAlternateColorCodes('&', "&7" + prisonCore.formatTime(cell.getEndTime() / 1000)));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSignInteract(PlayerInteractEvent e) {

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (Objects.requireNonNull(e.getClickedBlock()).getState() instanceof Sign) {
                Sign sign = (Sign) e.getClickedBlock().getState();
                if (sign.getLine(0).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&6&lxCell"))) {
                    Player player = e.getPlayer();
                    xCraftPlayer craftPlayer = prisonCore.getApiCore().getxCraftPlayer(player.getUniqueId());
                    xCell cell = prisonCore.getCellManager().getCell(ChatColor.stripColor(sign.getLine(1)));

                    if (cell.isOccupied()) {
                        if (!player.getUniqueId().equals(cell.getOwnerUUID())) {
                            craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "This cell does not belong to you.");
                            return;
                        }
                        player.openInventory(cell.getInventory("Main Menu"));
                    } else {
                        int money = (int) craftPlayer.getOtherData().get("Money");

                        if (money < cell.getPrice()) {
                            craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "You don't have enough money");
                            return;
                        }

                        craftPlayer.getPlayer().openInventory(cell.getInventory("Confirm"));
                    }
                    e.setCancelled(true);
                }
            }
        }
    }
}

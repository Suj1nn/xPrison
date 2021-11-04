package me.dylzqn.xprison.listeners;

import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xcraftapi.api.utils.MSGType;
import me.dylzqn.xprison.cells.xCell;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class InventoryListener implements Listener {

    private xPrisonCore prisonCore;

    public InventoryListener(xPrisonCore prisonCore) {
        this.prisonCore = prisonCore;
    }

    @EventHandler
    public void onInventoryUse(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        xCraftPlayer craftPlayer = prisonCore.getApiCore().getxCraftPlayer(player.getUniqueId());

        for (xCell cell : prisonCore.getCellManager().getCells()) {

            if (cell.getInventory("Confirm").equals(e.getClickedInventory())) {
                Objects.requireNonNull(e.getCurrentItem()).getType();
                if (e.getCurrentItem().getType() == Material.EMERALD_BLOCK) {
                    int money = (int) craftPlayer.getOtherData().get("Money");
                    if (money < cell.getPrice()) {
                        craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "You don't have enough money");
                        return;
                    }
                    cell.setOccupied(true);
                    cell.setOwnerUUID(craftPlayer.getUUID());
                    cell.setEndTime(System.currentTimeMillis() + 432000000);
                    cell.saveData();
                    cell.updateCell();
                    prisonCore.removeMoney(craftPlayer, cell.getPrice());
                    craftPlayer.sendMessage(MSGType.SUCCESS.getPrefix() + "You have successfully purchased cell &f" + cell.getID() + "&a.");
                    player.closeInventory();
                } else if (e.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
                    craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "Transaction declined");
                    player.closeInventory();
                }
                e.setCancelled(true);
            }

            if (cell.getInventory("Main Menu").equals(e.getClickedInventory())) {
                if (e.getCurrentItem().getType() == Material.WATCH) {
                    craftPlayer.getPlayer().openInventory(cell.getInventory("Cell Time"));
                }
                e.setCancelled(true);
            }

            if (cell.getInventory("Cell Time").equals(e.getClickedInventory())) {

                if (Objects.requireNonNull(e.getCurrentItem()).getType() == Material.WATCH) {
                    if (e.getSlot() == 10) {
                        cell.setEndTime(cell.getEndTime() + 86400000);
                        prisonCore.removeMoney(craftPlayer, cell.getPrice() / 5);
                        craftPlayer.sendMessage(MSGType.SUCCESS.getPrefix() + "+ 1 Day");
                    }

                    if (e.getSlot() == 12) {
                        cell.setEndTime(cell.getEndTime() + 172800000);
                        prisonCore.removeMoney(craftPlayer, cell.getPrice() / 5 * 2);
                        craftPlayer.sendMessage(MSGType.SUCCESS.getPrefix() + "+ 2 Days");
                    }

                    if (e.getSlot() == 14) {
                        cell.setEndTime(cell.getEndTime() + 432000000);
                        prisonCore.removeMoney(craftPlayer, cell.getPrice());
                        craftPlayer.sendMessage(MSGType.SUCCESS.getPrefix() + "+ 5 Days");
                    }

                    if (e.getSlot() == 16) {
                        cell.setEndTime(cell.getEndTime() + 604800000);
                        prisonCore.removeMoney(craftPlayer, cell.getPrice() / 5 * 6);
                        craftPlayer.sendMessage(MSGType.SUCCESS.getPrefix() + "+ 7 Days");
                    }

                    cell.saveData();
                    cell.updateCell();
                }
                e.setCancelled(true);
            }

        }

    }
}

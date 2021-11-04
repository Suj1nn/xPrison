package me.dylzqn.xprison.trade;

import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Trade implements Listener {

    private xPrisonCore prisonCore;
    private String id;
    private xCraftPlayer trader;
    private ItemStack[] traderItems;
    private xCraftPlayer recipient;
    private ItemStack[] recipientItems;
    private boolean traderAccepted;
    private boolean recipientAccepted;
    private Inventory inventory;

    public Trade(xPrisonCore prisonCore, String id, xCraftPlayer trader, xCraftPlayer recipient) {
        this.prisonCore = prisonCore;
        this.id = id;
        this.trader = trader;
        this.recipient = recipient;
        this.traderAccepted = false;
        this.recipientAccepted = false;
        this.inventory = prisonCore.getServer().createInventory(null, 54, prisonCore.getApiCore().formatColour("&a&l" + trader.getName() +
                "                " +
                "&c&l" + recipient.getName()));
    }

    public boolean hasTradeFinished(){
        if (traderAccepted && recipientAccepted) {
            return true;
        }
        return false;
    }


    public xPrisonCore getPrisonCore() {
        return prisonCore;
    }

    public String getId() {
        return id;
    }

    public xCraftPlayer getTrader() {
        return trader;
    }

    public ItemStack[] getTraderItems() {
        return traderItems;
    }

    public xCraftPlayer getRecipient() {
        return recipient;
    }

    public ItemStack[] getRecipientItems() {
        return recipientItems;
    }

    public boolean isTraderAccepted() {
        return traderAccepted;
    }

    public boolean isRecipientAccepted() {
        return recipientAccepted;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setTraderItems(ItemStack[] traderItems) {
        this.traderItems = traderItems;
    }

    public void setRecipientItems(ItemStack[] recipientItems) {
        this.recipientItems = recipientItems;
    }

    public void setTraderAccepted(boolean traderAccepted) {
        this.traderAccepted = traderAccepted;
    }

    public void setRecipientAccepted(boolean recipientAccepted) {
        this.recipientAccepted = recipientAccepted;
    }

    @EventHandler
    public void onInventoryOpen(InventoryInteractEvent e) {
        Player player = (Player) e.getWhoClicked();
        xCraftPlayer craftPlayer = prisonCore.getApiCore().getxCraftPlayer(player.getUniqueId());

        if (e.getInventory() == this.inventory) {
            if (craftPlayer == this.trader) {

            }

            if (craftPlayer == this.recipient) {

            }
        }
    }
}

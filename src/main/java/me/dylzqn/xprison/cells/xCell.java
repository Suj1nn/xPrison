package me.dylzqn.xprison.cells;

import me.dylzqn.xcraftapi.api.locations.Region;
import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xprison.cells.inventories.CellTime;
import me.dylzqn.xprison.cells.inventories.MainMenu;
import me.dylzqn.xprison.inventory.InventoryBuilder;
import me.dylzqn.xprison.cells.inventories.Confirm;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
public class xCell {

    private xPrisonCore prisonCore;
    private String ID;
    private Region region;
    private int price;
    private long endTime;
    private boolean isOccupied;
    private UUID ownerUUID;
    private Location signLocation;
    private List<InventoryBuilder> inventories;
    private List<xCraftPlayer> craftPlayers;
    private HashMap<String, Object> dbData;

    public xCell(xPrisonCore prisonCore, String ID, Region region, int price, Location signLocation, UUID ownerUUID) {
        this.prisonCore = prisonCore;
        this.ID = ID;
        this.region = region;
        if (ownerUUID != null) {
            this.isOccupied = true;
            this.ownerUUID = ownerUUID;
        } else {
            this.isOccupied = false;
            this.ownerUUID = null;
        }
        this.price = price;
        this.signLocation = signLocation;
        this.craftPlayers = new ArrayList<>();
        this.dbData = new HashMap<>();

        this.inventories = new ArrayList<>();
        this.inventories.add(new Confirm(prisonCore, this));
        this.inventories.add(new MainMenu(prisonCore, this));
        this.inventories.add(new CellTime(prisonCore, this));

        if (!prisonCore.getApiCore().getxCraftCore().getMysqlDatabase().exists("xPrisonCells", "CellID", ID)) {
            dbData.put("CellID", ID);
            dbData.put("Price", price);
            dbData.put("SignLocation", prisonCore.deserialiseLoc(signLocation));
            dbData.put("Loc1", prisonCore.deserialiseLoc(region.getSelection().getLoc1()));
            dbData.put("Loc2", prisonCore.deserialiseLoc(region.getSelection().getLoc2()));
            if (ownerUUID != null) {
                dbData.put("Occupied", 1);
                dbData.put("UUID", ownerUUID.toString());
                dbData.put("EndTime", System.currentTimeMillis() + 604800);
            } else {
                dbData.put("Occupied", 0);
                dbData.put("EndTime", 0);
            }
            prisonCore.getApiCore().getxCraftCore().getMysqlDatabase().set("xPrisonCells", dbData, "CellID", ID);
        }
    }

    public void saveData() {
        if (ownerUUID != null) {
            dbData.put("Occupied", 1);
            dbData.put("UUID", ownerUUID.toString());
            dbData.put("EndTime", endTime);
        } else {
            dbData.put("UUID", ownerUUID.toString());
            dbData.put("Occupied", 0);
            dbData.put("EndTime", 0);
        }
        prisonCore.getApiCore().getxCraftCore().getMysqlDatabase().set("xPrisonCells", dbData, "CellID", ID);

    }

    public void updateCell() {

        if (System.currentTimeMillis() >= this.endTime) {
            this.ownerUUID = null;
            this.isOccupied = false;
            this.endTime = 0;
        }

        Sign sign = (Sign) signLocation.getBlock().getState();

        if (!this.isOccupied()) {
            sign.setLine(0, ChatColor.translateAlternateColorCodes('&', "&6&lxCELL"));
            sign.setLine(1, ChatColor.translateAlternateColorCodes('&', "&b" + this.getID()));
            sign.setLine(2, ChatColor.translateAlternateColorCodes('&', "&b$" + this.getPrice()));
            sign.setLine(3, ChatColor.translateAlternateColorCodes('&', "&7" + "Click to Buy"));
        } else {
            sign.setLine(0, ChatColor.translateAlternateColorCodes('&', "&6&lxCELL"));
            sign.setLine(1, ChatColor.translateAlternateColorCodes('&', "&b" + this.ID));
            sign.setLine(2, ChatColor.translateAlternateColorCodes('&', "&b" + prisonCore.getServer().getOfflinePlayer(this.ownerUUID).getName()));
            sign.setLine(3, ChatColor.translateAlternateColorCodes('&', "&7" + getFormattedTimeRemaining()));
        }
        sign.update();

    }
    public String getFormattedTimeRemaining() {
        long seconds = (endTime - System.currentTimeMillis()) / 1000;
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

        return day + "d " + hours + "h " + minute + "m";
    }

    public Inventory getInventory(String name) {
        for (InventoryBuilder inventory : inventories) {
            if (inventory.getName().equalsIgnoreCase(name)) {
                return inventory.getInventory();
            }
        }
        return null;
    }

    public InventoryBuilder getInventoryBuilder(String name) {
        for (InventoryBuilder inventory : inventories) {
            if (inventory.getName().equalsIgnoreCase(name)) {
                return inventory;
            }
        }
        return null;
    }

    public String getID() {
        return ID;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public Region getRegion() {
        return region;
    }

    public int getPrice() {
        return price;
    }

    public long getEndTime() {
        return endTime;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public Location getSignLocation() {
        return signLocation;
    }

    public xCraftPlayer getCraftPlayer() {
        return prisonCore.getApiCore().getxCraftPlayer(ownerUUID);
    }

    public List<xCraftPlayer> getCraftPlayers() {
        return craftPlayers;
    }

    public List<InventoryBuilder> getInventories() {
        return inventories;
    }

    public void addTime(long time) {
        setEndTime(Long.sum(endTime, time));
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }
}

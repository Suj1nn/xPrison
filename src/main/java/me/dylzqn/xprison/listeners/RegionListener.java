package me.dylzqn.xprison.listeners;

import me.dylzqn.xcraftapi.api.locations.Region;
import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xcraftapi.api.utils.MSGType;
import me.dylzqn.xprison.WardType;
import me.dylzqn.xprison.cells.xCell;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

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
public class RegionListener implements Listener {

    private xPrisonCore prisonCore;

    public RegionListener(xPrisonCore prisonCore) {
        this.prisonCore = prisonCore;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {

        if (e.getDamager() instanceof Player) {
            Player damager = (Player) e.getDamager();
            if (e.getEntity() instanceof Player) {
                Player player = (Player) e.getEntity();
                xCraftPlayer craftPlayer = prisonCore.getApiCore().getxCraftPlayer(damager.getUniqueId());
                Region region = prisonCore.getApiCore().getRegionManager().getRegion(damager.getLocation());
                if (region != null) {
                    boolean canPVP = (boolean) region.getRegionData().get("canPVP");
                    if (region.getRegionData().get("canPVP") == null || !canPVP) {
                        craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "You are unable to PVP here");
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Region region = prisonCore.getApiCore().getRegionManager().getRegion(e.getBlock().getLocation());
        Player player = e.getPlayer();
        xCraftPlayer craftPlayer = prisonCore.getApiCore().getxCraftPlayer(player.getUniqueId());
        if (region != null) {
            if (region.getRegionData().get("canBreak") == null) {
                if (!craftPlayer.canEdit()) {
                    craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "You are unable to break here");
                    e.setCancelled(true);
                }
            }
            boolean canBreak = (boolean) region.getRegionData().get("canBreak");
            if (!canBreak){
                if (!craftPlayer.canEdit()) {
                    craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "You are unable to break here");
                    e.setCancelled(true);
                }
            }
        } else {
            if (!craftPlayer.canEdit()) {
                craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "You are unable to break here");
                e.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Region region = prisonCore.getApiCore().getRegionManager().getRegion(e.getBlockPlaced().getLocation());
        Player player = e.getPlayer();
        xCraftPlayer craftPlayer = prisonCore.getApiCore().getxCraftPlayer(player.getUniqueId());
        if (region != null) {
            if (region.getRegionData().get("canPlace") == null) {
                if (!craftPlayer.canEdit()) {
                    craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "You are unable to place here");
                    e.setCancelled(true);
                }
            }
            boolean canPlace = (boolean) region.getRegionData().get("canPlace");
            if (!canPlace) {
                if (!craftPlayer.canEdit()) {
                    craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "You are unable to place here");
                    e.setCancelled(true);
                }
            }
        } else {
            if (!craftPlayer.canEdit()) {
                craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "You are unable to place here");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && Objects.requireNonNull(e.getClickedBlock()).getType().equals(Material.LEVER)) {
            xCraftPlayer craftPlayer = prisonCore.getApiCore().getxCraftPlayer(e.getPlayer().getUniqueId());
            xCell cell = prisonCore.getCellManager().getCell(Objects.requireNonNull(e.getClickedBlock()).getLocation());
            if (cell != null) {
                if (!craftPlayer.getUUID().equals(cell.getOwnerUUID()) && cell.getOwnerUUID() != null) {
                    e.setCancelled(true);
                    craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "This cell does not belong to you");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Region region = prisonCore.getApiCore().getRegionManager().getRegion(e.getTo());
        Player player = e.getPlayer();
        xCraftPlayer craftPlayer = prisonCore.getApiCore().getxCraftPlayer(player.getUniqueId());
        WardType wardType = (WardType) craftPlayer.getOtherData().get("Ward");
        if (region != null) {
            if (region.getName().equals("CENT")) {
                if (wardType.getId() > 9) {
                    e.setCancelled(true);
                    craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "You are not allowed in here!");
                    for (Location location : region.getGetBlockLocations()) {
                        craftPlayer.getPlayer().sendBlockChange(location, Material.BARRIER, (byte) 2);

                        prisonCore.getServer().getScheduler().scheduleSyncDelayedTask(prisonCore, new BukkitRunnable() {
                            @Override
                            public void run() {
                                craftPlayer.getPlayer().sendBlockChange(location, Material.AIR, (byte) 1);
                            }
                        }, 60);
                    }
                }
            }
        }
    }

//    @EventHandler
//    public void onPlayerOpenInventory(InventoryOpenEvent e) {
//
//        Region region = prisonCore.getApiCore().getRegionManager().getRegion(e.getInventory().get);
//        if (region != null) {
//            xCraftPlayer craftPlayer = prisonCore.getApiCore().getxCraftPlayer(e.getPlayer().getUniqueId());
//            xCell cell = prisonCore.getCellManager().getCell(Objects.requireNonNull(e.getInventory()).getLocation());
//            if (cell != null) {
//                if (!craftPlayer.getUUID().equals(cell.getOwnerUUID()) && cell.getOwnerUUID() != null) {
//                    e.setCancelled(true);
//                    craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "This cell does not belong to you");
//                }
//            }
//        }
//
//    }
}

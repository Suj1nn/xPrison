package me.dylzqn.xprison.commands;

import me.dylzqn.xcraftapi.api.player.Rank;
import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xcraftapi.api.utils.MSGType;
import me.dylzqn.xprison.WardType;
import me.dylzqn.xprison.Warp;
import me.dylzqn.xprison.commands.xcommand.xCommand;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
public class WarpCommand extends xCommand {

    public WarpCommand(xPrisonCore prisonCore) {
        super(prisonCore);
        setDescription("Warp Command");
        setName("Warp");
        setSyntax("/warp <Name>");
        setRequiredWard(WardType.X);
        setRequiredRank(Rank.MEMBER);
        getLabels().add("warp");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            xCraftPlayer craftPlayer = getPrisonCore().getApiCore().getxCraftPlayer(player.getUniqueId());

            WardType playerWardType = (WardType) craftPlayer.getOtherData().get("Ward");

            if (craftPlayer.getRank().getID() > Rank.ADMIN.getID()) {

                if (args.length == 0) {
                    craftPlayer.sendMessage(MSGType.INFO.getPrefix() + "List of Warps:");
                    for (Warp warp : getPrisonCore().getPrisonGame().getWarps()) {
                        if (craftPlayer.getRank().getID() <= warp.getRequiredRank().getID()) {
                            if (playerWardType.getId() <= warp.getRequiredWard().getId()) {
                                craftPlayer.sendMessage("&3&l> &f" + warp.getName());
                            }
                        }
                    }
                    return true;
                }

                Warp warp = getPrisonCore().getPrisonGame().getWarp(args[0]);

                if (warp == null) {
                    craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "Could not find warp.");
                    return true;
                }

                if (craftPlayer.getRank().getID() > warp.getRequiredRank().getID()) {
                    craftPlayer.sendNoPermMessage();
                    return true;
                }

                if (playerWardType.getId() > warp.getRequiredWard().getId()) {
                    craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "You are not ranked high enough.");
                    return true;
                }

                craftPlayer.sendMessage(MSGType.INFO.getPrefix() + "Teleporting you to &r" + warp.getName() + "&b...");
                craftPlayer.getOtherData().put("isTeleporting", true);
                craftPlayer.getOtherData().put("teleportingTime", 5);
                craftPlayer.getOtherData().put("teleportLoc", warp.getLocation());
                return true;
            }

            if (args.length == 0) {
                craftPlayer.sendMessage(MSGType.INFO.getPrefix() + "List of Warps:");
                for (Warp warp : getPrisonCore().getPrisonGame().getWarps()) {
                    if (craftPlayer.getRank().getID() < warp.getRequiredRank().getID()) {
                        if (playerWardType.getId() <= warp.getRequiredWard().getId()) {
                            craftPlayer.sendMessage("&3&l> &f" + warp.getName());
                        }
                    }
                }
                craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "Usage: /warp <Name>");
                craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "Usage: /warp <Set|Remove> <Name> [Rank] [Ward]");
                return true;
            }

            switch (args[0].toLowerCase()) {

                case "set":

                    if (args.length < 2) {
                        craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "Usage: /warp <Set> <Name> [Rank] [Ward]");
                        return true;
                    }

                    if (args.length == 4) {

                        Rank rank;

                        try {
                            rank = Rank.valueOf(args[2].toUpperCase());
                        }catch (IllegalArgumentException e) {
                            craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "Could not find rank.");
                            return true;
                        }

                        WardType wardType;
                        try {
                            wardType = WardType.valueOf(args[3].toUpperCase());
                        }catch (IllegalArgumentException e) {
                            craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "Could not find ward.");
                            return true;
                        }

                        getPrisonCore().getPrisonGame().createWarp(args[1], rank, wardType, craftPlayer.getPlayer().getLocation());
                        craftPlayer.sendMessage(MSGType.SUCCESS.getPrefix() + "Warp &f" + args[1] + " &ahas been created.");
                        return true;
                    }

                    if (args.length == 3) {

                        Rank rank;

                        try {
                            rank = Rank.valueOf(args[2].toUpperCase());
                        }catch (IllegalArgumentException e) {
                            craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "Could not find rank.");
                            return true;
                        }

                        getPrisonCore().getPrisonGame().createWarp(args[1], rank, WardType.X, craftPlayer.getPlayer().getLocation());
                        craftPlayer.sendMessage(MSGType.SUCCESS.getPrefix() + "Warp &f" + args[1] + " &ahas been created.");
                        return true;
                    }

                    getPrisonCore().getPrisonGame().createWarp(args[1], Rank.MEMBER, WardType.X, craftPlayer.getPlayer().getLocation());
                    craftPlayer.sendMessage(MSGType.SUCCESS.getPrefix() + "Warp &f" + args[1] + " &ahas been created.");
                    break;

                case "remove":

                    if (args.length < 2) {
                        craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "Usage: /warp <Remove> <Name>");
                        return true;
                    }

                    if (!getPrisonCore().getPrisonGame().doesWarpExist(args[1])) {
                        craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "This warp does not exist.");
                        return true;
                    }

                    getPrisonCore().getPrisonGame().removeWarp(args[1]);
                    craftPlayer.sendMessage(MSGType.SUCCESS.getPrefix() + "Warp " + args[1] + " has been removed");
                    break;

                default:
                    Warp warp = getPrisonCore().getPrisonGame().getWarp(args[0]);

                    if (warp == null) {
                        craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "Could not find warp.");
                        return true;
                    }

                    //FOUNDER MEMBER
                    //0 10

                    if (craftPlayer.getRank().getID() > warp.getRequiredRank().getID()) {
                        craftPlayer.sendNoPermMessage();
                        return true;
                    }

                    //C R
                    //9 8

                    if (playerWardType.getId() > warp.getRequiredWard().getId()) {
                        craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "You are not ranked high enough.");
                        return true;
                    }

                    craftPlayer.sendMessage(MSGType.INFO.getPrefix() + "Teleporting you to &r" + warp.getName() + "&b...");
                    craftPlayer.getOtherData().put("isTeleporting", true);
                    craftPlayer.getOtherData().put("teleportingTime", 5);
                    craftPlayer.getOtherData().put("teleportLoc", warp.getLocation());
                    break;
            }

        }

        return true;
    }
}

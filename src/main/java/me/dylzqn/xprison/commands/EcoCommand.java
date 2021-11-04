package me.dylzqn.xprison.commands;

import me.dylzqn.xcraftapi.api.player.Rank;
import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xcraftapi.api.utils.MSGType;
import me.dylzqn.xcraftapi.api.utils.mojang.UUIDManager;
import me.dylzqn.xprison.WardType;
import me.dylzqn.xprison.commands.xcommand.xCommand;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class EcoCommand extends xCommand {

    public EcoCommand(xPrisonCore prisonCore) {
        super(prisonCore);
        setName("Eco");
        getLabels().add("eco");
        getLabels().add("economy");
        setRequiredRank(Rank.DEVELOPER);
        setDescription("Economy Command");
        setRequiredWard(WardType.X);
        setSyntax("/eco");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {

        if (args.length < 3) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MSGType.ERROR.getPrefix() + "Usage: /eco <add|remove|set> <Player> <Amount>"));
            return true;
        }

        UUID uuid = null;
        try {
            uuid = UUIDManager.getUUIDOf(args[1]);
        } catch (Exception ignored) {
        }

        int amount = 0;

        try {
            amount = Integer.parseInt(args[2]);
        }catch (Exception e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MSGType.ERROR.getPrefix() + "Invalid number"));
            return true;
        }

        if (uuid == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MSGType.ERROR.getPrefix() + "Could not find player. Please check your spelling"));
            return true;
        }

        if (!getPrisonCore().getApiCore().hasPlayedBefore(uuid)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MSGType.ERROR.getPrefix() + "This player has not played before"));
            return true;
        }

        xCraftPlayer craftPlayer = getPrisonCore().getApiCore().getxCraftPlayer(uuid);
        int currentMoney = (int) craftPlayer.getOtherData().get("Money");

        switch (args[0].toLowerCase()) {

            case "add":
                craftPlayer.getOtherData().put("Money", currentMoney + amount);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MSGType.SUCCESS.getPrefix() + "You have added &r$" + amount + " &ato &r" + craftPlayer.getName() + "&a's bank"));
                break;
            case "remove":
                craftPlayer.getOtherData().put("Money", currentMoney - amount);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MSGType.SUCCESS.getPrefix() + "You have removed &r$" + amount + " &afrom &r" + craftPlayer.getName() + "&a's bank"));
                break;
            case "set":
                craftPlayer.getOtherData().put("Money", amount);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MSGType.SUCCESS.getPrefix() + "You have set &r$" + amount + " &ato &r" + craftPlayer.getName() + "&a's bank"));
                break;
            default:
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MSGType.ERROR.getPrefix() + "Usage: /eco <add|remove|set> <Player> <Amount>"));
                break;
        }

        return true;
    }
}

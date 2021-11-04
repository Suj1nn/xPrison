package me.dylzqn.xprison.commands;

import me.dylzqn.xcraftapi.api.player.Rank;
import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xcraftapi.api.utils.MSGType;
import me.dylzqn.xprison.WardType;
import me.dylzqn.xprison.commands.xcommand.xCommand;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand extends xCommand {


    public HelpCommand(xPrisonCore prisonCore) {
        super(prisonCore);
        setName("Help");
        setDescription("Returns this menu");
        setRequiredRank(Rank.MEMBER);
        setRequiredWard(WardType.X);
        setSyntax("/help");
        getLabels().add("help");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            xCraftPlayer craftPlayer = getPrisonCore().getApiCore().getxCraftPlayer(player.getUniqueId());

            WardType playerWardType = (WardType) craftPlayer.getOtherData().get("Ward");

            craftPlayer.sendMessage(MSGType.OTHER.getPrefix() + "Help Menu");
            for (xCommand command : getPrisonCore().getCommandManager().getCommands()) {
                if (craftPlayer.getRank().getID() <= command.getRequiredRank().getID() && playerWardType.getId() >= command.getRequiredWard().getId()) {
                    craftPlayer.sendClickableMessage("&3&l> &b" + command.getName(), null, "&4USAGE: " + command.getSyntax() + "\n" + "&6" + command.getDescription());
                }
            }
            return true;
        }


        sender.sendMessage(MSGType.INFO.getPrefix() + "All Commands");

        for (xCommand command : getPrisonCore().getCommandManager().getCommands()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l> &b" + command.getSyntax()));
        }

        return true;
    }

}

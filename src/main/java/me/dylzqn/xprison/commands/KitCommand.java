package me.dylzqn.xprison.commands;

import me.dylzqn.xcraftapi.api.game.xKit;
import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xcraftapi.api.utils.MSGType;
import me.dylzqn.xprison.commands.xcommand.xCommand;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand extends xCommand {


    public KitCommand(xPrisonCore prisonCore) {
        super(prisonCore);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {

        if (sender instanceof Player) {

            Player player = (Player) sender;
            xCraftPlayer craftPlayer = getPrisonCore().getApiCore().getxCraftPlayer(player.getUniqueId());

            if (args.length == 0) {

                craftPlayer.sendMessage(MSGType.OTHER.getPrefix() + "Available Kits:");
                for (xKit kit : getPrisonCore().getPrisonGame().getKits()) {
                    if (craftPlayer.getRank().getID() <= kit.getRequiredRank().getID()) {

                    }
                }
                craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "Usage: /kit <Name>");
            }
        } else {
            sender.sendMessage(MSGType.ERROR.getPrefix() + "You must be a player to execute");
        }

        return true;
    }

}

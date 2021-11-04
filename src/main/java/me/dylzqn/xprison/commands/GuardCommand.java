package me.dylzqn.xprison.commands;

import me.dylzqn.xcraftapi.api.player.Rank;
import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuardCommand implements CommandExecutor {

    private xPrisonCore prisonCore;

    public GuardCommand(xPrisonCore prisonCore) {
        this.prisonCore = prisonCore;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            xCraftPlayer craftPlayer = prisonCore.getApiCore().getxCraftPlayer(player.getUniqueId());

            if (craftPlayer.getRank().getID() > Rank.DEVELOPER.getID()) {
                boolean canBeGuard = (boolean) craftPlayer.getOtherData().get("CanBeGuard");
                if (canBeGuard) {
                    craftPlayer.getOtherData().put("GuardInv", craftPlayer.getPlayer().getInventory());
                    craftPlayer.getPlayer().getInventory().clear();
                    //add items;
                } else {
                    craftPlayer.sendNoPermMessage();
                    return true;
                }
            }
        }

        return true;
    }
}

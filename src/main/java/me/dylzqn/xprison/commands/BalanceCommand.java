package me.dylzqn.xprison.commands;

import me.dylzqn.xcraftapi.api.player.Rank;
import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xcraftapi.api.utils.MSGType;
import me.dylzqn.xprison.WardType;
import me.dylzqn.xprison.commands.xcommand.xCommand;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand extends xCommand {

    public BalanceCommand(xPrisonCore prisonCore) {
        super(prisonCore);
        setDescription("Shows your balance");
        setName("Balance");
        setRequiredRank(Rank.MEMBER);
        setRequiredWard(WardType.X);
        setSyntax("/balance");
        getLabels().add("balance");
        setSyntax("/balance");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            xCraftPlayer craftPlayer = getPrisonCore().getApiCore().getxCraftPlayer(player.getUniqueId());

            craftPlayer.sendMessage(MSGType.INFO.getPrefix() + "Your balance is: &r$" + craftPlayer.getOtherData().get("Money"));
        }
        return true;
    }

}

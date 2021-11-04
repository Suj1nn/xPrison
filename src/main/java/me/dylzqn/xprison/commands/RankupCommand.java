package me.dylzqn.xprison.commands;

import me.dylzqn.xcraftapi.api.player.Rank;
import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xcraftapi.api.utils.MSGType;
import me.dylzqn.xprison.WardType;
import me.dylzqn.xprison.commands.xcommand.xCommand;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankupCommand extends xCommand {

    public RankupCommand(xPrisonCore prisonCore) {
        super(prisonCore);
        setName("Rankup");
        setDescription("Rankup Command");
        setRequiredRank(Rank.MEMBER);
        setRequiredWard(WardType.X);
        setSyntax("/rankup");
        getLabels().add("rankup");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {

        if (sender instanceof Player){
            Player player = (Player) sender;
            xCraftPlayer craftPlayer = getPrisonCore().getApiCore().getxCraftPlayer(player.getUniqueId());
            WardType playerWardType = (WardType) craftPlayer.getOtherData().get("Ward");
            int playerMoney = (int) craftPlayer.getOtherData().get("Money");

            for (WardType wardType : WardType.values()) {
                if (playerWardType.getId() - 1 == wardType.getId()) {
                    if (playerMoney >= wardType.getAmount()) {
                        craftPlayer.getOtherData().put("Ward", wardType);
                        craftPlayer.getOtherData().put("Money", playerMoney - wardType.getAmount());
                        craftPlayer.sendMessage(MSGType.SUCCESS.getPrefix() + "You have ranked up!");
                        getPrisonCore().getServer().broadcastMessage(MSGType.INFO.getPrefix() + craftPlayer.getName() + " has ranked up to ward " + wardType.getPrefix());
                    } else {
                        craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "You do not have enough money. You require &f$" + wardType.getAmount() + "&c.");
                    }
                }
            }
        }

        return true;
    }
}

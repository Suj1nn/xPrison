package me.dylzqn.xprison.commands;

import me.dylzqn.xcraftapi.api.player.Rank;
import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xcraftapi.api.utils.MSGType;
import me.dylzqn.xprison.WardType;
import me.dylzqn.xprison.commands.xcommand.xCommand;
import me.dylzqn.xprison.trade.Trade;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TradeCommand extends xCommand {

    public TradeCommand(xPrisonCore prisonCore) {
        super(prisonCore);
        setName("Trade");
        setDescription("Trade with other players");
        setSyntax("/trade <Name>");
        setRequiredWard(WardType.X);
        setRequiredRank(Rank.MEMBER);
        getLabels().add("trade");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            xCraftPlayer craftPlayer = getPrisonCore().getApiCore().getxCraftPlayer(player.getUniqueId());


            if (args.length == 0) {
                craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "Usage: " + getSyntax());
                return true;
            }

            if (args[0].equalsIgnoreCase("accept")) {

                boolean flag = false;

                for (Trade trade : getPrisonCore().getPrisonGame().getActiveTrades()) {
                    if (trade.getRecipient() == craftPlayer && !trade.hasTradeFinished()) {
                        if (trade.getRecipient().getPlayer() != null) {
                            flag = true;
                            trade.getTrader().getPlayer().openInventory(trade.getInventory());
                            trade.getRecipient().getPlayer().openInventory(trade.getInventory());
                            getPrisonCore().getPrisonGame().getActiveTrades().remove(trade);
                        }
                    }
                }

                if (!flag) {
                    craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "You do not have a trade request.");
                }
                return true;
            }


            Player recipient = getPrisonCore().getServer().getPlayer(args[0]);

            if (recipient == null) {
                craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "Could not find player. Are you sure they are online?");
                return true;
            }

            if (recipient == craftPlayer.getPlayer()) {
                craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "You can't send a request to yourself!");
                return true;
            }

            xCraftPlayer craftRecipient = getPrisonCore().getApiCore().getxCraftPlayer(recipient.getUniqueId());

            if (getDistance(craftPlayer.getPlayer().getLocation(), craftRecipient.getPlayer().getLocation()) > 5) {
                craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "You must be within 10 blocks to trade with this player.");
                return true;
            }

            Trade trade = getPrisonCore().getPrisonGame().createTrade(craftPlayer, craftRecipient);
            craftPlayer.sendMessage(MSGType.SUCCESS.getPrefix() + "Trade invitation sent to &f" + craftRecipient.getName());

            craftRecipient.sendMessage(MSGType.INFO.getPrefix() + "You have a trade request from &f" + craftPlayer.getName());

            getPrisonCore().getServer().getScheduler().scheduleSyncDelayedTask(getPrisonCore(), new BukkitRunnable() {
                @Override
                public void run() {
                    if (!trade.hasTradeFinished()){
                        getPrisonCore().getPrisonGame().getActiveTrades().remove(trade);
                        craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "Your trade request with &f" + craftRecipient.getName() + " &chas expired.");
                        craftRecipient.sendMessage(MSGType.ERROR.getPrefix() + "Trade request from &f" + craftRecipient.getName() + " &chas expired.");
                    }
                }
            }, 1200);


        } else {
            sender.sendMessage(MSGType.ERROR.getPrefix() + "This command can only be used as a player!");
        }
        return true;
    }

    private double getDistance(Location loc1, Location loc2) {
        return Math.sqrt((loc2.getY() - loc1.getY()) * (loc2.getY() - loc1.getY()) + (loc2.getX() - loc1.getX()) * (loc2.getX() - loc1.getX()));
    }
}

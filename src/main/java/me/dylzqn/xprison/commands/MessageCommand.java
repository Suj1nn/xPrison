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

public class MessageCommand extends xCommand {

    public MessageCommand(xPrisonCore prisonCore) {
        super(prisonCore);
        setName("Message");
        getLabels().add("message");
        getLabels().add("msg");
        getLabels().add("tell");
        setDescription("Message Command");
        setRequiredRank(Rank.MEMBER);
        setRequiredWard(WardType.X);
        setSyntax("/message <Player> <Message>");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {

        if (args.length < 2) {
            sender.sendMessage(MSGType.ERROR.getPrefix() + "Usage: /message <Player> <Message>");
            return true;
        }

        Player receiver = getPrisonCore().getServer().getPlayer(args[0]);

        if (receiver == null) {
            sender.sendMessage(MSGType.ERROR.getPrefix() + "Could not find player. Are they online?");
            return true;
        }

        xCraftPlayer craftPlayerReceiver = getPrisonCore().getApiCore().getxCraftPlayer(receiver.getUniqueId());

        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(MSGType.SUCCESS.getPrefix() + ChatColor.translateAlternateColorCodes('&',"You to " + craftPlayerReceiver.getRank().getSuffix() + craftPlayerReceiver.getName() + "&a: &7" + builder));
            craftPlayerReceiver.sendMessage(MSGType.SUCCESS.getPrefix() + "&4CONSOLE" + ChatColor.translateAlternateColorCodes('&', " &ato you: &7" + builder));
            return true;
        }
        Player player = (Player) sender;
        xCraftPlayer craftPlayer = getPrisonCore().getApiCore().getxCraftPlayer(player.getUniqueId());
        craftPlayer.sendMessage(MSGType.SUCCESS.getPrefix() + ChatColor.translateAlternateColorCodes('&',"You to " + craftPlayerReceiver.getRank().getSuffix() + craftPlayer.getName() + "&a: &7" + builder));
        craftPlayerReceiver.sendMessage(MSGType.SUCCESS.getPrefix() + craftPlayer.getRank().getSuffix() + craftPlayer.getName() + ChatColor.translateAlternateColorCodes('&', " &ato you: &7" + builder));
        return true;
    }

}


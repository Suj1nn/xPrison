package me.dylzqn.xprison.commands;

import me.dylzqn.xcraftapi.api.player.Rank;
import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xcraftapi.api.utils.MSGType;
import me.dylzqn.xprison.WardType;
import me.dylzqn.xprison.commands.xcommand.xCommand;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends xCommand {

    public SpawnCommand(xPrisonCore prisonCore) {
        super(prisonCore);
        setDescription("Teleporting to Spawn");
        setName("Spawn");
        setRequiredRank(Rank.MEMBER);
        setRequiredWard(WardType.X);
        setSyntax("/spawn");
        getLabels().add("spawn");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {

        if (sender instanceof Player){
            Player player = (Player) sender;
            xCraftPlayer craftPlayer = getPrisonCore().getApiCore().getxCraftPlayer(player.getUniqueId());

//            craftPlayer.getOtherData().put("isTeleporting", true);
//            craftPlayer.getOtherData().put("teleportingTime", 5);
//            craftPlayer.getOtherData().put("teleportLoc", getPrisonCore().getPrisonGame().getLobbyLocation());
//            craftPlayer.sendMessage(MSGType.OTHER.getPrefix() + "Teleporting you to spawn...");

            craftPlayer.getPlayer().performCommand("warp spawn");

        } else {
            sender.sendMessage(MSGType.ERROR.getPrefix() + "This command can only be used as a player!");
        }
        return true;
    }
}

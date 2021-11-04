package me.dylzqn.xprison.listeners;

import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xcraftapi.api.utils.MSGType;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private xPrisonCore prisonCore;

    public PlayerMoveListener(xPrisonCore prisonCore) {
        this.prisonCore = prisonCore;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        Player player = e.getPlayer();
        xCraftPlayer craftPlayer = prisonCore.getApiCore().getxCraftPlayer(player.getUniqueId());

        if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getY() != e.getTo().getY()) {
            if (craftPlayer.getOtherData().get("isTeleporting").equals(true)) {
                craftPlayer.getOtherData().put("isTeleporting", false);
                craftPlayer.getOtherData().put("teleportingTime", -1);
                craftPlayer.sendMessage(MSGType.ERROR.getPrefix() + "You have moved. Your teleport has been cancelled.");
            }
        }

    }
}

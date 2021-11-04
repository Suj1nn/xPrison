package me.dylzqn.xprison.listeners;

import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandProcessListener implements Listener {

    private xPrisonCore prisonCore;

    public CommandProcessListener(xPrisonCore prisonCore) {
        this.prisonCore = prisonCore;
    }

    @EventHandler
    public void onCommandProcess(PlayerCommandPreprocessEvent e) {

        String command = e.getMessage();

        if (e.isCancelled()) {
            xCraftPlayer craftPlayer = prisonCore.getApiCore().getxCraftPlayer(e.getPlayer().getUniqueId());
            craftPlayer.sendNoPermMessage();
        }

    }
}

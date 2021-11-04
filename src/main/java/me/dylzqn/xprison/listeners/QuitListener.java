package me.dylzqn.xprison.listeners;

import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private xPrisonCore prisonCore;

    public QuitListener(xPrisonCore prisonCore) {
        this.prisonCore = prisonCore;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        Player player = e.getPlayer();
        xCraftPlayer craftPlayer = prisonCore.getApiCore().getxCraftPlayer(player.getUniqueId());

        prisonCore.savePrisonPlayer(craftPlayer);

        prisonCore.getPrisonGame().removexCraftPlayer(craftPlayer);
        prisonCore.getPrisonGame().getTeam("Prisoners").getxCraftPlayers().remove(craftPlayer);

        e.setQuitMessage(prisonCore.getApiCore().formatColour("&8[&4&l-&8] &7" + craftPlayer.getName() + " &chas left."));
    }
}

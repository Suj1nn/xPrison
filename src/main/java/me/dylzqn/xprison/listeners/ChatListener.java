package me.dylzqn.xprison.listeners;

import me.dylzqn.xcraftapi.api.player.Rank;
import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xprison.WardType;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private xPrisonCore prisonCore;

    public ChatListener(xPrisonCore prisonCore) {
        this.prisonCore = prisonCore;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        xCraftPlayer craftPlayer = prisonCore.getApiCore().getxCraftPlayer(player.getUniqueId());

        WardType wardType = (WardType) craftPlayer.getOtherData().get("Ward");

        String format = "";

        if (craftPlayer.getRank() != Rank.MEMBER) {
            format = ChatColor.translateAlternateColorCodes('&', wardType.getPrefix() + " &r" + craftPlayer.getRank().getPrefix() + " &8" + craftPlayer.getName() + "&7: &r" + e.getMessage());
        } else {
            format = ChatColor.translateAlternateColorCodes('&', wardType.getPrefix() + " &8" + craftPlayer.getName() + "&7: " + e.getMessage());
        }

        e.setFormat(format);
    }
}

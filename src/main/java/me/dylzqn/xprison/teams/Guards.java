package me.dylzqn.xprison.teams;

import me.dylzqn.xcraftapi.api.game.xTeam;
import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.List;

public class Guards implements xTeam {

    private xPrisonCore prisonCore;
    private List<xCraftPlayer> xCraftPlayers;

    public Guards(xPrisonCore prisonCore) {
        this.prisonCore = prisonCore;
        this.xCraftPlayers = new ArrayList<>();
    }

    @Override
    public String getName() {
        return "Guards";
    }

    @Override
    public Color getColour() {
        return Color.WHITE;
    }

    @Override
    public String getPrefix() {
        return ChatColor.WHITE + "";
    }

    @Override
    public String getSuffix() {
        return "";
    }

    @Override
    public List<xCraftPlayer> getxCraftPlayers() {
        return xCraftPlayers;
    }

    @Override
    public boolean canDamageTeam() {
        return false;
    }
}

package me.dylzqn.xprison.teams;

import me.dylzqn.xcraftapi.api.game.xTeam;
import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.List;

public class Prisoners implements xTeam {

    private xPrisonCore prisonCore;
    private List<xCraftPlayer> xCraftPlayers;

    public Prisoners(xPrisonCore prisonCore) {
        this.prisonCore = prisonCore;
        this.xCraftPlayers = new ArrayList<>();
    }

    @Override
    public String getName() {
        return "Prisoners";
    }

    @Override
    public Color getColour() {
        return Color.GRAY;
    }

    @Override
    public String getPrefix() {
        return ChatColor.GRAY + "";
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
        return true;
    }
}

package me.dylzqn.xprison;

import me.dylzqn.xcraftapi.api.game.xGame;
import me.dylzqn.xcraftapi.api.player.Rank;
import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xprison.gamestates.InGame;
import me.dylzqn.xprison.kits.DiamondKit;
import me.dylzqn.xprison.kits.GoldKit;
import me.dylzqn.xprison.kits.IronKit;
import me.dylzqn.xprison.kits.StarterKit;
import me.dylzqn.xprison.teams.Guards;
import me.dylzqn.xprison.teams.Prisoners;
import me.dylzqn.xprison.trade.Trade;
import me.dylzqn.xprison.ward.Ward;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class xPrisonGame extends xGame {

    private final xPrisonCore prisonCore;
    private final List<Warp> warps;
    private final List<Trade> activeTrades;
    private final List<Ward> wards;

    public xPrisonGame(xPrisonCore prisonCore) {
        super(prisonCore.getApiCore(), "Prison", 0, -1, new Location(Bukkit.getWorld("Prison"),
                219.5,
                65,
                -226.5,
                90,
                0
        ));

        this.prisonCore = prisonCore;

        addGamestate(new InGame(prisonCore));

        addKit(new StarterKit(prisonCore));
        addKit(new IronKit(prisonCore));
        addKit(new GoldKit(prisonCore));
        addKit(new DiamondKit(prisonCore));

        addxTeam(new Prisoners(prisonCore));
        addxTeam(new Guards(prisonCore));

        this.warps = new ArrayList<>();
        this.activeTrades = new ArrayList<>();
        this.wards = new ArrayList<>();

        ResultSet rs = prisonCore.getxCore().getMysqlDatabase().getSet("SELECT * FROM xPrisonWarps;");
        try {
            if (rs != null) {
                while (rs.next()) {
                    Warp warp = new Warp(prisonCore, rs.getString("Name"), Rank.valueOf(rs.getString("Rank")), WardType.valueOf(rs.getString("Ward")), prisonCore.serialiseLoc(rs.getString("Location")));
                    warps.add(warp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Warp getWarp(String name) {
        for (Warp warp : warps) {
            if (warp.getName().equalsIgnoreCase(name)) {
                return warp;
            }
        }
        return null;
    }

    public Warp createWarp(String name, Rank requiredRank, WardType requiredWardType, Location location) {
        Warp warp = new Warp(prisonCore, name, requiredRank, requiredWardType, location);
        warps.add(warp);
        return warp;
    }

    public void removeWarp(String name) {
        Warp warp = getWarp(name);
        if (warp != null) {
            prisonCore.getxCore().getMysqlDatabase().update("DELETE FROM xPrisonWarps WHERE Name='" + name + "'");
            warps.remove(warp);
        }
    }

    public boolean doesWarpExist(String name) {
        return getApiCore().getxCraftCore().getMysqlDatabase().exists("xPrisonWarps", "Name", name);
    }

    public Ward getWard(String name) {
        for (Ward ward : wards) {
            if (ward.getName().equalsIgnoreCase(name)) {
                return ward;
            }
        }
        return null;
    }

    public Trade getTrade(xCraftPlayer craftPlayer) {
        for (Trade trade : activeTrades) {
            if (trade.getTrader() == craftPlayer) {
                return trade;
            }
        }
        return null;
    }

    public Trade getActiveTrade(String id) {
        for (Trade trade : activeTrades) {
            if (trade.getId().equalsIgnoreCase(id)) {
                return trade;
            }
        }
        return null;
    }

    public Trade createTrade(xCraftPlayer trader, xCraftPlayer recipient){
        Trade trade = new Trade(prisonCore, trader.getName() + "|" +  recipient.getName(), trader, recipient);
        activeTrades.add(trade);
        prisonCore.getServer().getPluginManager().registerEvents(trade, prisonCore);
        return trade;
    }

    public void removeTrade(Trade trade) {
        activeTrades.remove(trade);
        HandlerList.unregisterAll(trade);
    }

    public List<Warp> getWarps() {
        return warps;
    }

    public List<Trade> getActiveTrades() {
        return activeTrades;
    }
}

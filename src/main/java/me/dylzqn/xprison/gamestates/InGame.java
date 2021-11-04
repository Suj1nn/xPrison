package me.dylzqn.xprison.gamestates;

import me.dylzqn.xcraftapi.api.game.GameState;
import me.dylzqn.xcraftapi.api.game.TimerCount;
import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xcraftapi.api.utils.MSGType;
import me.dylzqn.xprison.cells.xCell;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class InGame implements GameState {

    private xPrisonCore prisonCore;

    public InGame(xPrisonCore prisonCore) {
        this.prisonCore = prisonCore;
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public String getName() {
        return "In-Game";
    }

    @Override
    public boolean isJoinable() {
        return true;
    }

    @Override
    public boolean canDamageOthers() {
        return true;
    }

    @Override
    public boolean canBeDamaged() {
        return true;
    }

    @Override
    public boolean canBreak() {
        return true;
    }

    @Override
    public boolean canPlace() {
        return true;
    }

    @Override
    public int getTicks() {
        return 0;
    }

    @Override
    public int getEndTime() {
        return -1;
    }

    @Override
    public TimerCount getTimerType() {
        return TimerCount.UP;
    }

    @Override
    public void runState() {

        int runningTicks = prisonCore.getApiCore().getCurrentGame().getTicks();

        for (Player player : prisonCore.getServer().getOnlinePlayers()) {

            xCraftPlayer craftPlayer = prisonCore.getApiCore().getxCraftPlayer(player.getUniqueId());

            if (craftPlayer != null) {
                if (craftPlayer.getOtherData().get("isTeleporting").equals(true)) {
                    int timeRemaining = (int) craftPlayer.getOtherData().get("teleportingTime");
                    craftPlayer.sendMessage("&3&l> &bTeleporting in &f" + timeRemaining + " &bseconds...");
                    craftPlayer.getOtherData().put("teleportingTime", timeRemaining - 1);
                }

                if (craftPlayer.getOtherData().get("teleportingTime").equals(0)) {
                    craftPlayer.getOtherData().put("isTeleporting", false);
                    craftPlayer.getOtherData().put("teleportingTime", -1);
                    Location location = (Location) craftPlayer.getOtherData().get("teleportLoc");
                    craftPlayer.getPlayer().teleport(location);
                    craftPlayer.sendMessage( "&3&l> &bSwoosh");
                }
            }

//            if (craftPlayer.getRank() == Rank.FOUNDER) {
//                craftPlayer.getPlayer().spawnParticle(Particle.REDSTONE, craftPlayer.getPlayer().getLocation(), 10, 2, 2, 2);
//            }
        }


        if (runningTicks % 600 == 0) {
            for (xCell cell : prisonCore.getCellManager().getCells()) {
                cell.updateCell();
            }
        }

        if (runningTicks % 300 == 0) {
            prisonCore.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', MSGType.LOGO.getPrefix() + "&5> " + "&6Our network will expand in the future. To help us please consider donating."));
        }

        //30mins                   10mins                   5mins                   1min
        if (runningTicks == 5401 || runningTicks == 6600 || runningTicks == 6900 || runningTicks == 7140) {
            prisonCore.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4=================================================="));
            prisonCore.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', MSGType.INFO.getPrefix() + "Server restarting in &f" + prisonCore.formatTime(7200 - runningTicks)));
            prisonCore.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4=================================================="));
        }

        //10 secs
        if (runningTicks > 7290) {
            prisonCore.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4=================================================="));
            prisonCore.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', MSGType.INFO.getPrefix() + "Server restarting in &f" + prisonCore.formatTime(7200 - runningTicks)));
            prisonCore.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4=================================================="));
        }

        if (runningTicks == 7200) {
            prisonCore.getServer().shutdown();
        }
    }
}

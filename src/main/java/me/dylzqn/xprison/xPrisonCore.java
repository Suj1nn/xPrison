package me.dylzqn.xprison;

import com.mojang.authlib.GameProfile;
import me.dylzqn.xcraftapi.api.APICore;
import me.dylzqn.xcraftapi.api.hologram.Hologram;
import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xcraftapi.xCraftCore;
import me.dylzqn.xprison.cells.CellManager;
import me.dylzqn.xprison.commands.xcommand.CommandManager;
import me.dylzqn.xprison.listeners.*;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class xPrisonCore extends JavaPlugin {

    private xCraftCore xCore;
    private APICore apiCore;
    private xPrisonGame prisonGame;
    private CellManager cellManager;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        this.xCore = (xCraftCore) getServer().getPluginManager().getPlugin("xCraftAPI");
        this.apiCore = xCore.getAPICore();

        setupDatabase();

        this.prisonGame = new xPrisonGame(this);
        apiCore.setCurrentGame(prisonGame);

        this.cellManager = new CellManager(this);
        this.commandManager = new CommandManager(this);

        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new QuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new SignListener(this), this);
        getServer().getPluginManager().registerEvents(new RegionListener(this), this);
        getServer().getPluginManager().registerEvents(new CommandProcessListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);

        for (Player player : getServer().getOnlinePlayers()) {
            xCraftPlayer craftPlayer = apiCore.getxCraftPlayer(player.getUniqueId());
            prisonGame.addxCraftPlayer(craftPlayer);
            prisonGame.addPlayerToTeam(craftPlayer, prisonGame.getTeam("Prisoners"));
            loadPrisonPlayer(craftPlayer);
        }
    }

    @Override
    public void onDisable() {

        for (Warp warp : prisonGame.getWarps()) {
            warp.saveData();
        }

        for (Player player : getServer().getOnlinePlayers()) {
            xCraftPlayer craftPlayer = apiCore.getxCraftPlayer(player.getUniqueId());
            savePrisonPlayer(craftPlayer);
        }

        for (Hologram hologram : apiCore.getHologramManager().getHolograms()) {
            hologram.destroy();
        }
    }

    public xCraftCore getxCore() {
        return xCore;
    }

    public APICore getApiCore() {
        return apiCore;
    }

    public xPrisonGame getPrisonGame() {
        return prisonGame;
    }

    public CellManager getCellManager() {
        return cellManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public String formatTime(long seconds) {

        long sec = seconds % 60;
        long minutes = seconds % 3600 / 60;
        long hours = seconds % 86400 / 3600;
        long days = seconds / 86400;

        String formattedTime = "";

        if (days > 0) {
            if (days == 1) {
                formattedTime = formattedTime + days + " Day";
            } else {
                formattedTime = formattedTime + days + " Days";
            }
        }

        if (hours > 0) {
            if (hours == 1) {
                formattedTime = formattedTime + hours + " Hour ";
            } else {
                formattedTime = formattedTime + hours + " Hours ";
            }
        }

        if (minutes > 0) {
            if (minutes == 1) {
                formattedTime = formattedTime + minutes + " Minute ";
            } else {
                formattedTime = formattedTime + minutes + " Minutes ";
            }
        }

        if (hours < 1) {
            if (sec == 1) {
                formattedTime = formattedTime + sec + " Second";
            } else if (sec > 1) {
                formattedTime = formattedTime + sec + " Seconds";

            }
        }

        return formattedTime;
    }

    public Location serialiseLoc(String loc) {
        String[] split = loc.split(",");

        return new Location(getServer().getWorld(split[0]),
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]),
                Float.parseFloat(split[4]),
                Float.parseFloat(split[5]));
    }

    public String deserialiseLoc(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
    }

    public boolean hasPlayedPrison(xCraftPlayer craftPlayer) {
        return xCore.getMysqlDatabase().exists("xPrisonPlayers", "PlayerID", craftPlayer.getPlayerID().toString());
    }

    public void loadPrisonPlayer(xCraftPlayer craftPlayer) {

        if (!hasPlayedPrison(craftPlayer)) {
            craftPlayer.getDBData().clear();
            craftPlayer.getDBData().put("PlayerID", craftPlayer.getPlayerID());
            craftPlayer.getDBData().put("Ward", WardType.X.name());
            craftPlayer.getDBData().put("Money", 100);
            craftPlayer.getDBData().put("CanBeGuard", 0);
            xCore.getMysqlDatabase().set("xPrisonPlayers", craftPlayer.getDBData(), "PlayerID", craftPlayer.getPlayerID().toString());

        }

        craftPlayer.getOtherData().put("Ward", WardType.valueOf((String) xCore.getMysqlDatabase().getObjectByOther("xPrisonPlayers", "Ward", "PlayerID", craftPlayer.getPlayerID().toString())));
        craftPlayer.getOtherData().put("Money", xCore.getMysqlDatabase().getObjectByOther("xPrisonPlayers", "Money", "PlayerID", craftPlayer.getPlayerID().toString()));
        craftPlayer.getOtherData().put("CanBeGuard", xCore.getMysqlDatabase().getObjectByOther("xPrisonPlayers", "CanBeGuard", "PlayerID", craftPlayer.getPlayerID().toString()));
        craftPlayer.getOtherData().put("isTeleporting", false);
        craftPlayer.getOtherData().put("teleportingTime", -1);
        craftPlayer.getOtherData().put("teleportLoc", null);

    }

    public void savePrisonPlayer(xCraftPlayer craftPlayer) {

        craftPlayer.getOtherData().put("isTeleporting", false);
        craftPlayer.getOtherData().put("teleportingTime", -1);
        craftPlayer.getOtherData().put("teleportLoc", null);

        craftPlayer.getDBData().clear();
        craftPlayer.getDBData().put("Ward", craftPlayer.getOtherData().get("Ward"));
        craftPlayer.getDBData().put("Money", craftPlayer.getOtherData().get("Money"));
        if (craftPlayer.getOtherData().get("CanBeGuard").equals(true)) {
            craftPlayer.getDBData().put("CanBeGuard", 1);
        } else {
            craftPlayer.getDBData().put("CanBeGuard", 0);
        }
        xCore.getMysqlDatabase().set("xPrisonPlayers", craftPlayer.getDBData(), "PlayerID", craftPlayer.getPlayerID().toString());
    }

    public boolean hasMoney(xCraftPlayer craftPlayer, int amount) {
        int money = (int) craftPlayer.getOtherData().get("Money");
        return money >= amount;
    }

    public int getMoney(xCraftPlayer craftPlayer) {
        return (int) craftPlayer.getOtherData().get("Money");
    }

    public void removeMoney(xCraftPlayer craftPlayer, int amount) {
        int money = (int) craftPlayer.getOtherData().get("Money");
        craftPlayer.getOtherData().put("Money", money - amount);
        if (craftPlayer.getPlayer() != null) {
            craftPlayer.sendMessage("&c&l- &c$" + amount);
        }
    }

    public void addMoney(xCraftPlayer craftPlayer, int amount) {
        int money = (int) craftPlayer.getOtherData().get("Money");
        craftPlayer.getOtherData().put("Money", money + amount);
        if (craftPlayer.getPlayer() != null) {
            craftPlayer.sendMessage("&a&l+ &a$" + amount);
        }
    }

    private void setupDatabase() {

        List<String> keys = new ArrayList<>();
        keys.add("PlayerID;INT(11)");
        keys.add("Ward;VARCHAR(20)");
        keys.add("Money;INT(11)");
        keys.add("CanBeGuard;BOOLEAN");
        keys.add("StarterKit;DECIMAL(13,0)");
        keys.add("IronKit;DECIMAL(13,0)");
        keys.add("GoldKit;DECIMAL(13,0)");
        keys.add("DiamondKit;DECIMAL(13,0)");

        xCore.getMysqlDatabase().createTable("xPrisonPlayers", keys);

        List<String> prisonWarp = new ArrayList<>();
        prisonWarp.add("WarpID;INT(11) NOT NULL AUTO_INCREMENT");
        prisonWarp.add("Name;VARCHAR(20)");
        prisonWarp.add("Rank;VARCHAR(10)");
        prisonWarp.add("Ward;VARCHAR(10)");
        prisonWarp.add("Location;VARCHAR(150)");

        xCore.getMysqlDatabase().createTable("xPrisonWarps", prisonWarp);

    }

    public void spawnPlayer(Location loc){

        for (Player player: getServer().getOnlinePlayers()) {
            EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

            MinecraftServer mcServer = ((CraftServer)this.getServer()).getServer();
            WorldServer nmsWorld = ((CraftWorld)getServer().getWorld("Prison")).getHandle();

            GameProfile gp = new GameProfile(UUID.randomUUID(), ChatColor.RED + "I'm fake! :D");

            EntityPlayer fake = new EntityPlayer(mcServer, nmsWorld, gp, new PlayerInteractManager(nmsWorld));
            fake.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch()); // Not sure why this isn't working correctly for yaw/pitch :(

            PacketPlayOutPlayerInfo pi = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, fake);
            PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(fake);

            entityPlayer.playerConnection.sendPacket(pi);
            entityPlayer.playerConnection.sendPacket(spawn);
        }
    }

}

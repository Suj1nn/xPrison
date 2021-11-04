package me.dylzqn.xprison.listeners;

import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xcraftapi.api.utils.MSGType;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class JoinListener implements Listener {

    private xPrisonCore prisonCore;

    public JoinListener(xPrisonCore prisonCore) {
        this.prisonCore = prisonCore;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();
        xCraftPlayer craftPlayer = prisonCore.getApiCore().getxCraftPlayer(player.getUniqueId());

        if (!player.hasPlayedBefore()) {

            craftPlayer.getPlayer().teleport(prisonCore.getPrisonGame().getLobbyLocation());
            craftPlayer.sendMessage(MSGType.INFO.getPrefix() + "Welcome to " + "&5&lx&d&lCraft" + "&d's &bPrison Server");

            for (ItemStack itemStack : prisonCore.getPrisonGame().getKit("Starter").getItems()) {
                craftPlayer.getPlayer().getInventory().addItem(itemStack);
            }

            craftPlayer.sendMessage(prisonCore.getApiCore().formatColour("&6&l> &eYou have received your starter kit!"));
        }

        craftPlayer.getPlayer().setGameMode(GameMode.SURVIVAL);

        e.setJoinMessage(prisonCore.getApiCore().formatColour("&8[&2&l+&8] &7" + player.getName() + " &ahas joined."));
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {

        Player player = e.getPlayer();
        xCraftPlayer craftPlayer = prisonCore.getApiCore().getxCraftPlayer(player.getUniqueId());

        prisonCore.loadPrisonPlayer(craftPlayer);

        prisonCore.getPrisonGame().addxCraftPlayer(craftPlayer);
        prisonCore.getPrisonGame().addPlayerToTeam(craftPlayer, prisonCore.getPrisonGame().getTeam("Prisoners"));

        if (!player.isOp()) {
            PermissionAttachment attachment = player.addAttachment(prisonCore);

            for (PermissionAttachmentInfo info : player.getEffectivePermissions()) {
                attachment.setPermission(info.getPermission(), false);
            }

            attachment.setPermission("jetsprisonmines.admin.blockbreak", true);
        }
    }
}

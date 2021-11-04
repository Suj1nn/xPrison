package me.dylzqn.xprison.commands.xcommand;

import me.dylzqn.xcraftapi.api.player.xCraftPlayer;
import me.dylzqn.xcraftapi.api.utils.MSGType;
import me.dylzqn.xprison.WardType;
import me.dylzqn.xprison.commands.*;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor {

    private xPrisonCore prisonCore;
    private List<xCommand> commands;

    public CommandManager(xPrisonCore prisonCore) {
        this.prisonCore = prisonCore;
        this.commands = new ArrayList<>();

        addCommand(new HelpCommand(prisonCore));
        addCommand(new TestCommand(prisonCore));
        addCommand(new SpawnCommand(prisonCore));
        addCommand(new RankupCommand(prisonCore));
        addCommand(new EcoCommand(prisonCore));
        addCommand(new MessageCommand(prisonCore));
        addCommand(new BalanceCommand(prisonCore));
        addCommand(new WarpCommand(prisonCore));
        addCommand(new TradeCommand(prisonCore));
        registerCommands();
    }

    public void addCommand(xCommand command) {
        this.commands.add(command);
    }

    public void registerCommands() {
        for (xCommand command : this.commands) {
            for (String label : command.getLabels()) {
                prisonCore.getCommand(label).setExecutor(this);
            }
        }
    }

    public xCommand getCommand(String name) {
        for (xCommand command : this.commands) {
            if (command.getName().equalsIgnoreCase(name)) {
                return command;
            }
        }
        return null;
    }

    public List<xCommand> getCommands() {
        return commands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        for (xCommand command : commands) {
            if (command.getLabels().contains(label.toLowerCase())) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    xCraftPlayer craftPlayer = prisonCore.getApiCore().getxCraftPlayer(player.getUniqueId());

                    if (craftPlayer.getRank().getID() > command.getRequiredRank().getID()) {
                        craftPlayer.sendNoPermMessage();
                        return true;
                    }

                    if (command.getRequiredWard() != WardType.X) {
                        WardType wardType = (WardType) craftPlayer.getOtherData().get("Ward");
                        if (wardType.getId() < command.getRequiredWard().getId()) {
                            craftPlayer.sendNoPermMessage();
                            return true;
                        }
                    }
                }

                try {
                    command.execute(sender, args);
                } catch (Exception e) {
                    sender.sendMessage(MSGType.ERROR.getPrefix() + "Usage: " + command.getSyntax());
                }
                return true;
            }
        }
        return true;
    }

}



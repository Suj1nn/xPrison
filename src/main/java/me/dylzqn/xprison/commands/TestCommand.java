package me.dylzqn.xprison.commands;

import me.dylzqn.xcraftapi.api.player.Rank;
import me.dylzqn.xprison.WardType;
import me.dylzqn.xprison.commands.xcommand.xCommand;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.command.CommandSender;

public class TestCommand extends xCommand {

    public TestCommand(xPrisonCore prisonCore) {
        super(prisonCore);
        setName("Test");
        this.getLabels().add("test");
        this.setDescription("Test Command");
        this.setSyntax("/test");
        this.setRequiredRank(Rank.DEVELOPER);
        this.setRequiredWard(WardType.X);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {

        sender.sendMessage("Testing ");

        return false;
    }
}

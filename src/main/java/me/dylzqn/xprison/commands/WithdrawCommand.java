package me.dylzqn.xprison.commands;

import me.dylzqn.xprison.commands.xcommand.xCommand;
import me.dylzqn.xprison.xPrisonCore;
import org.bukkit.command.CommandSender;

public class WithdrawCommand extends xCommand {

    public WithdrawCommand(xPrisonCore prisonCore) {
        super(prisonCore);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {
        return true;
    }
}

package me.superischroma.spectaculation.command;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@CommandParameters(description = "Spec test command.", aliases = "test")
public class SpecTestCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (args.length != 0) throw new CommandArgumentException();
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        Player player = sender.getPlayer();
        player.setVelocity(new Vector(-670.5, 58.0, -275.5).multiply(-1.0).multiply(3.0).setY(100.0));
    }
}

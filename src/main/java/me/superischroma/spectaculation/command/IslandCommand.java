package me.superischroma.spectaculation.command;

import me.superischroma.spectaculation.user.PlayerUtils;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "go to or create your island", aliases = "is")
public class IslandCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        Player player = sender.getPlayer();
        PlayerUtils.sendToIsland(player);
    }
}
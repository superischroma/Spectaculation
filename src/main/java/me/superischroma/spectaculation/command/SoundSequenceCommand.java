package me.superischroma.spectaculation.command;

import me.superischroma.spectaculation.sequence.SoundSequenceType;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Play a sound sequence.", usage = "/<command> <sequence>")
public class SoundSequenceCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (args.length != 1) throw new CommandArgumentException();
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        Player player = sender.getPlayer();
        SoundSequenceType type = SoundSequenceType.getByNamespace(args[0]);
        if (type == null)
            throw new CommandFailException("That is not a sound sequence!");
        player.sendMessage(ChatColor.GRAY + "Playing " + type.getNamespace() + "...");
        type.play(player);
    }
}
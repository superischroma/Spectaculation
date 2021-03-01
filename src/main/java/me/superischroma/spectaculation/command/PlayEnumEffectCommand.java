package me.superischroma.spectaculation.command;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Play a Bukkit enum sound.", usage = "/playenumsound <sound>")
public class PlayEnumEffectCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (args.length < 1 || args.length > 2) throw new CommandArgumentException();
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        Player player = sender.getPlayer();
        Effect effect = Effect.valueOf(args[0].toUpperCase());
        int count = 1;
        if (args.length == 2)
            count = Integer.parseInt(args[1]);
        for (int i = 0; i < count; i++)
            player.getWorld().playEffect(player.getLocation(), effect, effect.getData());
        player.sendMessage(ChatColor.GRAY + "Played " + effect.name() + ".");
    }
}
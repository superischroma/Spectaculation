package me.superischroma.spectaculation.command;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Play a Bukkit enum sound.", usage = "/playenumsound <sound>")
public class PlayEnumSoundCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (args.length < 1 || args.length > 4) throw new CommandArgumentException();
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        Player player = sender.getPlayer();
        Sound sound = Sound.valueOf(args[0].toUpperCase());
        float volume = 1.0f, pitch = 1.0f;
        int times = 1;
        if (args.length >= 2)
            volume = Float.parseFloat(args[1]);
        if (args.length >= 3)
            pitch = Float.parseFloat(args[2]);
        if (args.length == 4)
            times = Integer.parseInt(args[3]);
        for (int i = 0; i < times; i++)
            player.playSound(player.getLocation(), sound, volume, pitch);
        player.sendMessage(ChatColor.GRAY + "Played " + sound.name() + ".");
    }
}
package me.superischroma.spectaculation.command;

import me.superischroma.spectaculation.Repeater;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandParameters(description = "Modify your mana amount.", permission = "spt.item")
public class ManaCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (args.length != 1) throw new CommandArgumentException();
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        Player player = sender.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!Repeater.MANA_MAP.containsKey(uuid)) throw new CommandFailException("Something went wrong!");
        int set = Integer.parseInt(args[0]);
        Repeater.MANA_MAP.remove(uuid);
        Repeater.MANA_MAP.put(uuid, set);
        send("Your mana is now " + set + ".");
    }
}

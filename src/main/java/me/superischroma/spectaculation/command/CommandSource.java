package me.superischroma.spectaculation.command;

import lombok.Getter;
import me.superischroma.spectaculation.user.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
public class CommandSource
{
    private final CommandSender sender;
    private final Player player;
    private final User user;

    public CommandSource(CommandSender sender)
    {
        this.sender = sender;
        this.player = sender instanceof Player ? (Player) sender : null;
        this.user = player != null ? User.getUser(player.getUniqueId()) : null;
    }

    public void send(String message)
    {
        sender.sendMessage(message);
    }
}
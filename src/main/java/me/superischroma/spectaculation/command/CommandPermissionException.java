package me.superischroma.spectaculation.command;

import org.bukkit.ChatColor;

public class CommandPermissionException extends RuntimeException
{
    public CommandPermissionException(String permission)
    {
        super(ChatColor.RED + "No permission. You need \"" + permission + "\"");
    }
}
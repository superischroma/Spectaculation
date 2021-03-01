package me.superischroma.spectaculation.entity;

import lombok.Getter;
import org.bukkit.ChatColor;

public enum EntityDropType
{
    GUARANTEED(ChatColor.GREEN),
    COMMON(ChatColor.GREEN),
    OCCASIONAL(ChatColor.BLUE),
    RARE(ChatColor.GOLD),
    VERY_RARE(ChatColor.AQUA),
    EXTRAORDINARILY_RARE(ChatColor.DARK_PURPLE),
    CRAZY_RARE(ChatColor.LIGHT_PURPLE);

    @Getter
    private final ChatColor color;

    EntityDropType(ChatColor color)
    {
        this.color = color;
    }

    public String getDisplay()
    {
        return "" + color + ChatColor.BOLD + name().replaceAll("_", " ");
    }
}

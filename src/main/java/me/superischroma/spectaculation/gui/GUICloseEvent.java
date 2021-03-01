package me.superischroma.spectaculation.gui;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class GUICloseEvent extends PlayerEvent
{
    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final GUI closed;

    public GUICloseEvent(Player player, GUI opened)
    {
        super(player);
        this.closed = opened;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
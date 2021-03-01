package me.superischroma.spectaculation.event;

import org.bukkit.entity.Creeper;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class CreeperIgniteEvent extends EntityEvent implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    public CreeperIgniteEvent(Creeper what)
    {
        super(what);
    }

    public Creeper getEntity()
    {
        return (Creeper) entity;
    }

    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel)
    {
        this.cancelled = cancel;
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
package me.superischroma.spectaculation.user;

import org.bukkit.entity.Player;

public abstract class ItemCollectionReward
{
    private final Type type;

    protected ItemCollectionReward(Type type)
    {
        this.type = type;
    }

    public abstract void onAchieve(Player player);

    protected enum Type
    {
        RECIPE,
        UPGRADE,
        EXPERIENCE
    }
}
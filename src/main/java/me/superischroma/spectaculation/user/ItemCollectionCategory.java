package me.superischroma.spectaculation.user;

public enum ItemCollectionCategory
{
    FARMING,
    MINING,
    COMBAT,
    FORAGING,
    FISHING,
    BOSS;

    public String getName()
    {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
package me.superischroma.spectaculation.item.dragon.young;

import me.superischroma.spectaculation.item.*;

public class YoungDragonHelmet implements MaterialFunction, SkullStatistics, ToolStatistics
{
    @Override
    public double getBaseSpeed()
    {
        return 0.2;
    }

    @Override
    public int getBaseHealth()
    {
        return 70;
    }

    @Override
    public int getBaseDefense()
    {
        return 110;
    }

    @Override
    public String getURL()
    {
        return "5c486af3b882766e82a0bc1665ff02eb6e873b6e0d771f3adabc759b720226a";
    }

    @Override
    public String getDisplayName()
    {
        return "Young Dragon Helmet";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ARMOR;
    }

    @Override
    public SpecificItemType getSpecificType()
    {
        return SpecificItemType.HELMET;
    }

    @Override
    public String getLore()
    {
        return null;
    }
}
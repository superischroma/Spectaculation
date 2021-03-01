package me.superischroma.spectaculation.item.dragon.protector;

import me.superischroma.spectaculation.item.*;

public class ProtectorDragonHelmet implements MaterialFunction, SkullStatistics, ToolStatistics
{
    @Override
    public int getBaseHealth()
    {
        return 70;
    }

    @Override
    public int getBaseDefense()
    {
        return 135;
    }

    @Override
    public String getURL()
    {
        return "f37a596cdc4b11a9948ffa38c2aa3c6942ef449eb0a3982281d3a5b5a14ef6ae";
    }

    @Override
    public String getDisplayName()
    {
        return "Protector Dragon Helmet";
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
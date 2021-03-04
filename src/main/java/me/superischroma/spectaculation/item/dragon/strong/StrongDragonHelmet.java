package me.superischroma.spectaculation.item.dragon.strong;

import me.superischroma.spectaculation.item.*;

public class StrongDragonHelmet implements MaterialFunction, SkullStatistics, ToolStatistics
{
    @Override
    public double getBaseStrength()
    {
        return 25;
    }

    @Override
    public double getBaseHealth()
    {
        return 70;
    }

    @Override
    public double getBaseDefense()
    {
        return 110;
    }

    @Override
    public String getURL()
    {
        return "efde09603b0225b9d24a73a0d3f3e3af29058d448ccd7ce5c67cd02fab0ff682";
    }

    @Override
    public String getDisplayName()
    {
        return "Strong Dragon Helmet";
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
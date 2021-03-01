package me.superischroma.spectaculation.item.dragon.young;

import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.SpecificItemType;
import me.superischroma.spectaculation.item.armor.LeatherArmorStatistics;

public class YoungDragonBoots implements MaterialFunction, LeatherArmorStatistics
{
    @Override
    public double getBaseSpeed()
    {
        return 0.2;
    }

    @Override
    public int getBaseHealth()
    {
        return 60;
    }

    @Override
    public int getBaseDefense()
    {
        return 90;
    }

    @Override
    public int getColor()
    {
        return 0xDDE4F0;
    }

    @Override
    public String getDisplayName()
    {
        return "Young Dragon Boots";
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
        return SpecificItemType.BOOTS;
    }

    @Override
    public String getLore()
    {
        return null;
    }
}
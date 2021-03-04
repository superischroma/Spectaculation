package me.superischroma.spectaculation.item.dragon.young;

import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.SpecificItemType;
import me.superischroma.spectaculation.item.armor.LeatherArmorStatistics;

public class YoungDragonChestplate implements MaterialFunction, LeatherArmorStatistics
{
    @Override
    public double getBaseSpeed()
    {
        return 0.2;
    }

    @Override
    public double getBaseHealth()
    {
        return 120;
    }

    @Override
    public double getBaseDefense()
    {
        return 160;
    }

    @Override
    public int getColor()
    {
        return 0xDDE4F0;
    }

    @Override
    public String getDisplayName()
    {
        return "Young Dragon Chestplate";
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
        return SpecificItemType.CHESTPLATE;
    }

    @Override
    public String getLore()
    {
        return null;
    }
}
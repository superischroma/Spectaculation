package me.superischroma.spectaculation.item.dragon.strong;

import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.SpecificItemType;
import me.superischroma.spectaculation.item.armor.LeatherArmorStatistics;

public class StrongDragonChestplate implements MaterialFunction, LeatherArmorStatistics
{
    @Override
    public int getBaseStrength()
    {
        return 25;
    }

    @Override
    public int getBaseHealth()
    {
        return 120;
    }

    @Override
    public int getBaseDefense()
    {
        return 160;
    }

    @Override
    public int getColor()
    {
        return 0xD91E41;
    }

    @Override
    public String getDisplayName()
    {
        return "Strong Dragon Chestplate";
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
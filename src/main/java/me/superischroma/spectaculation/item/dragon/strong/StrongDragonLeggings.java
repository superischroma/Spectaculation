package me.superischroma.spectaculation.item.dragon.strong;

import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.SpecificItemType;
import me.superischroma.spectaculation.item.armor.LeatherArmorStatistics;

public class StrongDragonLeggings implements MaterialFunction, LeatherArmorStatistics
{
    @Override
    public int getBaseStrength()
    {
        return 25;
    }

    @Override
    public int getBaseHealth()
    {
        return 100;
    }

    @Override
    public int getBaseDefense()
    {
        return 140;
    }

    @Override
    public int getColor()
    {
        return 0xE09419;
    }

    @Override
    public String getDisplayName()
    {
        return "Strong Dragon Leggings";
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
        return SpecificItemType.LEGGINGS;
    }

    @Override
    public String getLore()
    {
        return null;
    }
}
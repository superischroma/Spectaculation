package me.superischroma.spectaculation.item.dragon.strong;

import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.SpecificItemType;
import me.superischroma.spectaculation.item.armor.LeatherArmorStatistics;

public class StrongDragonBoots implements MaterialFunction, LeatherArmorStatistics
{
    @Override
    public int getBaseStrength()
    {
        return 25;
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
        return 0xF0D124;
    }

    @Override
    public String getDisplayName()
    {
        return "Strong Dragon Boots";
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
package me.superischroma.spectaculation.item.dragon.wise;

import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.SpecificItemType;
import me.superischroma.spectaculation.item.armor.LeatherArmorStatistics;

public class WiseDragonLeggings implements MaterialFunction, LeatherArmorStatistics
{
    @Override
    public int getBaseIntelligence()
    {
        return 75;
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
        return 0x29F0E9;
    }

    @Override
    public String getDisplayName()
    {
        return "Wise Dragon Leggings";
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
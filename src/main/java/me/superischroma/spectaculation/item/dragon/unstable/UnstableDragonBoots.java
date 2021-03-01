package me.superischroma.spectaculation.item.dragon.unstable;

import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.SpecificItemType;
import me.superischroma.spectaculation.item.armor.LeatherArmorStatistics;

public class UnstableDragonBoots implements MaterialFunction, LeatherArmorStatistics
{
    @Override
    public double getBaseCritChance()
    {
        return 0.05;
    }

    @Override
    public double getBaseCritDamage()
    {
        return 0.15;
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
        return 0xB212E3;
    }

    @Override
    public String getDisplayName()
    {
        return "Unstable Dragon Boots";
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
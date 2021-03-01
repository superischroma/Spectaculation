package me.superischroma.spectaculation.item.dragon.superior;

import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.SpecificItemType;
import me.superischroma.spectaculation.item.armor.LeatherArmorStatistics;

public class SuperiorDragonLeggings implements MaterialFunction, LeatherArmorStatistics
{
    @Override
    public int getBaseStrength()
    {
        return 10;
    }

    @Override
    public double getBaseCritChance()
    {
        return 0.02;
    }

    @Override
    public double getBaseCritDamage()
    {
        return 0.08;
    }

    @Override
    public int getBaseIntelligence()
    {
        return 25;
    }

    @Override
    public double getBaseSpeed()
    {
        return 0.03;
    }

    @Override
    public int getBaseHealth()
    {
        return 130;
    }

    @Override
    public int getBaseDefense()
    {
        return 170;
    }

    @Override
    public int getColor()
    {
        return 0xF2DF11;
    }

    @Override
    public String getDisplayName()
    {
        return "Superior Dragon Leggings";
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
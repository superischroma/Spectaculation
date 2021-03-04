package me.superischroma.spectaculation.item.dragon.superior;

import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.SpecificItemType;
import me.superischroma.spectaculation.item.armor.LeatherArmorStatistics;

public class SuperiorDragonBoots implements MaterialFunction, LeatherArmorStatistics
{
    @Override
    public double getBaseStrength()
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
    public double getBaseIntelligence()
    {
        return 25;
    }

    @Override
    public double getBaseSpeed()
    {
        return 0.03;
    }

    @Override
    public double getBaseHealth()
    {
        return 80;
    }

    @Override
    public double getBaseDefense()
    {
        return 110;
    }

    @Override
    public int getColor()
    {
        return 0xF25D18;
    }

    @Override
    public String getDisplayName()
    {
        return "Superior Dragon Boots";
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
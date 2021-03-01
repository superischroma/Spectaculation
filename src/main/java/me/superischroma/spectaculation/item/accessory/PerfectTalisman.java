package me.superischroma.spectaculation.item.accessory;

import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.SpecificItemType;

public class PerfectTalisman implements AccessoryStatistics, MaterialFunction
{
    @Override
    public String getURL()
    {
        return "13e8bbc8d174aecd6b46888fa63f9bade14b042e5e17063139d67f8e0163a38";
    }

    @Override
    public String getDisplayName()
    {
        return "Perfect Talisman";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.EXCLUSIVE;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ACCESSORY;
    }

    @Override
    public SpecificItemType getSpecificType()
    {
        return SpecificItemType.ACCESSORY;
    }

    @Override
    public double getBaseCritChance()
    {
        return 1.0;
    }

    @Override
    public int getBaseIntelligence()
    {
        return 800;
    }

    @Override
    public int getBaseStrength()
    {
        return 500;
    }

    @Override
    public int getBaseHealth()
    {
        return 300;
    }

    @Override
    public int getBaseDefense()
    {
        return 100;
    }

    @Override
    public double getBaseCritDamage()
    {
        return 1.5;
    }

    @Override
    public double getBaseSpeed()
    {
        return 2.0;
    }

    @Override
    public double getBaseMagicFind()
    {
        return 100.0;
    }
}
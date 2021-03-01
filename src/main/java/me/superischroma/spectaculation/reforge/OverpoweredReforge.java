package me.superischroma.spectaculation.reforge;

import me.superischroma.spectaculation.item.RarityValue;

// sort of a test reforge
public class OverpoweredReforge implements Reforge
{
    @Override
    public String getName()
    {
        return "Overpowered";
    }

    @Override
    public RarityValue<Integer> getStrength()
    {
        return RarityValue.singleInteger(1000);
    }

    @Override
    public RarityValue<Double> getCritChance()
    {
        return RarityValue.singleDouble(0.5);
    }

    @Override
    public RarityValue<Double> getCritDamage()
    {
        return RarityValue.singleDouble(2.0);
    }

    @Override
    public RarityValue<Integer> getIntelligence()
    {
        return RarityValue.singleInteger(500);
    }
}

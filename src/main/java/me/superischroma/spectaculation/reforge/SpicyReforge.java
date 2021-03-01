package me.superischroma.spectaculation.reforge;

import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.RarityValue;

import java.util.Collections;
import java.util.List;

public class SpicyReforge implements Reforge
{
    @Override
    public String getName()
    {
        return "Spicy";
    }

    @Override
    public RarityValue<Integer> getStrength()
    {
        return new RarityValue<>(2, 3, 4, 7, 10, 12);
    }

    @Override
    public RarityValue<Double> getCritChance()
    {
        return RarityValue.singleDouble(0.01);
    }

    @Override
    public RarityValue<Double> getCritDamage()
    {
        return new RarityValue<>(0.25, 0.35, 0.45, 0.6, 0.8, 1.0);
    }

    @Override
    public List<GenericItemType> getCompatibleTypes()
    {
        return Collections.singletonList(GenericItemType.WEAPON);
    }
}
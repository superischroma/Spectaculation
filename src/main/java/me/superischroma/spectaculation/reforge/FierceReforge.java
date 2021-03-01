package me.superischroma.spectaculation.reforge;

import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.RarityValue;

import java.util.Collections;
import java.util.List;

public class FierceReforge implements Reforge
{
    @Override
    public String getName()
    {
        return "Fierce";
    }

    @Override
    public RarityValue<Integer> getStrength()
    {
        return new RarityValue<>(2, 4, 6, 8, 10, 12);
    }

    @Override
    public RarityValue<Double> getCritChance()
    {
        return new RarityValue<>(0.02, 0.03, 0.04, 0.05, 0.06, 0.08);
    }

    @Override
    public RarityValue<Double> getCritDamage()
    {
        return new RarityValue<>(0.04, 0.07, 0.1, 0.14, 0.18, 0.24);
    }

    @Override
    public List<GenericItemType> getCompatibleTypes()
    {
        return Collections.singletonList(GenericItemType.ARMOR);
    }
}
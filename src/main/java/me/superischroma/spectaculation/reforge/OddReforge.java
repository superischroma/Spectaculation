package me.superischroma.spectaculation.reforge;

import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.RarityValue;

import java.util.Collections;
import java.util.List;

public class OddReforge implements Reforge
{
    @Override
    public String getName()
    {
        return "Odd";
    }

    @Override
    public RarityValue<Double> getCritChance()
    {
        return new RarityValue<>(0.12, 0.15, 0.15, 0.2, 0.25, 0.25);
    }

    @Override
    public RarityValue<Double> getCritDamage()
    {
        return new RarityValue<>(0.1, 0.15, 0.15, 0.22, 0.3, 0.3);
    }

    @Override
    public RarityValue<Integer> getIntelligence()
    {
        return new RarityValue<>(-5, -10, -18, -32, -50, -50);
    }

    @Override
    public List<GenericItemType> getCompatibleTypes()
    {
        return Collections.singletonList(GenericItemType.WEAPON);
    }
}
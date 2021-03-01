package me.superischroma.spectaculation.reforge;

import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.RarityValue;

import java.util.Collections;
import java.util.List;

public class RapidReforge implements Reforge
{
    @Override
    public String getName()
    {
        return "Rapid";
    }

    @Override
    public RarityValue<Integer> getStrength()
    {
        return new RarityValue<>(2, 3, 4, 7, 10, 15);
    }

    @Override
    public RarityValue<Double> getCritDamage()
    {
        return new RarityValue<>(0.35, 0.45, 0.55, 0.65, 0.75, 0.9);
    }

    @Override
    public List<GenericItemType> getCompatibleTypes()
    {
        return Collections.singletonList(GenericItemType.RANGED_WEAPON);
    }
}
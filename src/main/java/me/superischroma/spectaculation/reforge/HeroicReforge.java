package me.superischroma.spectaculation.reforge;

import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.RarityValue;

import java.util.Collections;
import java.util.List;

public class HeroicReforge implements Reforge
{
    @Override
    public String getName()
    {
        return "Heroic";
    }

    @Override
    public RarityValue<Integer> getStrength()
    {
        return new RarityValue<>(15, 20, 25, 32, 40, 50);
    }

    @Override
    public RarityValue<Integer> getIntelligence()
    {
        return new RarityValue<>(40, 50, 65, 80, 100, 125);
    }

    @Override
    public List<GenericItemType> getCompatibleTypes()
    {
        return Collections.singletonList(GenericItemType.WEAPON);
    }
}
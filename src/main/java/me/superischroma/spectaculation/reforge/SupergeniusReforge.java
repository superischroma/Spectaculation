package me.superischroma.spectaculation.reforge;

import me.superischroma.spectaculation.item.RarityValue;

public class SupergeniusReforge implements Reforge
{
    @Override
    public String getName()
    {
        return "Supergenius";
    }

    @Override
    public RarityValue<Integer> getIntelligence()
    {
        return RarityValue.singleInteger(1000000);
    }
}
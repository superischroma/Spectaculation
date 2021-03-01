package me.superischroma.spectaculation.item.tarantula;

import me.superischroma.spectaculation.item.*;

public class DigestedMosquito implements MaterialStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Digested Mosquito";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ITEM;
    }
}
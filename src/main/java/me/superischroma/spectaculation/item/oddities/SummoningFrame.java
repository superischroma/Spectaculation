package me.superischroma.spectaculation.item.oddities;

import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.MaterialStatistics;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;

public class SummoningFrame implements MaterialFunction, MaterialStatistics
{
    @Override
    public String getDisplayName()
    {
        return "Summoning Frame";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.EXCLUSIVE;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.BLOCK;
    }
}
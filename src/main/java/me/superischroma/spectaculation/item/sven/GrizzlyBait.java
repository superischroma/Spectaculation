package me.superischroma.spectaculation.item.sven;

import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.MaterialStatistics;
import me.superischroma.spectaculation.item.Rarity;

public class GrizzlyBait implements MaterialStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Grizzly Bait";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.RARE;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ITEM;
    }
}
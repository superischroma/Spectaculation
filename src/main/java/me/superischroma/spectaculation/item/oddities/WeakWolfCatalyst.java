package me.superischroma.spectaculation.item.oddities;

import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.MaterialStatistics;
import me.superischroma.spectaculation.item.Rarity;

public class WeakWolfCatalyst implements MaterialStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Weak Wolf Catalyst";
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

    @Override
    public boolean isEnchanted()
    {
        return true;
    }
}

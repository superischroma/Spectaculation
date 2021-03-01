package me.superischroma.spectaculation.item.sven;

import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.MaterialStatistics;
import me.superischroma.spectaculation.item.Rarity;

public class OverfluxCapacitor implements MaterialStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Overflux Capacitor";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.EPIC;
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
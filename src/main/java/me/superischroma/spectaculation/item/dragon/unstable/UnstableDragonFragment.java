package me.superischroma.spectaculation.item.dragon.unstable;

import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.SkullStatistics;

public class UnstableDragonFragment implements SkullStatistics, MaterialFunction
{
    @Override
    public String getURL()
    {
        return "98228c234c3903c512a5a0aa45260e7b567e0e20eefc7d561ccec97b295871af";
    }

    @Override
    public String getDisplayName()
    {
        return "Unstable Dragon Fragment";
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
}
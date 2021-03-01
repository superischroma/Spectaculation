package me.superischroma.spectaculation.item.revenant;

import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.SkullStatistics;

public class RevenantCatalyst implements SkullStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Revenant Catalyst";
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
    public String getURL()
    {
        return "b88cfafa5f03f8aef042a143799e964342df76b7c1eb461f618e398f84a99a63";
    }
}
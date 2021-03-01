package me.superischroma.spectaculation.item.revenant;

import me.superischroma.spectaculation.item.*;

public class UndeadCatalyst implements SkullStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Undead Catalyst";
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
    public String getURL()
    {
        return "80625369b0a7b052632db6b926a87670219539922836ac5940be26d34bf14e10";
    }
}
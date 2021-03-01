package me.superischroma.spectaculation.item.dragon.protector;

import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.SkullStatistics;

public class ProtectorDragonFragment implements SkullStatistics, MaterialFunction
{
    @Override
    public String getURL()
    {
        return "48de339af63a229c9238d027e47f53eeb56141a419f51b35c31ea1494b435dd3";
    }

    @Override
    public String getDisplayName()
    {
        return "Protector Dragon Fragment";
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
package me.superischroma.spectaculation.item.bow;

import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.SpecificItemType;
import me.superischroma.spectaculation.item.ToolStatistics;

public class Bow implements ToolStatistics, BowFunction
{
    @Override
    public String getDisplayName()
    {
        return "Bow";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.COMMON;
    }

    @Override
    public int getBaseDamage()
    {
        return 30;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.RANGED_WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType()
    {
        return SpecificItemType.BOW;
    }
}
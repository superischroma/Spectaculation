package me.superischroma.spectaculation.item.revenant;

import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.SkullStatistics;

public class BeheadedHorror implements SkullStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Beheaded Horror";
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
        return "dbad99ed3c820b7978190ad08a934a68dfa90d9986825da1c97f6f21f49ad626";
    }
}
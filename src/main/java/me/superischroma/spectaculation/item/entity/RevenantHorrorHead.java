package me.superischroma.spectaculation.item.entity;

import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.SkullStatistics;

public class RevenantHorrorHead implements SkullStatistics, MaterialFunction
{
    @Override
    public String getURL()
    {
        return "d8bee23b5c726ae8e3d021e8b4f7525619ab102a4e04be983b61414349aaac67";
    }

    @Override
    public String getDisplayName()
    {
        return "Revenant Horror Head";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.EXCLUSIVE;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ARMOR;
    }
}
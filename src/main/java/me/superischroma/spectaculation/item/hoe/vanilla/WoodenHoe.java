package me.superischroma.spectaculation.item.hoe.vanilla;

import me.superischroma.spectaculation.item.*;

public class WoodenHoe implements ToolStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Wooden Hoe";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.COMMON;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.TOOL;
    }

    @Override
    public SpecificItemType getSpecificType()
    {
        return SpecificItemType.HOE;
    }
}
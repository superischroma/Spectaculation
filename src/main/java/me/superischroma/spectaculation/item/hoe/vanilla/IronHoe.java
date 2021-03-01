package me.superischroma.spectaculation.item.hoe.vanilla;

import me.superischroma.spectaculation.item.*;

public class IronHoe implements ToolStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Iron Hoe";
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
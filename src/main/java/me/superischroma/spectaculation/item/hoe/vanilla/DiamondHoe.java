package me.superischroma.spectaculation.item.hoe.vanilla;

import me.superischroma.spectaculation.item.*;

public class DiamondHoe implements ToolStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Diamond Hoe";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.UNCOMMON;
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
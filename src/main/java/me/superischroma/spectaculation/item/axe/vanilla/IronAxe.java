package me.superischroma.spectaculation.item.axe.vanilla;

import me.superischroma.spectaculation.item.*;

public class IronAxe implements ToolStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Iron Axe";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.COMMON;
    }

    @Override
    public int getBaseDamage()
    {
        return 25;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.TOOL;
    }

    @Override
    public SpecificItemType getSpecificType()
    {
        return SpecificItemType.AXE;
    }
}
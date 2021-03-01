package me.superischroma.spectaculation.item.axe.vanilla;

import me.superischroma.spectaculation.item.*;

public class WoodenAxe implements ToolStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Wooden Axe";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.COMMON;
    }

    @Override
    public int getBaseDamage()
    {
        return 10;
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
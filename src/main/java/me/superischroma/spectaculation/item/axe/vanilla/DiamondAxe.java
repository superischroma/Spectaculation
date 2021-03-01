package me.superischroma.spectaculation.item.axe.vanilla;

import me.superischroma.spectaculation.item.*;

public class DiamondAxe implements ToolStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Diamond Axe";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.UNCOMMON;
    }

    @Override
    public int getBaseDamage()
    {
        return 30;
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
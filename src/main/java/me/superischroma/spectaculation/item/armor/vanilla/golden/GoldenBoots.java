package me.superischroma.spectaculation.item.armor.vanilla.golden;

import me.superischroma.spectaculation.item.*;

public class GoldenBoots implements ToolStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Golden Boots";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.COMMON;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ARMOR;
    }

    @Override
    public SpecificItemType getSpecificType()
    {
        return SpecificItemType.BOOTS;
    }

    @Override
    public int getBaseDefense()
    {
        return 5;
    }
}

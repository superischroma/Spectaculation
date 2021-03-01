package me.superischroma.spectaculation.item.armor.vanilla.golden;

import me.superischroma.spectaculation.item.*;

public class GoldenLeggings implements ToolStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Golden Chestplate";
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
        return SpecificItemType.LEGGINGS;
    }

    @Override
    public int getBaseDefense()
    {
        return 15;
    }
}

package me.superischroma.spectaculation.item.armor.vanilla.iron;

import me.superischroma.spectaculation.item.*;

public class IronLeggings implements ToolStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Iron Chestplate";
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
        return 25;
    }
}

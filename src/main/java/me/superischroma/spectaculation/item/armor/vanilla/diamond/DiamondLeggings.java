package me.superischroma.spectaculation.item.armor.vanilla.diamond;

import me.superischroma.spectaculation.item.*;

public class DiamondLeggings implements ToolStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Diamond Chestplate";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.UNCOMMON;
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
    public double getBaseDefense()
    {
        return 30;
    }
}

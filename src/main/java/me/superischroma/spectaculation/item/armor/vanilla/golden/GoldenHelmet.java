package me.superischroma.spectaculation.item.armor.vanilla.golden;

import me.superischroma.spectaculation.item.*;

public class GoldenHelmet implements ToolStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Golden Helmet";
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
        return SpecificItemType.HELMET;
    }

    @Override
    public int getBaseDefense()
    {
        return 10;
    }
}

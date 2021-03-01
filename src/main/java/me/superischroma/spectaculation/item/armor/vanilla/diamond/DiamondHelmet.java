package me.superischroma.spectaculation.item.armor.vanilla.diamond;

import me.superischroma.spectaculation.item.*;

public class DiamondHelmet implements ToolStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Diamond Helmet";
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
        return SpecificItemType.HELMET;
    }

    @Override
    public int getBaseDefense()
    {
        return 15;
    }
}

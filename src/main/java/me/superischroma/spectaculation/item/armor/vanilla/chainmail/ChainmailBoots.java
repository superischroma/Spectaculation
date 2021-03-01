package me.superischroma.spectaculation.item.armor.vanilla.chainmail;

import me.superischroma.spectaculation.item.*;

public class ChainmailBoots implements ToolStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Chainmail Boots";
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
        return 7;
    }
}

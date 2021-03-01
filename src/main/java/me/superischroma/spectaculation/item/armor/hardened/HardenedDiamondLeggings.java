package me.superischroma.spectaculation.item.armor.hardened;

import me.superischroma.spectaculation.item.*;

public class HardenedDiamondLeggings implements ToolStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Hardened Diamond Leggings";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.RARE;
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
        return 95;
    }

}
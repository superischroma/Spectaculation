package me.superischroma.spectaculation.item.armor.lapis;

import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.item.armor.LeatherArmorStatistics;

public class LapisArmorBoots implements LeatherArmorStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Lapis Armor Boots";
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
        return SpecificItemType.BOOTS;
    }
    
    @Override
    public int getBaseDefense()
    {
        return 20;
    }

    @Override
    public int getColor()
    {
        return 0x0000FF;
    }
}
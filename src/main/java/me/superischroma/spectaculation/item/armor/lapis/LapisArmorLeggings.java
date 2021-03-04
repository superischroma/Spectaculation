package me.superischroma.spectaculation.item.armor.lapis;

import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.item.armor.LeatherArmorStatistics;

public class LapisArmorLeggings implements LeatherArmorStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Lapis Armor Leggings";
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
        return 35;
    }

    @Override
    public int getColor()
    {
        return 0x0000FF;
    }
}
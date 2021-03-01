package me.superischroma.spectaculation.item.dragon.old;

import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.SpecificItemType;
import me.superischroma.spectaculation.item.armor.LeatherArmorStatistics;

public class OldDragonChestplate implements MaterialFunction, LeatherArmorStatistics
{
    @Override
    public int getBaseHealth()
    {
        return 160;
    }

    @Override
    public int getBaseDefense()
    {
        return 150;
    }

    @Override
    public int getColor()
    {
        return 0xF0E6AA;
    }

    @Override
    public String getDisplayName()
    {
        return "Old Dragon Chestplate";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ARMOR;
    }

    @Override
    public SpecificItemType getSpecificType()
    {
        return SpecificItemType.CHESTPLATE;
    }

    @Override
    public String getLore()
    {
        return null;
    }
}
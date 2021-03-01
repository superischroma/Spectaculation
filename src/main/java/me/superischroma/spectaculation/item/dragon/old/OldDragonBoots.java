package me.superischroma.spectaculation.item.dragon.old;

import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.SpecificItemType;
import me.superischroma.spectaculation.item.armor.LeatherArmorStatistics;

public class OldDragonBoots implements MaterialFunction, LeatherArmorStatistics
{
    @Override
    public int getBaseHealth()
    {
        return 80;
    }

    @Override
    public int getBaseDefense()
    {
        return 90;
    }

    @Override
    public int getColor()
    {
        return 0xF0E6AA;
    }

    @Override
    public String getDisplayName()
    {
        return "Old Dragon Boots";
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
        return SpecificItemType.BOOTS;
    }

    @Override
    public String getLore()
    {
        return null;
    }
}
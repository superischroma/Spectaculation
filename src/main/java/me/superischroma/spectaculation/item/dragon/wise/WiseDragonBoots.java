package me.superischroma.spectaculation.item.dragon.wise;

import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.SpecificItemType;
import me.superischroma.spectaculation.item.armor.LeatherArmorStatistics;

public class WiseDragonBoots implements MaterialFunction, LeatherArmorStatistics
{
    @Override
    public int getBaseIntelligence()
    {
        return 75;
    }

    @Override
    public int getBaseHealth()
    {
        return 60;
    }

    @Override
    public int getBaseDefense()
    {
        return 90;
    }

    @Override
    public int getColor()
    {
        return 0x29F0E9;
    }

    @Override
    public String getDisplayName()
    {
        return "Wise Dragon Boots";
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
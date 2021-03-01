package me.superischroma.spectaculation.item.armor.miner;

import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.item.armor.LeatherArmorStatistics;

public class MinerChestplate implements LeatherArmorStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Miner Chestplate";
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
        return SpecificItemType.CHESTPLATE;
    }

    @Override
    public int getBaseDefense()
    {
        return 5;
    }

    @Override
    public int getColor()
    {
        return 0x0000FF;
    }
}
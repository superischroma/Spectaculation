package me.superischroma.spectaculation.item.enchanted;

import me.superischroma.spectaculation.item.*;

import java.util.ArrayList;
import java.util.List;

public class EnchantedCharcoal implements MaterialStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Enchanted Charcoal";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.UNCOMMON;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ITEM;
    }

    @Override
    public boolean isEnchanted()
    {
        return true;
    }
}
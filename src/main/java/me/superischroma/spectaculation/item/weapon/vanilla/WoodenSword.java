package me.superischroma.spectaculation.item.weapon.vanilla;

import me.superischroma.spectaculation.item.*;

public class WoodenSword implements ToolStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Wooden Sword";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.COMMON;
    }

    @Override
    public int getBaseDamage()
    {
        return 20;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType()
    {
        return SpecificItemType.SWORD;
    }
}
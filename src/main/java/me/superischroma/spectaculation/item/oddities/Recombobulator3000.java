package me.superischroma.spectaculation.item.oddities;

import me.superischroma.spectaculation.item.*;

public class Recombobulator3000 implements SkullStatistics, MaterialFunction
{
    @Override
    public String getURL()
    {
        return "57ccd36dc8f72adcb1f8c8e61ee82cd96ead140cf2a16a1366be9b5a8e3cc3fc";
    }

    @Override
    public String getDisplayName()
    {
        return "Recombobulator 3000";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ITEM;
    }

    @Override
    public SpecificItemType getSpecificType()
    {
        return SpecificItemType.DUNGEON_ITEM;
    }

    @Override
    public boolean isStackable()
    {
        return false;
    }
}

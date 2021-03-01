package me.superischroma.spectaculation.item.accessory;

import me.superischroma.spectaculation.item.*;

public interface AccessoryStatistics extends PlayerBoostStatistics, SkullStatistics, Reforgable
{
    @Override
    default GenericItemType getType()
    {
        return GenericItemType.ACCESSORY;
    }

    @Override
    default SpecificItemType getSpecificType()
    {
        return SpecificItemType.ACCESSORY;
    }
}
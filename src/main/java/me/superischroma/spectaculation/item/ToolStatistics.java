package me.superischroma.spectaculation.item;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

public interface ToolStatistics extends PlayerBoostStatistics, Enchantable, Reforgable
{
    @Override
    String getDisplayName();

    @Override
    Rarity getRarity();

    @Override
    GenericItemType getType();

    @Override
    default boolean isStackable()
    {
        return false;
    }

    @Override
    default NBTTagCompound getData()
    {
        return new NBTTagCompound();
    }
}
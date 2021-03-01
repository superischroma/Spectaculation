package me.superischroma.spectaculation.item.oddities;

import me.superischroma.spectaculation.item.*;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class Bouncer implements MaterialStatistics, MaterialFunction, ItemData
{
    @Override
    public String getDisplayName()
    {
        return "Bouncer";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.EXCLUSIVE;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.BLOCK;
    }

    @Override
    public NBTTagCompound getData()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setFloat("bounce", 1f);
        compound.setLong("delay", 20L);
        compound.setFloat("velX", 1f);
        compound.setFloat("velY", 1f);
        compound.setFloat("velZ", 1f);
        return compound;
    }
}

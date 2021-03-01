package me.superischroma.spectaculation.item.oddities;

import me.superischroma.spectaculation.item.*;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class Launcher implements MaterialStatistics, MaterialFunction, ItemData
{
    @Override
    public String getDisplayName()
    {
        return "Launcher";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.LEGENDARY;
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
        compound.setFloat("velX", 1f);
        compound.setFloat("velY", 1f);
        compound.setFloat("velZ", 1f);
        return compound;
    }
}

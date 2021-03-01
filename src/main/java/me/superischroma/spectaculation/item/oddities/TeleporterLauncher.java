package me.superischroma.spectaculation.item.oddities;

import me.superischroma.spectaculation.item.*;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class TeleporterLauncher implements MaterialStatistics, MaterialFunction, ItemData
{
    @Override
    public String getDisplayName()
    {
        return "Teleporter Launcher";
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
    public boolean isEnchanted()
    {
        return true;
    }

    @Override
    public NBTTagCompound getData()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setFloat("velX", 1f);
        compound.setFloat("velY", 1f);
        compound.setFloat("velZ", 1f);
        compound.setDouble("x", 0.0);
        compound.setDouble("y", 0.0);
        compound.setDouble("z", 0.0);
        compound.setString("world", "world");
        compound.setFloat("yaw", 0.0f);
        compound.setFloat("pitch", 0.0f);
        compound.setLong("delay", 60);
        return compound;
    }
}

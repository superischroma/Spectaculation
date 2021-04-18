package me.superischroma.spectaculation.util;

import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public class SerialNBTTagCompound extends NBTTagCompound implements ConfigurationSerializable
{
    public SerialNBTTagCompound() {}

    public SerialNBTTagCompound(NBTTagCompound compound)
    {
        for (String k : compound.c())
            this.set(k, compound.get(k));
    }

    @Override
    public Map<String, Object> serialize()
    {
        Map<String, Object> map = new HashMap<>();
        for (String k : c())
        {
            NBTBase b = this.get(k);
            if (b instanceof NBTTagCompound)
            {
                SerialNBTTagCompound serial = new SerialNBTTagCompound((NBTTagCompound) b);
                for (Map.Entry<String, Object> entry : serial.serialize().entrySet())
                    map.put(k + "." + entry.getKey(), entry.getValue());
                continue;
            }
            map.put(k, SUtil.getObjectFromCompound(this, k));
        }
        return map;
    }

    public static SerialNBTTagCompound deserialize(Map<String, Object> map)
    {
        SerialNBTTagCompound compound = new SerialNBTTagCompound();
        for (Map.Entry<String, Object> entry : map.entrySet())
        {
            String key = entry.getKey();
            String[] dir = entry.getKey().split("\\.");
            if (dir.length >= 2)
            {
                key = dir[dir.length - 1];
                NBTTagCompound track = compound;
                for (int i = 0; i < dir.length - 1; i++)
                {
                    if (!track.hasKey(dir[i]))
                        track.set(dir[i], new NBTTagCompound());
                    track = track.getCompound(dir[i]);
                }
                track.set(key, SUtil.getBaseFromObject(entry.getValue()));
                continue;
            }
            compound.set(key, SUtil.getBaseFromObject(entry.getValue()));
        }
        return compound;
    }
}
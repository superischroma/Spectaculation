package me.superischroma.spectaculation.item;

import lombok.Getter;
import lombok.Setter;
import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.util.SUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

@Getter
public class SBlock
{
    protected static final Spectaculation plugin = Spectaculation.getPlugin();

    private final Location location;
    @Setter
    private SMaterial type;
    private NBTTagCompound data;

    public SBlock(Location location, SMaterial type, NBTTagCompound data)
    {
        this.location = location;
        this.type = type;
        this.data = data;
    }

    public float getDataFloat(String key)
    {
        return data.getFloat(key);
    }

    public long getDataLong(String key)
    {
        return data.getLong(key);
    }

    public double getDataDouble(String key)
    {
        return data.getDouble(key);
    }

    public String getDataString(String key)
    {
        return data.getString(key);
    }

    public static SBlock getBlock(Location location)
    {
        ConfigurationSection cs = plugin.blocks.getConfigurationSection(toLocationString(location));
        if (cs == null)
            return null;
        NBTTagCompound compound = new NBTTagCompound();
        for (String key : cs.getKeys(false))
        {
            if (key.equals("type"))
                continue;
            compound.set(key, SUtil.getBaseFromObject(cs, key));
        }
        return new SBlock(location, SMaterial.getMaterial(cs.getString("type")), compound);
    }

    public void save()
    {
        plugin.blocks.set(toLocationString() + ".type", type.name());
        for (String key : data.c())
        {
            Object o = SUtil.getObjectFromCompound(data, key);
            if (o instanceof NBTTagCompound)
                continue;
            plugin.blocks.set(toLocationString() + "." + key, o);
        }
        plugin.blocks.save();
    }

    public void delete()
    {
        plugin.blocks.set(toLocationString(), null);
        plugin.blocks.save();
    }

    private String toLocationString()
    {
        return toLocationString(location);
    }

    private static String toLocationString(Location location)
    {
        return location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ() + "," + location.getWorld().getName();
    }
}
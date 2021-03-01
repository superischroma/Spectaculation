package me.superischroma.spectaculation.entity.nms;

import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class WiseDragon extends Dragon
{
    public WiseDragon(World world)
    {
        super(world, Dragon.DEFAULT_SPEED_MODIFIER, Dragon.DEFAULT_DAMAGE_DEGREE_RANGE, 200);
    }

    public WiseDragon()
    {
        this(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle());
    }

    @Override
    public String getEntityName()
    {
        return "Wise Dragon";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 9000000.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 1200.0;
    }
}
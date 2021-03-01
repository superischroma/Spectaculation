package me.superischroma.spectaculation.entity.nms;

import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class OldDragon extends Dragon
{
    public OldDragon(World world)
    {
        super(world, 1.2, Dragon.DEFAULT_DAMAGE_DEGREE_RANGE, Dragon.DEFAULT_ATTACK_COOLDOWN);
    }

    public OldDragon()
    {
        this(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle());
    }

    @Override
    public String getEntityName()
    {
        return "Old Dragon";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 15000000.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 1400.0;
    }
}
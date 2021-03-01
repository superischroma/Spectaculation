package me.superischroma.spectaculation.entity.nms;

import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class SuperiorDragon extends Dragon
{
    public SuperiorDragon(World world)
    {
        super(world, 1.6, Dragon.DEFAULT_DAMAGE_DEGREE_RANGE, Dragon.DEFAULT_ATTACK_COOLDOWN);
    }

    public SuperiorDragon()
    {
        this(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle());
    }

    @Override
    public String getEntityName()
    {
        return "Superior Dragon";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 12000000.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 1600.0;
    }
}
package me.superischroma.spectaculation.entity.nms;

import net.minecraft.server.v1_8_R3.World;
import org.apache.commons.lang3.Range;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class UnstableDragon extends Dragon
{
    public UnstableDragon(World world)
    {
        super(world, Dragon.DEFAULT_SPEED_MODIFIER, Range.between(0.6, 0.9), Dragon.DEFAULT_ATTACK_COOLDOWN);
    }

    public UnstableDragon()
    {
        this(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle());
    }

    @Override
    public String getEntityName()
    {
        return "Unstable Dragon";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 9000000.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 1400.0;
    }
}
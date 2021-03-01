package me.superischroma.spectaculation.entity.nms;

import net.minecraft.server.v1_8_R3.World;
import org.apache.commons.lang3.Range;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class StrongDragon extends Dragon
{
    public StrongDragon(World world)
    {
        super(world, Dragon.DEFAULT_SPEED_MODIFIER, Range.between(0.5, 0.8), Dragon.DEFAULT_ATTACK_COOLDOWN);
    }

    public StrongDragon()
    {
        this(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle());
    }

    @Override
    public String getEntityName()
    {
        return "Strong Dragon";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 9000000.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 1700.0;
    }
}
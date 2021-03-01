package me.superischroma.spectaculation.entity.nms;

import me.superischroma.spectaculation.entity.EntityFunction;
import me.superischroma.spectaculation.entity.EntityStatistics;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class VelocityArmorStand extends EntityArmorStand implements EntityStatistics, EntityFunction, SNMSEntity
{
    public VelocityArmorStand(World world)
    {
        super(world);
        this.setGravity(true);
        this.noclip = true;
    }

    public VelocityArmorStand()
    {
        this(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle());
    }

    public void g(float f, float f1)
    {
        if (!hasGravity())
            super.g(f, f1);
        else
            move(motX, motY, motZ);
    }

    @Override
    public String getEntityName()
    {
        return null;
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 1.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 0;
    }

    @Override
    public boolean hasNameTag()
    {
        return false;
    }

    public double getXPDropped()
    {
        return 0.0;
    }

    @Override
    public LivingEntity spawn(Location location)
    {
        this.world = ((CraftWorld) location.getWorld()).getHandle();
        this.setPosition(location.getX(), location.getY(), location.getZ());
        this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (LivingEntity) this.getBukkitEntity();
    }
}
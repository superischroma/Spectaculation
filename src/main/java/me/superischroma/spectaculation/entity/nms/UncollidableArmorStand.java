package me.superischroma.spectaculation.entity.nms;

import me.superischroma.spectaculation.entity.EntityFunction;
import me.superischroma.spectaculation.entity.EntityStatistics;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class UncollidableArmorStand extends EntityArmorStand implements EntityStatistics, EntityFunction, SNMSEntity
{
    public UncollidableArmorStand(World world)
    {
        super(world);
        n(true);
    }

    public UncollidableArmorStand()
    {
        this(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle());
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
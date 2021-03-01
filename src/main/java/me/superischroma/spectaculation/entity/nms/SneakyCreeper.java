package me.superischroma.spectaculation.entity.nms;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.entity.EntityStatistics;
import me.superischroma.spectaculation.entity.SEntity;
import me.superischroma.spectaculation.entity.caverns.CreeperFunction;
import me.superischroma.spectaculation.event.CreeperIgniteEvent;
import me.superischroma.spectaculation.util.SLog;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreeper;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;

public class SneakyCreeper extends EntityCreeper implements EntityStatistics, SNMSEntity, CreeperFunction
{
    public SneakyCreeper(World world)
    {
        super(world);
    }

    public SneakyCreeper()
    {
        this(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle());
    }

    @Override
    public String getEntityName()
    {
        return "Sneaky Creeper";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 120.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 80.0;
    }

    @Override
    public boolean isVisible()
    {
        return false;
    }

    @Override
    public void t_()
    {
        try
        {
            Field f = EntityCreeper.class.getDeclaredField("fuseTicks");
            f.setAccessible(true);
            int fuseTicks = (int) f.get(this);
            if (cm() > 0 && fuseTicks == 0)
            {
                CreeperIgniteEvent ignite = new CreeperIgniteEvent((Creeper) this.getBukkitEntity());
                Spectaculation.getPlugin().getServer().getPluginManager().callEvent(ignite);
                if (ignite.isCancelled())
                    return;
            }
        }
        catch (IllegalAccessException | NoSuchFieldException ignored) {}
        super.t_();
    }

    @Override
    public void onCreeperIgnite(CreeperIgniteEvent e, SEntity sEntity)
    {
        sEntity.setVisible(true);
        new BukkitRunnable()
        {
            public void run()
            {
                if (e.getEntity().isDead())
                    return;
                sEntity.setVisible(false);
            }
        }.runTaskLater(Spectaculation.getPlugin(), 35);
    }

    @Override
    public LivingEntity spawn(Location location)
    {
        this.world = ((CraftWorld) location.getWorld()).getHandle();
        this.setPosition(location.getX(), location.getY(), location.getZ());
        this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (LivingEntity) this.getBukkitEntity();
    }

    public double getXPDropped()
    {
        return 8.0;
    }
}
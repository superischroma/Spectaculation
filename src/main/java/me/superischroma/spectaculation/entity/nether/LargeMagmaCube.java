package me.superischroma.spectaculation.entity.nether;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.entity.EntityFunction;
import me.superischroma.spectaculation.entity.SEntity;
import me.superischroma.spectaculation.entity.SlimeStatistics;
import me.superischroma.spectaculation.util.SLog;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftMagmaCube;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class LargeMagmaCube implements SlimeStatistics, EntityFunction
{
    @Override
    public String getEntityName()
    {
        return "Magma Cube";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 300.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 150.0;
    }

    @Override
    public double getXPDropped()
    {
        return 4.0;
    }

    @Override
    public void onTarget(SEntity sEntity, EntityTargetLivingEntityEvent e)
    {
        LivingEntity entity = (LivingEntity) e.getEntity();
        Entity found = e.getTarget();
        new BukkitRunnable()
        {
            public void run()
            {
                if (entity.isDead())
                {
                    cancel();
                    return;
                }
                Entity target = ((CraftMagmaCube) entity).getHandle().getGoalTarget().getBukkitEntity();
                if (!found.equals(target))
                {
                    cancel();
                    return;
                }
                for (int i = 0; i < 3; i++)
                {
                    new BukkitRunnable()
                    {
                        public void run()
                        {
                            if (entity.isDead())
                            {
                                cancel();
                                return;
                            }
                            Fireball fireball = entity.getWorld().spawn(entity.getEyeLocation().add(
                                    entity.getEyeLocation().getDirection().multiply(3.0)), Fireball.class);
                            fireball.setMetadata("magma", new FixedMetadataValue(Spectaculation.getPlugin(), sEntity));
                            fireball.setDirection(target.getLocation().getDirection().multiply(-1.0).normalize());
                        }
                    }.runTaskLater(Spectaculation.getPlugin(), (i + 1) * 10);
                }
            }
        }.runTaskTimer(Spectaculation.getPlugin(), 60, 100);
    }

    @Override
    public int getSize()
    {
        return 6;
    }

    @Override
    public boolean split()
    {
        return false;
    }
}
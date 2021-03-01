package me.superischroma.spectaculation.entity.skeleton;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.entity.EntityFunction;
import me.superischroma.spectaculation.entity.EntityStatistics;
import me.superischroma.spectaculation.entity.SEntity;
import me.superischroma.spectaculation.entity.SEntityType;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class HighLevelSkeleton implements EntityStatistics, EntityFunction
{
    @Override
    public String getEntityName()
    {
        return "Skeleton";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 200.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 47.0;
    }

    @Override
    public double getXPDropped()
    {
        return 6.0;
    }

    @Override
    public void onDeath(SEntity sEntity, Entity killed, Entity damager)
    {
        Item item = sEntity.getEntity().getWorld().dropItem(sEntity.getEntity().getLocation(), new ItemStack(Material.BONE, 2));
        new BukkitRunnable()
        {
            public void run()
            {
                if (item.isDead())
                    return;
                Location spawn = item.getLocation().clone().add(0, 1, 0);
                for (int i = 0; i < 5; i++)
                    item.getWorld().spigot().playEffect(spawn, Effect.PARTICLE_SMOKE, 0, 1, 0f, 0f, 0f, 0f, 1, 20);
                new SEntity(spawn, SEntityType.HIGH_LEVEL_SKELETON);
                item.remove();
            }
        }.runTaskLater(Spectaculation.getPlugin(), 100);
    }
}
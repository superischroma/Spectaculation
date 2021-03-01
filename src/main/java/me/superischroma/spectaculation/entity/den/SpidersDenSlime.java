package me.superischroma.spectaculation.entity.den;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.entity.EntityFunction;
import me.superischroma.spectaculation.entity.SlimeStatistics;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SpidersDenSlime implements SlimeStatistics, EntityFunction
{
    @Override
    public String getEntityName()
    {
        return "Slime";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return SUtil.random(200.0, 400.0);
    }

    @Override
    public double getDamageDealt()
    {
        return 140.0;
    }

    @Override
    public int getSize()
    {
        return 9;
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent e)
    {
        new BukkitRunnable()
        {
            public void run()
            {
                e.getEntity().setVelocity(e.getEntity().getVelocity().clone().setY(1.5));
            }
        }.runTaskLater(Spectaculation.getPlugin(), 1);
    }

    @Override
    public double getXPDropped()
    {
        return 4.0;
    }
}
package me.superischroma.spectaculation.entity;

import lombok.Getter;
import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.region.Region;
import me.superischroma.spectaculation.region.RegionType;
import me.superischroma.spectaculation.util.SLog;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EntityPopulator
{
    private static final List<EntityPopulator> POPULATORS = new ArrayList<>();
    public static List<EntityPopulator> getPopulators()
    {
        return POPULATORS;
    }

    private final int amount;
    private final int max;
    private final long delay;
    private final SEntityType type;
    private final Predicate<World> condition;
    @Getter
    private final RegionType regionType;
    private BukkitTask task;
    private final List<SEntity> spawned;

    public EntityPopulator(int amount, int max, long delay, SEntityType type, RegionType regionType, Predicate<World> condition)
    {
        this.amount = amount;
        this.max = max;
        this.delay = delay;
        this.type = type;
        this.regionType = regionType;
        this.spawned = new ArrayList<>();
        this.condition = condition;
        POPULATORS.add(this);
    }

    public EntityPopulator(int amount, int max, long delay, SEntityType type, RegionType regionType)
    {
        this(amount, max, delay, type, regionType, null);
    }

    public void start()
    {
        this.task = new BukkitRunnable()
        {
            public void run()
            {
                spawned.removeIf(sEntity -> sEntity.getEntity().isDead());
                List<Region> regions = Region.getRegionsOfType(regionType);
                if (regions.isEmpty())
                    return;
                if (Region.getPlayersWithinRegionType(regionType).isEmpty())
                {
                    for (SEntity s : spawned)
                        s.remove();
                    spawned.clear();
                    return;
                }
                if (condition != null && !condition.test(SUtil.getRandom(regions).getFirstLocation().getWorld()))
                    return;
                if (spawned.size() >= max)
                    return;
                for (int i = 0; i < amount; i++)
                {
                    Location available;
                    int attempts = 0;
                    do
                    {
                        available = SUtil.getRandom(regions).getRandomAvailableLocation();
                        attempts++;
                    }
                    while (available == null && attempts <= 150);
                    if (available != null)
                        spawned.add(new SEntity(available.clone().add(0.5, 0.0, 0.5), type));
                }
            }
        }.runTaskTimer(Spectaculation.getPlugin(), 0, delay);
    }

    public void stop()
    {
        if (this.task == null) return;
        this.task.cancel();
    }

    public static void stopAll()
    {
        for (EntityPopulator populator : POPULATORS)
            populator.stop();
    }
}
package me.superischroma.spectaculation.entity;

import lombok.Getter;
import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.config.Config;
import me.superischroma.spectaculation.util.SLog;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class EntitySpawner
{
    private static final List<EntitySpawner> SPAWNER_CACHE = new ArrayList<>();
    private static BukkitTask SPAWNER_TASK;

    @Getter
    private final UUID uuid;
    @Getter
    private final SEntityType type;
    @Getter
    private final Location location;
    private SEntity active;

    private EntitySpawner(UUID uuid, SEntityType type, Location location)
    {
        this.uuid = uuid;
        this.type = type;
        this.location = location;
        save();
    }

    public EntitySpawner(SEntityType type, Location location)
    {
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.location = location;
        SPAWNER_CACHE.add(this);
        save();
    }

    public void delete()
    {
        Config spawners = Spectaculation.getPlugin().spawners;
        SPAWNER_CACHE.remove(this);
        spawners.set(uuid.toString(), null);
        spawners.save();
    }

    public void save()
    {
        Config spawners = Spectaculation.getPlugin().spawners;
        spawners.set(uuid.toString() + ".type", type.name());
        spawners.set(uuid.toString() + ".location", location);
        spawners.save();
    }

    @Override
    public String toString()
    {
        return "EntitySpawner{uuid=" + uuid.toString() + ", type=" + type.name() + ", location=" + location.toString() + "}";
    }

    public static EntitySpawner deserialize(String key)
    {
        Config spawners = Spectaculation.getPlugin().spawners;
        return new EntitySpawner(UUID.fromString(key),
                SEntityType.getEntityType(spawners.getString(key + ".type")),
                (Location) spawners.get(key + ".location"));
    }

    public static List<EntitySpawner> getSpawners()
    {
        if (SPAWNER_CACHE.size() == 0)
        {
            Config spawners = Spectaculation.getPlugin().spawners;
            for (String key : spawners.getKeys(false))
                SPAWNER_CACHE.add(EntitySpawner.deserialize(key));
        }
        return SPAWNER_CACHE;
    }

    public static void startSpawnerTask()
    {
        if (SPAWNER_TASK != null)
            return;
        SPAWNER_TASK = Spectaculation.getPlugin().getServer().getScheduler().runTaskTimer(Spectaculation.getPlugin(), () ->
        {
            List<Location> locations = new ArrayList<>(Bukkit.getOnlinePlayers().size());
            for (Player player : Bukkit.getOnlinePlayers())
                locations.add(player.getLocation());
            for (EntitySpawner spawner : getSpawners())
            {
                boolean sp = false;
                for (Location location : locations)
                {
                    if (!location.getWorld().getUID().equals(spawner.location.getWorld().getUID()))
                        continue;
                    if (location.distance(spawner.location) <= 60.0D)
                    {
                        sp = true;
                        break;
                    }
                }
                if (!sp)
                {
                    if (spawner.active != null && !spawner.active.getEntity().isDead())
                        spawner.active.remove();
                    continue;
                }
                if (spawner.active == null || spawner.active.getEntity().isDead())
                {
                    spawner.active = new SEntity(spawner.location, spawner.type);
                    spawner.active.getEntity().setRemoveWhenFarAway(true);
                }
            }
        }, 0, 400);
    }

    public static void stopSpawnerTask()
    {
        SPAWNER_TASK.cancel();
        SPAWNER_TASK = null;
    }
}
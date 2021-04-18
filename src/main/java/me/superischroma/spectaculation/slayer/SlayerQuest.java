package me.superischroma.spectaculation.slayer;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.Getter;
import lombok.Setter;
import me.superischroma.spectaculation.sequence.SoundSequenceType;
import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.entity.SEntity;
import me.superischroma.spectaculation.entity.SEntityType;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

@Getter
public class SlayerQuest implements ConfigurationSerializable
{
    private final SlayerBossType type;
    private final long started;
    @Setter
    private double xp;
    @Setter
    private long spawned;
    @Setter
    private long killed;
    @Setter
    private long died;
    @Setter
    private SEntityType lastKilled;
    @Setter
    private SEntity entity;

    public SlayerQuest(SlayerBossType type, long started)
    {
        this.type = type;
        this.started = started;
        this.entity = null;
    }

    private SlayerQuest(SlayerBossType type, long started, double xp, long spawned, long killed, long died, SEntityType lastKilled)
    {
        this.type = type;
        this.started = started;
        this.xp = xp;
        this.spawned = spawned;
        this.killed = killed;
        this.died = died;
        this.lastKilled = lastKilled;
        this.entity = null;
    }

    @Override
    public Map<String, Object> serialize()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type.getNamespace());
        map.put("started", started);
        map.put("xp", xp);
        map.put("spawned", spawned);
        map.put("killed", killed);
        map.put("died", died);
        map.put("lastKilled", lastKilled.name());
        return map;
    }

    public static SlayerQuest deserialize(Map<String, Object> map)
    {
        return new SlayerQuest(SlayerBossType.getByNamespace(String.valueOf(map.get("type"))),
                ((Number) map.get("started")).longValue(),
                ((Number) map.get("xp")).doubleValue(),
                ((Number) map.get("spawned")).longValue(),
                ((Number) map.get("killed")).longValue(),
                ((Number) map.get("died")).longValue(),
                SEntityType.valueOf(String.valueOf(map.get("lastKilled"))));
    }

    public static void playMinibossSpawn(Location location, Entity sound)
    {
        Location clone = location.clone();
        World world = location.getWorld();
        if (sound != null)
            SoundSequenceType.SLAYER_MINIBOSS_SPAWN.play(sound);
        else
            SoundSequenceType.SLAYER_MINIBOSS_SPAWN.play(clone);
        AtomicDouble additive = new AtomicDouble();
        SUtil.runIntervalForTicks(() ->
                world.spigot().playEffect(clone.clone().add(0.0, additive.getAndAdd(0.5), 0.0), Effect.EXPLOSION_LARGE, 1,
                        0, 0.0f, 0.0f, 0.0f, 0.0f, 1, 16), 3, 12);
    }

    public static void playBossSpawn(Location location, Entity sound)
    {
        Location clone = location.clone();
        World world = location.getWorld();
        if (sound != null)
            SoundSequenceType.SLAYER_BOSS_SPAWN.play(sound);
        else
            SoundSequenceType.SLAYER_BOSS_SPAWN.play(clone);
        SUtil.runIntervalForTicks(() ->
        {
            for (int i = 0; i < 50; i++)
            {
                world.playEffect(clone, Effect.SPELL, Effect.SPELL.getData());
                world.playEffect(clone, Effect.FLYING_GLYPH, Effect.FLYING_GLYPH.getData());
                world.playEffect(clone, Effect.WITCH_MAGIC, Effect.WITCH_MAGIC.getData());
            }
        }, 5, 28);
        new BukkitRunnable()
        {
            public void run()
            {
                world.playEffect(clone, Effect.EXPLOSION_HUGE, Effect.EXPLOSION_HUGE.getData());
            }
        }.runTaskLater(Spectaculation.getPlugin(), 28);
    }
}
package me.superischroma.spectaculation.sequence;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class SoundSequenceType
{
    private static final List<SoundSequenceType> TYPES = new ArrayList<>();

    public static final SoundSequenceType MADDOX_BATPHONE = new SoundSequenceType("maddox_batphone", new SoundSequence(
            new SoundSequence.DelayedSound(Sound.NOTE_PLING, 1f, 26f, 1),
            new SoundSequence.DelayedSound(Sound.NOTE_PLING, 1f, 26f, 3),
            new SoundSequence.DelayedSound(Sound.NOTE_PLING, 1f, 26f, 5),
            new SoundSequence.DelayedSound(Sound.NOTE_PLING, 1f, 26f, 7),
            new SoundSequence.DelayedSound(Sound.NOTE_PLING, 1f, 26f, 9),
            new SoundSequence.DelayedSound(Sound.NOTE_PLING, 1f, 26f, 18),
            new SoundSequence.DelayedSound(Sound.NOTE_PLING, 1f, 26f, 20),
            new SoundSequence.DelayedSound(Sound.NOTE_PLING, 1f, 26f, 22),
            new SoundSequence.DelayedSound(Sound.NOTE_PLING, 1f, 26f, 24),
            new SoundSequence.DelayedSound(Sound.NOTE_PLING, 1f, 26f, 26),
            new SoundSequence.DelayedSound(Sound.NOTE_PLING, 1f, 26f, 35),
            new SoundSequence.DelayedSound(Sound.NOTE_PLING, 1f, 26f, 37),
            new SoundSequence.DelayedSound(Sound.NOTE_PLING, 1f, 26f, 39),
            new SoundSequence.DelayedSound(Sound.NOTE_PLING, 1f, 26f, 41),
            new SoundSequence.DelayedSound(Sound.NOTE_PLING, 1f, 26f, 43),
            new SoundSequence.DelayedSound(Sound.WOOD_CLICK, 1f, 1f, 52)
    ));

    public static final SoundSequenceType SLAYER_BOSS_SPAWN = new SoundSequenceType("slayer_boss_spawn", new SoundSequence(
            new SoundSequence.DelayedSound(Sound.WITHER_SHOOT, 1f, 9f, 1),
            new SoundSequence.DelayedSound(Sound.WITHER_SHOOT, 1f, 9f, 4),
            new SoundSequence.DelayedSound(Sound.WITHER_SHOOT, 1f, 5f, 7),
            new SoundSequence.DelayedSound(Sound.WITHER_SHOOT, 1f, 5f, 10),
            new SoundSequence.DelayedSound(Sound.WITHER_SHOOT, 1f, 5f, 13),
            new SoundSequence.DelayedSound(Sound.WITHER_SHOOT, 1f, 5f, 16),
            new SoundSequence.DelayedSound(Sound.WITHER_SHOOT, 1f, 1f, 19),
            new SoundSequence.DelayedSound(Sound.WITHER_SHOOT, 1f, 1f, 22),
            new SoundSequence.DelayedSound(Sound.WITHER_SHOOT, 1f, 1f, 25),
            new SoundSequence.DelayedSound(Sound.WITHER_SPAWN, 1f, -25f, 28),
            new SoundSequence.DelayedSound(Sound.EXPLODE, 1f, 2f, 28)
    ));

    public static final SoundSequenceType SLAYER_MINIBOSS_SPAWN = new SoundSequenceType("slayer_miniboss_spawn", new SoundSequence(
            new SoundSequence.DelayedSound(Sound.EXPLODE, 1f, 2f, 0),
            new SoundSequence.DelayedSound(Sound.EXPLODE, 1f, 2f, 3),
            new SoundSequence.DelayedSound(Sound.EXPLODE, 1f, 2f, 6),
            new SoundSequence.DelayedSound(Sound.EXPLODE, 1f, 2f, 9),
            new SoundSequence.DelayedSound(Sound.EXPLODE, 1f, 2f, 12)
    ));

    private final String namespace;
    private final SoundSequence sequence;

    public SoundSequenceType(String namespace, SoundSequence sequence)
    {
        this.namespace = namespace;
        this.sequence = sequence;
        TYPES.add(this);
    }

    public String getNamespace()
    {
        return namespace;
    }

    public SoundSequence getSequence()
    {
        return sequence;
    }

    public void play(Location location)
    {
        sequence.play(location);
    }

    public void play(Entity entity)
    {
        sequence.play(entity);
    }

    public static SoundSequenceType getByNamespace(String namespace)
    {
        for (SoundSequenceType type : TYPES)
        {
            if (namespace.toLowerCase().equals(type.namespace.toLowerCase()))
                return type;
        }
        return null;
    }
}
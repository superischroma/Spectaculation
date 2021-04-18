package me.superischroma.spectaculation.sequence;

import me.superischroma.spectaculation.Spectaculation;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class SoundSequence implements Sequence
{
    private final List<DelayedSound> sounds;

    public SoundSequence(DelayedSound... sounds)
    {
        this.sounds = Arrays.asList(sounds);
    }

    public void append(DelayedSound sound)
    {
        sounds.add(sound);
    }

    public void play(Location location)
    {
        for (DelayedSound sound : sounds)
            sound.play(location);
    }

    public void play(Entity entity)
    {
        for (DelayedSound sound : sounds)
            sound.play(entity);
    }

    public static class DelayedSound
    {
        private final Sound sound;
        private final float volume;
        private final float pitch;
        private final long delay;

        public DelayedSound(Sound sound, float volume, float pitch, long delay)
        {
            this.sound = sound;
            this.volume = volume;
            this.pitch = pitch;
            this.delay = delay;
        }

        public DelayedSound(Sound sound, float volume, float pitch)
        {
            this(sound, volume, pitch, 0);
        }

        public void play(Location location)
        {
            new BukkitRunnable()
            {
                public void run()
                {
                    location.getWorld().playSound(location, sound, volume, pitch);
                }
            }.runTaskLater(Spectaculation.getPlugin(), delay);
        }

        public void play(Entity entity)
        {
            play(entity.getLocation());
        }
    }
}
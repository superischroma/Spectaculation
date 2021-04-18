package me.superischroma.spectaculation.sequence;

import me.superischroma.spectaculation.Spectaculation;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class AnimationSequence implements Sequence
{
    private final List<DelayedAnimation> animations;

    public AnimationSequence(DelayedAnimation... animations)
    {
        this.animations = Arrays.asList(animations);
    }

    public void append(DelayedAnimation sound)
    {
        animations.add(sound);
    }

    public void play(Location location)
    {
        for (DelayedAnimation animation : animations)
            animation.play(location);
    }

    public void play(Entity entity)
    {
        for (DelayedAnimation animation : animations)
            animation.play(entity);
    }

    public static class DelayedAnimation
    {
        private final Effect effect;
        private final int data;
        private final long delay;
        private final float speed;
        private final int particleCount;

        public DelayedAnimation(Effect effect, int data, long delay, float speed, int particleCount)
        {
            this.effect = effect;
            this.data = data;
            this.delay = delay;
            this.speed = speed;
            this.particleCount = particleCount;
        }

        public void play(Location location)
        {
            new BukkitRunnable()
            {
                public void run()
                {
                    location.getWorld().spigot().playEffect(location, effect, 1, data, 0.0f, 0.0f, 0.0f, speed, particleCount, 16);
                }
            }.runTaskLater(Spectaculation.getPlugin(), delay);
        }

        public void play(Entity entity)
        {
            play(entity.getLocation());
        }
    }
}

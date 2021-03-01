package me.superischroma.spectaculation.entity.wolf;

import me.superischroma.spectaculation.entity.Ageable;
import me.superischroma.spectaculation.entity.EntityStatistics;

public interface WolfStatistics extends EntityStatistics, Ageable
{
    default boolean isAngry()
    {
        return false;
    }
}
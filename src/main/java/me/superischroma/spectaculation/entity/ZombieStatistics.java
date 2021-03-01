package me.superischroma.spectaculation.entity;

public interface ZombieStatistics extends EntityStatistics, Ageable
{
    default boolean isVillager()
    {
        return false;
    }
}
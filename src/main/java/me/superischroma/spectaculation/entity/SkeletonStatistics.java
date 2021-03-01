package me.superischroma.spectaculation.entity;

public interface SkeletonStatistics extends EntityStatistics
{
    default boolean isWither()
    {
        return false;
    }
}
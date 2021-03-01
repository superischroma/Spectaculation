package me.superischroma.spectaculation.entity;

public interface Ageable
{
    default boolean isBaby()
    {
        return false;
    }
}
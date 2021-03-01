package me.superischroma.spectaculation.entity.end;

import me.superischroma.spectaculation.entity.EntityFunction;

public class Enderman implements EndermanStatistics, EntityFunction
{
    @Override
    public String getEntityName()
    {
        return "Enderman";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 6000.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 600.0;
    }

    @Override
    public double getXPDropped()
    {
        return 32.0;
    }
}
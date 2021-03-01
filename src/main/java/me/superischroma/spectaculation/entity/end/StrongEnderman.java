package me.superischroma.spectaculation.entity.end;

import me.superischroma.spectaculation.entity.EntityFunction;

public class StrongEnderman implements EndermanStatistics, EntityFunction
{
    @Override
    public String getEntityName()
    {
        return "Enderman";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 9000.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 700.0;
    }

    @Override
    public double getXPDropped()
    {
        return 36.0;
    }
}
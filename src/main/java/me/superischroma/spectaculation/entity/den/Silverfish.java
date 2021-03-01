package me.superischroma.spectaculation.entity.den;

import me.superischroma.spectaculation.entity.EntityFunction;
import me.superischroma.spectaculation.entity.EntityStatistics;

public class Silverfish implements EntityStatistics, EntityFunction
{
    @Override
    public String getEntityName()
    {
        return "Silverfish";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 50.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 20.0;
    }

    @Override
    public double getXPDropped()
    {
        return 5.4;
    }
}
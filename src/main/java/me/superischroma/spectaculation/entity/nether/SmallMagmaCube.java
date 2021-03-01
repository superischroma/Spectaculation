package me.superischroma.spectaculation.entity.nether;

import me.superischroma.spectaculation.entity.EntityFunction;
import me.superischroma.spectaculation.entity.SlimeStatistics;

public class SmallMagmaCube implements SlimeStatistics, EntityFunction
{
    @Override
    public String getEntityName()
    {
        return "Magma Cube";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 200.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 70.0;
    }

    @Override
    public double getXPDropped()
    {
        return 4.0;
    }

    @Override
    public int getSize()
    {
        return 4;
    }

    @Override
    public boolean split()
    {
        return false;
    }
}
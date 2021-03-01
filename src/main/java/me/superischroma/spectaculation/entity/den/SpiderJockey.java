package me.superischroma.spectaculation.entity.den;

import me.superischroma.spectaculation.entity.EntityFunction;
import me.superischroma.spectaculation.entity.EntityStatistics;
import me.superischroma.spectaculation.entity.JockeyStatistics;
import me.superischroma.spectaculation.entity.SEntityType;

public class SpiderJockey extends BaseSpider implements JockeyStatistics
{
    @Override
    public SEntityType getPassenger()
    {
        return SEntityType.JOCKEY_SKELETON;
    }

    @Override
    public String getEntityName()
    {
        return "Spider";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 180.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 30.0;
    }

    @Override
    public double getXPDropped()
    {
        return 8.0;
    }

    public static class JockeySkeleton implements EntityStatistics, EntityFunction
    {
        @Override
        public String getEntityName()
        {
            return "Jockey Skeleton";
        }

        @Override
        public double getEntityMaxHealth()
        {
            return 250.0;
        }

        @Override
        public double getDamageDealt()
        {
            return 38.0;
        }

        @Override
        public double getXPDropped()
        {
            return 6.0;
        }
    }
}
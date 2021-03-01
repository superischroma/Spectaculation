package me.superischroma.spectaculation.entity.den;

public class WeaverSpider extends BaseSpider
{
    @Override
    public String getEntityName()
    {
        return "Splitter Spider";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 160.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 35.0;
    }

    @Override
    public double getXPDropped()
    {
        return 9.7;
    }

    public static class Strong extends BaseSpider
    {
        @Override
        public String getEntityName()
        {
            return "Splitter Spider";
        }

        @Override
        public double getEntityMaxHealth()
        {
            return 200.0;
        }

        @Override
        public double getDamageDealt()
        {
            return 45.0;
        }

        @Override
        public double getXPDropped()
        {
            return 8.0;
        }
    }
}
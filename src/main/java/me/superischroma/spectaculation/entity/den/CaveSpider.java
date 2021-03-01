package me.superischroma.spectaculation.entity.den;

public class CaveSpider extends BaseSpider
{
    @Override
    public String getEntityName()
    {
        return "Cave Spider";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 12.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 5.0;
    }

    @Override
    public double getXPDropped()
    {
        return 5.7;
    }
}
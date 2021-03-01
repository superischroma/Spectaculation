package me.superischroma.spectaculation.entity.den;

public class VoraciousSpider extends BaseSpider
{
    @Override
    public String getEntityName()
    {
        return "Voracious Spider";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 1000.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 100.0;
    }

    @Override
    public double getXPDropped()
    {
        return 10.8;
    }
}
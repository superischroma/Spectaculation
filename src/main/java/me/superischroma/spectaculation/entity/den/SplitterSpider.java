package me.superischroma.spectaculation.entity.den;

import me.superischroma.spectaculation.entity.SEntity;
import me.superischroma.spectaculation.entity.SEntityType;
import org.bukkit.entity.Entity;

public class SplitterSpider extends BaseSpider
{
    @Override
    public String getEntityName()
    {
        return "Splitter Spider";
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
        return 9.7;
    }

    @Override
    public void onDeath(SEntity sEntity, Entity killed, Entity damager)
    {
        super.onDeath(sEntity, killed, damager);
        for (int i = 0; i < 2; i++)
            new SEntity(sEntity.getEntity(), SEntityType.SILVERFISH);
    }
}
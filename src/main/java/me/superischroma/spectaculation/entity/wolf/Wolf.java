package me.superischroma.spectaculation.entity.wolf;

import me.superischroma.spectaculation.entity.*;
import me.superischroma.spectaculation.item.SMaterial;

import java.util.Collections;
import java.util.List;

public class Wolf extends BaseWolf
{
    @Override
    public String getEntityName()
    {
        return "Wolf";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 250.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 80.0;
    }

    @Override
    public List<EntityDrop> drops()
    {
        return Collections.singletonList(new EntityDrop(SMaterial.BONE, EntityDropType.GUARANTEED, 1.0));
    }

    public double getXPDropped()
    {
        return 10.0;
    }

    @Override
    public boolean isAngry()
    {
        return true;
    }
}
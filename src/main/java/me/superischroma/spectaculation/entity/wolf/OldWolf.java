package me.superischroma.spectaculation.entity.wolf;

import me.superischroma.spectaculation.entity.EntityDrop;
import me.superischroma.spectaculation.entity.EntityDropType;
import me.superischroma.spectaculation.entity.EntityFunction;
import me.superischroma.spectaculation.item.SMaterial;

import java.util.Collections;
import java.util.List;

public class OldWolf extends BaseWolf
{
    @Override
    public String getEntityName()
    {
        return "Old Wolf";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 15000.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 720.0;
    }

    @Override
    public List<EntityDrop> drops()
    {
        return Collections.singletonList(new EntityDrop(SMaterial.BONE, EntityDropType.GUARANTEED, 1.0));
    }

    public double getXPDropped()
    {
        return 40.0;
    }

    @Override
    public boolean isAngry()
    {
        return true;
    }
}
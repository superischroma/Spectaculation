package me.superischroma.spectaculation.entity.wolf;

import me.superischroma.spectaculation.entity.EntityDrop;
import me.superischroma.spectaculation.entity.EntityDropType;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.util.SUtil;

import java.util.Collections;
import java.util.List;

public class SvenFollower extends BaseWolf
{
    @Override
    public String getEntityName()
    {
        return "Pack Enforcer";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 120000.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 1100.0;
    }

    @Override
    public List<EntityDrop> drops()
    {
        return Collections.singletonList(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.WOLF_TOOTH).getStack(), 2), EntityDropType.GUARANTEED, 1.0));
    }

    public double getXPDropped()
    {
        return 250.0;
    }

    @Override
    public boolean isAngry()
    {
        return true;
    }
}
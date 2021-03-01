package me.superischroma.spectaculation.entity.wolf;

import me.superischroma.spectaculation.entity.EntityDrop;
import me.superischroma.spectaculation.entity.EntityDropType;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.util.SUtil;

import java.util.Collections;
import java.util.List;

public class SvenAlpha extends BaseWolf
{
    @Override
    public String getEntityName()
    {
        return "Sven Alpha";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 480000.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 1300.0;
    }

    @Override
    public List<EntityDrop> drops()
    {
        return Collections.singletonList(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.WOLF_TOOTH).getStack(), 5), EntityDropType.GUARANTEED, 1.0));
    }

    public double getXPDropped()
    {
        return 500.0;
    }

    @Override
    public boolean isAngry()
    {
        return true;
    }
}
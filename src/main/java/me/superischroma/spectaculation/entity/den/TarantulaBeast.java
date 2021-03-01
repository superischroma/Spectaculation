package me.superischroma.spectaculation.entity.den;

import me.superischroma.spectaculation.entity.EntityDrop;
import me.superischroma.spectaculation.entity.EntityDropType;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.util.SUtil;

import java.util.Collections;
import java.util.List;

public class TarantulaBeast extends BaseSpider
{
    @Override
    public String getEntityName()
    {
        return "Tarantula Beast";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 144000;
    }

    @Override
    public double getDamageDealt()
    {
        return 2500.0;
    }

    @Override
    public double getXPDropped()
    {
        return 300.0;
    }

    @Override
    public List<EntityDrop> drops()
    {
        return Collections.singletonList(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.TARANTULA_WEB).getStack(), 2), EntityDropType.GUARANTEED, 1.0));
    }
}
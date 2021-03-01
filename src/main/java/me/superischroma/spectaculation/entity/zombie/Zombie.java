package me.superischroma.spectaculation.entity.zombie;

import me.superischroma.spectaculation.entity.*;
import me.superischroma.spectaculation.item.SMaterial;

import java.util.Arrays;
import java.util.List;

public class Zombie extends BaseZombie
{
    @Override
    public String getEntityName()
    {
        return "Zombie";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 100.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 20.0;
    }

    @Override
    public List<EntityDrop> drops()
    {
        return Arrays.asList(new EntityDrop(SMaterial.ROTTEN_FLESH, EntityDropType.GUARANTEED, 1.0),
                new EntityDrop(SMaterial.POISONOUS_POTATO, EntityDropType.OCCASIONAL, 0.05),
                new EntityDrop(SMaterial.POTATO_ITEM, EntityDropType.OCCASIONAL, 0.05),
                new EntityDrop(SMaterial.CARROT_ITEM, EntityDropType.OCCASIONAL, 0.05));
    }

    @Override
    public boolean isBaby()
    {
        return false;
    }

    @Override
    public boolean isVillager()
    {
        return false;
    }

    @Override
    public double getXPDropped()
    {
        return 6.0;
    }
}
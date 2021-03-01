package me.superischroma.spectaculation.entity.wolf;

import lombok.Getter;
import me.superischroma.spectaculation.entity.SEntity;
import me.superischroma.spectaculation.entity.nms.SvenPackmaster;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wolf;

public class SvenPup extends BaseWolf
{
    private final double health;
    private final double damage;
    private final CraftPlayer target;
    @Getter
    private final SvenPackmaster parent;

    public SvenPup(Double health, Double damage, CraftPlayer target, SvenPackmaster parent)
    {
        this.health = health;
        this.damage = damage;
        this.target = target;
        this.parent = parent;
    }

    @Override
    public String getEntityName()
    {
        return "Sven Pup";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return health;
    }

    @Override
    public double getDamageDealt()
    {
        return damage;
    }

    @Override
    public double getXPDropped()
    {
        return 0.0;
    }

    @Override
    public boolean isAngry()
    {
        return true;
    }

    public boolean isBaby()
    {
        return true;
    }

    @Override
    public void onSpawn(LivingEntity entity, SEntity sEntity)
    {
        ((Wolf) entity).setTarget(target);
    }

    @Override
    public void onDeath(SEntity sEntity, Entity killed, Entity damager)
    {
        parent.getPups().remove(sEntity);
    }
}
package me.superischroma.spectaculation.entity;

import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import java.util.ArrayList;
import java.util.List;

public interface EntityFunction
{
    default void onDeath(SEntity sEntity, Entity killed, Entity damager) {}
    default void onDamage(SEntity sEntity, Entity damager, EntityDamageByEntityEvent e, AtomicDouble damage) {}
    default List<EntityDrop> drops()
    {
        return new ArrayList<>();
    }
    default boolean tick(LivingEntity entity)
    {
        return false;
    }
    default void onSpawn(LivingEntity entity, SEntity sEntity) {}
    default void onAttack(EntityDamageByEntityEvent e) {}
    default void onTarget(SEntity sEntity, EntityTargetLivingEntityEvent e) {}
}
package me.superischroma.spectaculation.item.pet;

import com.google.common.util.concurrent.AtomicDouble;
import me.superischroma.spectaculation.item.SItem;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

public interface PetAbility
{
    String getName();
    List<String> getDescription(SItem instance);
    default void onHurt(EntityDamageByEntityEvent e, Entity damager) {}
    default void onDamage(EntityDamageByEntityEvent e) {}
    default void onZealotAttempt(AtomicDouble chance) {}
}
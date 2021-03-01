package me.superischroma.spectaculation.item;

import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface MaterialFunction
{
    default void onInteraction(PlayerInteractEvent e) {}
    default void onInventoryClick(SItem instance, InventoryClickEvent e) {}
    default void onDamage(Entity damaged, Player damager, AtomicDouble damage, SItem item) {}
    default void onKill(Entity damaged, Player damager, SItem item) {}
    default void whileHolding(Player holding) {}
    default void onInstanceUpdate(SItem instance) {}
}
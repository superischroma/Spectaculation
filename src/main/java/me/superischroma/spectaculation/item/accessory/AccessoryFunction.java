package me.superischroma.spectaculation.item.accessory;

import com.google.common.util.concurrent.AtomicDouble;
import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.SItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface AccessoryFunction extends MaterialFunction
{
    default void onDamageInInventory(SItem weapon, Player damager, Entity damaged, SItem accessory, AtomicDouble damage) {}
    default void update(SItem instance, Player player, int accessorySlot) {}
}
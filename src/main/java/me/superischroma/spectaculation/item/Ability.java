package me.superischroma.spectaculation.item;

import org.bukkit.entity.Player;

public interface Ability
{
    String getAbilityName();
    String getAbilityDescription();
    default void onAbilityUse(Player player, SItem sItem) {}
    int getAbilityCooldownTicks();
    int getManaCost();
    default AbilityActivation getAbilityActivation()
    {
        return AbilityActivation.RIGHT_CLICK;
    }
    default boolean displayUsage()
    {
        return true;
    }
}
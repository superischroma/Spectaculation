package me.superischroma.spectaculation.item.bow;

import me.superischroma.spectaculation.item.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EndStoneBow implements ToolStatistics, MaterialFunction, Ability
{
    @Override
    public String getAbilityName()
    {
        return "Extreme Focus";
    }

    @Override
    public String getAbilityDescription()
    {
        return "Consumes all your Mana, and your next hit will deal that much more Damage!";
    }

    @Override
    public void onAbilityUse(Player player, SItem sItem)
    {
        player.sendMessage(ChatColor.GRAY + "Incomplete ability.");
    }

    @Override
    public int getAbilityCooldownTicks()
    {
        return 0;
    }

    @Override
    public int getManaCost()
    {
        return -1;
    }

    // TODO: TEMPORARY
    @Override
    public AbilityActivation getAbilityActivation()
    {
        return AbilityActivation.NO_ACTIVATION;
    }

    @Override
    public String getDisplayName()
    {
        return "End Stone Bow";
    }

    @Override
    public int getBaseDamage()
    {
        return 140;
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.EPIC;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.RANGED_WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType()
    {
        return SpecificItemType.BOW;
    }
}

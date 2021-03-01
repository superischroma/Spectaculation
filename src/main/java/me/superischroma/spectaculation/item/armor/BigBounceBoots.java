package me.superischroma.spectaculation.item.armor;

import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BigBounceBoots implements LeatherArmorStatistics, TickingMaterial, FlightStatistics, Ability
{
    @Override
    public String getAbilityName()
    {
        return "Big Double Jump";
    }

    @Override
    public String getAbilityDescription()
    {
        return "Allows you to Double Jump high!";
    }

    @Override
    public int getAbilityCooldownTicks()
    {
        return 0;
    }

    @Override
    public AbilityActivation getAbilityActivation()
    {
        return AbilityActivation.FLIGHT;
    }

    @Override
    public void onAbilityUse(Player player, SItem sItem)
    {
        GameMode gameMode = player.getGameMode();
        if (gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR) return;
        player.setVelocity(player.getVelocity().clone().setY(1.5));
    }

    @Override
    public int getManaCost()
    {
        return 50;
    }

    @Override
    public String getDisplayName()
    {
        return "Big Bounce Boots";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.EXCLUSIVE;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ARMOR;
    }

    @Override
    public SpecificItemType getSpecificType()
    {
        return SpecificItemType.BOOTS;
    }

    @Override
    public int getBaseDefense()
    {
        return 75;
    }

    @Override
    public double getBaseSpeed()
    {
        return 0.2;
    }

    @Override
    public int getBaseIntelligence()
    {
        return 100;
    }

    @Override
    public void tick(SItem item, Player owner)
    {
        SUtil.toggleAllowFlightNoCreative(owner.getUniqueId(), owner.getLocation().clone().subtract(0, 0.2, 0).getBlock().getType() == Material.AIR);
    }

    @Override
    public boolean enableFlight()
    {
        return true;
    }

    @Override
    public int getColor()
    {
        return 0x91B2E3;
    }
}
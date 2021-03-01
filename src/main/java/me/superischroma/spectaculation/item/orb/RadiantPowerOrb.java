package me.superischroma.spectaculation.item.orb;

import me.superischroma.spectaculation.item.Rarity;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RadiantPowerOrb extends PowerOrb
{
    @Override
    public String getAbilityDescription()
    {
        return "Place an orb for 30s buffing up to 5 players within 18 blocks. Costs 50% of max mana. Only one orb applies per player.";
    }

    @Override
    public String getURL()
    {
        return "7ab4c4d6ee69bc24bba2b8faf67b9f704a06b01aa93f3efa6aef7a9696c4feef";
    }

    @Override
    public String getDisplayName()
    {
        return "Radiant Power Orb";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.UNCOMMON;
    }

    @Override
    public String getBuffName()
    {
        return "Radiant";
    }

    @Override
    public String getBuffDescription()
    {
        return "Heals 1% of max ❤ per second.";
    }

    @Override
    protected void buff(Player player)
    {
        player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + (player.getMaxHealth() * 0.01)));
    }

    @Override
    protected long getOrbLifeTicks()
    {
        return 30 * 20;
    }

    @Override
    protected void playEffect(Location location)
    {
        location.getWorld().playEffect(location, Effect.HAPPY_VILLAGER, Effect.HAPPY_VILLAGER.getData());
    }
}
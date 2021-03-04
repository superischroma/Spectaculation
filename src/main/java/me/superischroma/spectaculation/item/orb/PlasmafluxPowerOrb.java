package me.superischroma.spectaculation.item.orb;

import me.superischroma.spectaculation.item.PlayerBoostStatistics;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.user.PlayerStatistics;
import me.superischroma.spectaculation.user.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlasmafluxPowerOrb extends PowerOrb
{
    @Override
    public String getAbilityDescription()
    {
        return "Place an orb for 60s buffing up to 5 players within 20 blocks. Costs 50% of max mana. Only one orb applies per player.";
    }

    @Override
    public String getURL()
    {
        return "83ed4ce23933e66e04df16070644f7599eeb55307f7eafe8d92f40fb3520863c";
    }

    @Override
    public String getDisplayName()
    {
        return "Plasmaflux Power Orb";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.LEGENDARY;
    }

    @Override
    public String getBuffName()
    {
        return "Plasmaflux";
    }

    @Override
    public String getBuffDescription()
    {
        return "Grants +125% base mana regen. Heals 3% of max ❤ per second. Increases all heals by +7.5%. Grants +35 Strength";
    }

    @Override
    public String getCustomOrbName()
    {
        return "" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Plasmaflux";
    }

    @Override
    protected void buff(Player player)
    {
        player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + (player.getMaxHealth() * 0.03)));
        PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
        PlayerUtils.boostPlayer(statistics, new PlayerBoostStatistics()
        {
            @Override
            public String getDisplayName()
            {
                return null;
            }
            @Override
            public Rarity getRarity()
            {
                return null;
            }
            @Override
            public GenericItemType getType()
            {
                return null;
            }
            @Override
            public double getBaseStrength()
            {
                return 35;
            }
        }, 20);
        statistics.boostManaRegeneration(1.25, 20);
        statistics.boostHealthRegeneration(0.075, 20);
    }

    @Override
    protected long getOrbLifeTicks()
    {
        return 60 * 20;
    }

    @Override
    protected void playEffect(Location location)
    {
        location.getWorld().spigot().playEffect(location, Effect.COLOURED_DUST, 0, 1,
                72.0f / 255.0f, 2.0f / 255.0f, 102.0f / 255.0f, 1, 0, 64);
    }
}
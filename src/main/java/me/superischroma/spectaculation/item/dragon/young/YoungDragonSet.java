package me.superischroma.spectaculation.item.dragon.young;

import me.superischroma.spectaculation.item.MaterialStatistics;
import me.superischroma.spectaculation.item.PlayerBoostStatistics;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.armor.ArmorSet;
import org.bukkit.entity.Player;

public class YoungDragonSet implements ArmorSet
{
    @Override
    public String getName()
    {
        return "Young Blood";
    }

    @Override
    public String getDescription()
    {
        return "Gain +70 Speed when you are above 50% Health.";
    }

    @Override
    public Class<? extends MaterialStatistics> getHelmet()
    {
        return YoungDragonHelmet.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getChestplate()
    {
        return YoungDragonChestplate.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getLeggings()
    {
        return YoungDragonLeggings.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getBoots()
    {
        return YoungDragonBoots.class;
    }

    @Override
    public PlayerBoostStatistics whileHasFullSet(Player player)
    {
        return new PlayerBoostStatistics()
        {
            @Override
            public double getBaseSpeed()
            {
                if (player.getHealth() > player.getMaxHealth() / 2.0)
                    return 0.7;
                return 0.0;
            }

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
            public String getLore()
            {
                return null;
            }

            @Override
            public GenericItemType getType()
            {
                return null;
            }
        };
    }
}
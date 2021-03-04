package me.superischroma.spectaculation.item.dragon.superior;

import me.superischroma.spectaculation.item.MaterialStatistics;
import me.superischroma.spectaculation.item.PlayerBoostStatistics;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.armor.ArmorSet;
import org.bukkit.entity.Player;

public class SuperiorDragonSet implements ArmorSet
{
    @Override
    public String getName()
    {
        return "Superior Blood";
    }

    @Override
    public String getDescription()
    {
        return "All your stats are increased by 5% and Aspect of the Dragons ability deals 50% more Ability Damage.";
    }

    @Override
    public Class<? extends MaterialStatistics> getHelmet()
    {
        return SuperiorDragonHelmet.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getChestplate()
    {
        return SuperiorDragonChestplate.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getLeggings()
    {
        return SuperiorDragonLeggings.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getBoots()
    {
        return SuperiorDragonBoots.class;
    }

    @Override
    public PlayerBoostStatistics whileHasFullSet(Player player)
    {
        return new PlayerBoostStatistics()
        {
            @Override
            public double getBaseSpeed()
            {
                return 0.006;
            }

            @Override
            public double getBaseHealth()
            {
                return 23;
            }

            @Override
            public double getBaseStrength()
            {
                return 2;
            }

            @Override
            public double getBaseCritDamage()
            {
                return 0.02;
            }

            @Override
            public double getBaseCritChance()
            {
                return 0.004;
            }

            @Override
            public double getBaseIntelligence()
            {
                return 5;
            }

            @Override
            public double getBaseDefense()
            {
                return 30;
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
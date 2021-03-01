package me.superischroma.spectaculation.item.dragon.old;

import me.superischroma.spectaculation.item.MaterialStatistics;
import me.superischroma.spectaculation.item.PlayerBoostStatistics;
import me.superischroma.spectaculation.item.armor.ArmorSet;
import org.bukkit.entity.Player;

public class OldDragonSet implements ArmorSet
{
    @Override
    public String getName()
    {
        return "Old Blood";
    }

    @Override
    public String getDescription()
    {
        return "Increases the strength of Growth, Protection, Feather Falling, Sugar Rush, and True Protection enchantments while worn.";
    }

    @Override
    public Class<? extends MaterialStatistics> getHelmet()
    {
        return OldDragonHelmet.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getChestplate()
    {
        return OldDragonChestplate.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getLeggings()
    {
        return OldDragonLeggings.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getBoots()
    {
        return OldDragonBoots.class;
    }

    @Override
    public PlayerBoostStatistics whileHasFullSet(Player player)
    {
        return null; // TODO
    }
}
package me.superischroma.spectaculation.item.armor;

import me.superischroma.spectaculation.item.MaterialStatistics;
import me.superischroma.spectaculation.item.PlayerBoostStatistics;
import org.bukkit.entity.Player;

public interface ArmorSet
{
    String getName();
    String getDescription();
    Class<? extends MaterialStatistics> getHelmet();
    Class<? extends MaterialStatistics> getChestplate();
    Class<? extends MaterialStatistics> getLeggings();
    Class<? extends MaterialStatistics> getBoots();
    default PlayerBoostStatistics whileHasFullSet(Player player)
    {
        return null;
    }
}
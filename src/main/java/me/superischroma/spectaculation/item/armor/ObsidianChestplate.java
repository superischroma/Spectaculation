package me.superischroma.spectaculation.item.armor;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.user.PlayerStatistic;
import me.superischroma.spectaculation.user.PlayerStatistics;
import me.superischroma.spectaculation.user.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class ObsidianChestplate implements LeatherArmorStatistics, TickingMaterial
{
    @Override
    public int getColor()
    {
        return 0x000000;
    }

    @Override
    public String getDisplayName()
    {
        return "Obsidian Chestplate";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.EPIC;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ARMOR;
    }

    @Override
    public SpecificItemType getSpecificType()
    {
        return SpecificItemType.CHESTPLATE;
    }

    @Override
    public int getBaseDefense()
    {
        return 250;
    }

    @Override
    public void tick(SItem item, Player owner)
    {
       PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(owner.getUniqueId());
       statistics.getSpeed().zero(PlayerStatistic.OBSIDIAN_CHESTPLATE);
       int obsidian = 0;
       for (Map.Entry<Integer, ? extends ItemStack> entry : owner.getInventory().all(Material.OBSIDIAN).entrySet())
           obsidian += entry.getValue().getAmount();
       statistics.getSpeed().add(PlayerStatistic.OBSIDIAN_CHESTPLATE, ((obsidian / 20.0) / 100.0));
       new BukkitRunnable()
       {
           public void run()
           {
               SItem chestplate = SItem.find(owner.getInventory().getChestplate());
               if (chestplate != null && chestplate.getType() == SMaterial.OBSIDIAN_CHESTPLATE)
                   return;
               statistics.getSpeed().zero(PlayerStatistic.OBSIDIAN_CHESTPLATE);
           }
       }.runTaskLater(Spectaculation.getPlugin(), 13);
    }

    @Override
    public long getInterval()
    {
        return 10;
    }
}
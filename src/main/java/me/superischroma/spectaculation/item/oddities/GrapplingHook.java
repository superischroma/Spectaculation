package me.superischroma.spectaculation.item.oddities;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.item.*;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GrapplingHook implements ToolStatistics, FishingRodFunction
{
    private static final List<UUID> COOLDOWN = new ArrayList<>();

    @Override
    public void onFish(SItem instance, PlayerFishEvent e)
    {
        PlayerFishEvent.State state = e.getState();
        if (state != PlayerFishEvent.State.FAILED_ATTEMPT && state != PlayerFishEvent.State.IN_GROUND) return;
        Player player = e.getPlayer();
        if (COOLDOWN.contains(player.getUniqueId()))
        {
            player.sendMessage(ChatColor.RED + "Whow! Slow down there!");
            return;
        }
        player.setVelocity(player.getLocation().toVector().subtract(e.getHook().getLocation().toVector()).multiply(-1.0).multiply(0.5).setY(0.9));
        if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR)
        {
            COOLDOWN.add(player.getUniqueId());
            new BukkitRunnable()
            {
                public void run()
                {
                    COOLDOWN.remove(player.getUniqueId());
                }
            }.runTaskLater(Spectaculation.getPlugin(), 40);
        }
    }

    @Override
    public String getDisplayName()
    {
        return "Grappling Hook";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.UNCOMMON;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ITEM;
    }

    @Override
    public String getLore()
    {
        return "Travel around in style using this Grappling Hook. 2 Second Cooldown";
    }
}
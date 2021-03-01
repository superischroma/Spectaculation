package me.superischroma.spectaculation.item.weapon;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.item.*;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LeapingSword implements ToolStatistics, MaterialFunction, Ability
{
    @Override
    public int getBaseDamage()
    {
        return 150;
    }

    @Override
    public int getBaseStrength()
    {
        return 100;
    }

    @Override
    public double getBaseCritDamage()
    {
        return 0.25;
    }

    @Override
    public String getDisplayName()
    {
        return "Leaping Sword";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.EPIC;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType()
    {
        return SpecificItemType.SWORD;
    }

    @Override
    public String getLore()
    {
        return null;
    }

    @Override
    public String getAbilityName()
    {
        return "Leap";
    }

    @Override
    public String getAbilityDescription()
    {
        return "Leap into the air and deal damage to any nearby enemies upon landing on the ground. Damaged enemies will also be frozen for 1 second.";
    }

    @Override
    public void onAbilityUse(Player player, SItem sItem)
    {
        player.setVelocity(player.getLocation().getDirection().multiply(3).setY(2));
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (player.getLocation().subtract(0, 0.5, 0).getBlock().getType() == Material.AIR) return;
                player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 2f, 1f);
                player.playEffect(player.getLocation(), Effect.EXPLOSION_LARGE, Effect.EXPLOSION_LARGE.getData());
                //for (Entity entity : player.getNearbyEntities(5, 5, 5))
                //    entity.
                this.cancel();
            }
        }.runTaskTimer(Spectaculation.getPlugin(), 10, 2);
    }

    @Override
    public int getAbilityCooldownTicks()
    {
        return 20;
    }

    @Override
    public int getManaCost()
    {
        return 50;
    }
}
package me.superischroma.spectaculation.item.bow;

import com.google.common.util.concurrent.AtomicDouble;
import me.superischroma.spectaculation.util.DefenseReplacement;
import me.superischroma.spectaculation.Repeater;
import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.user.PlayerUtils;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class MosquitoBow implements ToolStatistics, BowFunction, Ability
{
    @Override
    public String getAbilityName()
    {
        return "Nasty Bite";
    }

    @Override
    public String getAbilityDescription()
    {
        return "Costs 11% of max Mana, deals +19% more damage, heal for 2x the Mana cost";
    }

    @Override
    public void onAbilityUse(Player player, SItem sItem)
    {
    }

    @Override
    public int getAbilityCooldownTicks()
    {
        return 0;
    }

    @Override
    public int getManaCost() {
        return 0;
    }

    @Override
    public String getDisplayName()
    {
        return "Mosquito Bow";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.RANGED_WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType()
    {
        return SpecificItemType.BOW;
    }

    @Override
    public AbilityActivation getAbilityActivation()
    {
        return AbilityActivation.NO_ACTIVATION;
    }

    @Override
    public int getBaseDamage()
    {
        return 251;
    }

    @Override
    public double getBaseStrength()
    {
        return 101;
    }

    @Override
    public double getBaseCritDamage()
    {
        return 0.09;
    }

    @Override
    public void onBowShoot(SItem bow, EntityShootBowEvent e)
    {
        Player player = (Player) e.getEntity();
        if (!player.isSneaking()) return;
        if (e.getForce() != 1.0f) return;
        int manaPool = SUtil.blackMagic(PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId()).getIntelligence().addAll() + 100);
        int cost = PlayerUtils.getFinalManaCost(player, bow, (int) (manaPool * 0.11));
        boolean take = PlayerUtils.takeMana(player, cost);
        if (!take)
        {
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, -4f);
            player.sendMessage(ChatColor.RED + "You don't have enough mana!");
            return;
        }
        long c = System.currentTimeMillis();
        Repeater.DEFENSE_REPLACEMENT_MAP.put(player.getUniqueId(), new DefenseReplacement()
        {
            @Override
            public String getReplacement()
            {
                return ChatColor.AQUA + "-" + cost + " Mana (" + ChatColor.GOLD + getAbilityName() + ChatColor.AQUA + ")";
            }

            @Override
            public long getEnd()
            {
                return c + 4000;
            }
        });
        player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + ((manaPool * 0.11) * 2)));
        e.getProjectile().setMetadata("bite", new FixedMetadataValue(Spectaculation.getPlugin(), true));
    }

    @Override
    public void onBowHit(Entity hit, Player shooter, Arrow arrow, SItem weapon, AtomicDouble finalDamage)
    {
        if (!arrow.hasMetadata("bite")) return;
        finalDamage.set(finalDamage.get() + (finalDamage.get() * 0.19));
    }
}
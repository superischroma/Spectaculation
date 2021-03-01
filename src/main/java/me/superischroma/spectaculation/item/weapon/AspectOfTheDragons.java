package me.superischroma.spectaculation.item.weapon;

import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SLog;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.*;

import java.util.List;

public class AspectOfTheDragons implements ToolStatistics, MaterialFunction, Ability
{
    @Override
    public int getBaseDamage()
    {
        return 225;
    }

    @Override
    public int getBaseStrength()
    {
        return 100;
    }

    @Override
    public String getDisplayName()
    {
        return "Aspect of the Dragons";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.LEGENDARY;
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
        return "Dragon Rage";
    }

    @Override
    public String getAbilityDescription()
    {
        return "All Monsters in front of you take 5,000 Ability Damage. Hit monsters take large knockback.";
    }

    @Override
    public void onAbilityUse(Player player, SItem sItem)
    {
        player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 5f, 5f);
        for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation().add(player.getLocation().getDirection().multiply(3.0)), 3.0, 3.0, 3.0))
        {
            if (!(entity instanceof LivingEntity)) continue;
            if (entity instanceof Player || entity instanceof EnderDragon || entity instanceof EnderDragonPart) continue;
            User user = User.getUser(player.getUniqueId());
            entity.setVelocity(player.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(-1.0).multiply(50.0));
            user.damageEntity((LivingEntity) entity, 12000.0);
        }
    }

    @Override
    public int getAbilityCooldownTicks()
    {
        return 100;
    }

    @Override
    public int getManaCost()
    {
        return 100;
    }
}
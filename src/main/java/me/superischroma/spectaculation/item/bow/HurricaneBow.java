package me.superischroma.spectaculation.item.bow;

import com.google.common.util.concurrent.AtomicDouble;
import me.superischroma.spectaculation.item.*;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

public class HurricaneBow implements ToolStatistics, BowFunction, Ability
{
    @Override
    public String getAbilityName()
    {
        return "Tempest";
    }

    @Override
    public String getAbilityDescription()
    {
        return "The more kills you get using this bow the more powerful it becomes! Reach 250 kills to unlock its full potential.";
    }

    @Override
    public int getAbilityCooldownTicks()
    {
        return 0;
    }

    @Override
    public int getManaCost()
    {
        return 0;
    }

    @Override
    public AbilityActivation getAbilityActivation()
    {
        return AbilityActivation.NO_ACTIVATION;
    }

    @Override
    public String getDisplayName()
    {
        return "Hurricane Bow";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.EPIC;
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
    public int getBaseDamage()
    {
        return 120;
    }

    @Override
    public int getBaseStrength()
    {
        return 50;
    }

    @Override
    public boolean displayKills()
    {
        return true;
    }

    @Override
    public void onBowShoot(SItem bow, EntityShootBowEvent e)
    {
        Player shooter = (Player) e.getEntity();
        int kills = bow.getDataInt("kills");
        Location location = shooter.getEyeLocation().add(shooter.getEyeLocation().getDirection().toLocation(shooter.getWorld()));
        float speed = e.getForce() * 3.0f;
        if (kills >= 20)
        {
            Location l = location.clone();
            l.setYaw(location.getYaw() - 15.0f);
            shooter.getWorld().spawnArrow(l, l.getDirection(), speed, 1).setShooter(shooter);
        }
        if (kills >= 50)
        {
            Location l = location.clone();
            l.setYaw(location.getYaw() + 15.0f);
            shooter.getWorld().spawnArrow(l, l.getDirection(), speed, 1).setShooter(shooter);
        }
        if (kills >= 100)
        {
            Location l = location.clone();
            l.setYaw(location.getYaw() - 30.0f);
            shooter.getWorld().spawnArrow(l, l.getDirection(), speed, 1).setShooter(shooter);
        }
        if (kills >= 250)
        {
            Location l = location.clone();
            l.setYaw(location.getYaw() + 30.0f);
            shooter.getWorld().spawnArrow(l, l.getDirection(), speed, 1).setShooter(shooter);
        }
    }

    @Override
    public void onBowHit(Entity hit, Player shooter, Arrow arrow, SItem weapon, AtomicDouble finalDamage)
    {
        if (!(hit instanceof LivingEntity))
            return;
        if (((LivingEntity) hit).getHealth() - finalDamage.get() <= 0.0)
            weapon.setKills(weapon.getDataInt("kills") + 1);
    }
}

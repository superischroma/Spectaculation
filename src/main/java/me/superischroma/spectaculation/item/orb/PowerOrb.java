package me.superischroma.spectaculation.item.orb;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.entity.SEntity;
import me.superischroma.spectaculation.entity.SEntityType;
import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class PowerOrb implements SkullStatistics, MaterialFunction, Ability, OrbBuff
{
    private static final Map<UUID, ArmorStand> USING_POWER_ORB_MAP = new HashMap<>();
    private static final Map<UUID, PowerOrbInstance> POWER_ORB_MAP = new HashMap<>();

    @Override
    public String getAbilityName()
    {
        return "Deploy";
    }

    @Override
    public int getAbilityCooldownTicks()
    {
        return 0;
    }

    @Override
    public int getManaCost()
    {
        return -2;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ITEM;
    }

    @Override
    public void onAbilityUse(Player player, SItem sItem)
    {
        SEntity sEntity = new SEntity(player.getLocation().clone().add(player.getLocation().getDirection().multiply(1.5)), SEntityType.VELOCITY_ARMOR_STAND);
        ArmorStand stand = (ArmorStand) sEntity.getEntity();
        if (POWER_ORB_MAP.containsKey(player.getUniqueId()))
        {
            PowerOrbInstance instance = POWER_ORB_MAP.get(player.getUniqueId());
            ArmorStand s = instance.getArmorStand();
            s.getWorld().playEffect(s.getLocation(), Effect.LARGE_SMOKE, Effect.LARGE_SMOKE.getData());
            s.remove();
            player.sendMessage(ChatColor.YELLOW + "Your previous " + instance.getColoredName() + ChatColor.YELLOW + " was removed!");
        }
        POWER_ORB_MAP.put(player.getUniqueId(), new PowerOrbInstance()
        {
            @Override
            public String getColoredName()
            {
                return sItem.getRarity().getColor() + sItem.getType().getDisplayName(sItem.getVariant());
            }
            @Override
            public ArmorStand getArmorStand()
            {
                return stand;
            }
        });
        stand.setVisible(false);
        AtomicInteger seconds = new AtomicInteger((int) (getOrbLifeTicks() / 20));
        stand.setCustomName(sItem.getRarity().getColor() + (getCustomOrbName() == null ? getBuffName() : getCustomOrbName()) +
                " " + ChatColor.YELLOW + seconds.get() + "s");
        stand.setCustomNameVisible(true);
        stand.setHelmet(SUtil.getSkull(getURL(), null));
        stand.setVelocity(new Vector(0, 0.1, 0));
        stand.setMetadata("specUnbreakableArmorStand", new FixedMetadataValue(Spectaculation.getPlugin(), true));
        new BukkitRunnable()
        {
            public void run()
            {
                if (stand.isDead())
                {
                    cancel();
                    return;
                }
                Vector velClone = stand.getVelocity().clone();
                stand.setVelocity(new Vector(0, velClone.getY() < 0D ? 0.1 : -0.1, 0));
            }
        }.runTaskTimer(Spectaculation.getPlugin(), 25, 25);
        new BukkitRunnable()
        {
            public void run()
            {
                if (stand.isDead())
                {
                    cancel();
                    return;
                }
                Location location = stand.getLocation();
                location.setYaw(stand.getLocation().getYaw() + 15.0f);
                stand.teleport(location);
                playEffect(stand.getEyeLocation().clone().add(stand.getLocation().getDirection().divide(new Vector(2, 2, 2))));
            }
        }.runTaskTimer(Spectaculation.getPlugin(), 0, 1);
        new BukkitRunnable()
        {
            public void run()
            {
                if (stand.isDead())
                {
                    cancel();
                    return;
                }
                int c = 0;
                for (Entity entity : stand.getNearbyEntities(18, 18, 18))
                {
                    if (!(entity instanceof Player)) continue;
                    Player p = (Player) entity;
                    if (c >= 5) break;
                    c++;
                    if (USING_POWER_ORB_MAP.containsKey(p.getUniqueId()))
                    {
                        if (!USING_POWER_ORB_MAP.get(p.getUniqueId()).equals(stand)) continue;
                    }
                    USING_POWER_ORB_MAP.put(p.getUniqueId(), stand);
                    new BukkitRunnable()
                    {
                        public void run()
                        {
                            USING_POWER_ORB_MAP.remove(p.getUniqueId());
                        }
                    }.runTaskLater(Spectaculation.getPlugin(), 20);
                    buff(p);
                    for (int i = 0; i < 8; i++)
                        playEffect(p.getLocation().add(SUtil.random(-0.5, 0.5), 0.1, SUtil.random(-0.5, 0.5)));
                }
                stand.setCustomName(sItem.getRarity().getColor() + (getCustomOrbName() == null ? getBuffName() : getCustomOrbName()) +
                        " " + ChatColor.YELLOW + Math.max(0, seconds.decrementAndGet()) + "s");
            }
        }.runTaskTimer(Spectaculation.getPlugin(), 20, 20);
        new BukkitRunnable()
        {
            public void run()
            {
                POWER_ORB_MAP.remove(player.getUniqueId());
                stand.getWorld().playEffect(stand.getLocation(), Effect.LARGE_SMOKE, Effect.LARGE_SMOKE.getData());
                stand.remove();
            }
        }.runTaskLater(Spectaculation.getPlugin(), getOrbLifeTicks() + 15);
    }

    protected abstract void buff(Player player);
    protected abstract long getOrbLifeTicks();
    protected abstract void playEffect(Location location);

    private interface PowerOrbInstance
    {
        String getColoredName();
        ArmorStand getArmorStand();
    }
}
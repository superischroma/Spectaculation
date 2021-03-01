package me.superischroma.spectaculation.entity.wolf;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.entity.EntityFunction;
import me.superischroma.spectaculation.entity.SEntity;
import me.superischroma.spectaculation.entity.SEntityType;
import me.superischroma.spectaculation.user.SlayerQuest;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.stream.Collectors;

public abstract class BaseWolf implements WolfStatistics, EntityFunction
{
    private LivingEntity target;

    @Override
    public void onSpawn(LivingEntity entity, SEntity sEntity)
    {
        new BukkitRunnable()
        {
            public void run()
            {
                if (entity.isDead())
                    return;
                if (target == null || target.isDead())
                    target = null;
                if (target != null)
                    return;
                Player found = (Player) SUtil.getRandom(entity.getNearbyEntities(16, 4, 16)
                        .stream().filter(e -> e instanceof Player &&
                                (((Player) e).getGameMode() == GameMode.SURVIVAL || ((Player) e).getGameMode() == GameMode.ADVENTURE)).collect(Collectors.toList()));
                if (found == null)
                    return;
                target = found;
                ((Wolf) entity).setTarget(target);
            }
        }.runTaskTimer(Spectaculation.getPlugin(), 20, 20);
    }

    @Override
    public void onDeath(SEntity sEntity, Entity killed, Entity damager)
    {
        if (!(damager instanceof Player)) return;
        Player player = (Player) damager;
        User user = User.getUser(player.getUniqueId());
        SlayerQuest quest = user.getSlayerQuest();
        if (quest == null) return;
        if (quest.getSpawned() != 0) return;
        Location k = killed.getLocation().clone();
        if (SUtil.random(0, 8) == 0 && quest.getType().getTier() >= 3)
        {
            SlayerQuest.playMinibossSpawn(k, player);
            SUtil.delay(() -> new SEntity(k, SEntityType.SVEN_FOLLOWER).setTarget(player), 12);
            return;
        }
        if (SUtil.random(0, 12) == 0 && quest.getType().getTier() >= 4)
        {
            SlayerQuest.playMinibossSpawn(k, player);
            SUtil.delay(() -> new SEntity(k, SEntityType.PACK_ENFORCER).setTarget(player), 12);
            return;
        }
        if (SUtil.random(0, 25) == 0 && quest.getType().getTier() >= 4)
        {
            SlayerQuest.playMinibossSpawn(k, player);
            SUtil.delay(() -> new SEntity(k, SEntityType.SVEN_ALPHA).setTarget(player), 12);
        }
    }
}
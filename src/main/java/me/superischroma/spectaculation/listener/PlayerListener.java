package me.superischroma.spectaculation.listener;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.entity.SEntity;
import me.superischroma.spectaculation.entity.SEntityType;
import me.superischroma.spectaculation.item.SBlock;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.skill.Skill;
import me.superischroma.spectaculation.user.PlayerUtils;
import me.superischroma.spectaculation.user.SlayerQuest;
import me.superischroma.spectaculation.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PlayerListener extends PListener
{
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        Player player = e.getPlayer();
        SBlock block = SBlock.getBlock(player.getLocation().clone().subtract(0, 0.3, 0));
        if (block == null) return;
        if (block.getType() == SMaterial.BOUNCER)
        {
            player.setVelocity(player.getVelocity().setY(block.getDataFloat("bounce")));
            new BukkitRunnable()
            {
                public void run()
                {
                    player.setVelocity(new Vector(block.getDataFloat("velX"), block.getDataFloat("velY"), block.getDataFloat("velZ")));
                }
            }.runTaskLater(Spectaculation.getPlugin(), block.getDataLong("delay"));
        }
        if (block.getType() != SMaterial.LAUNCHER && block.getType() != SMaterial.TELEPORTER_LAUNCHER) return;
        SEntity stand = new SEntity(player.getLocation(), SEntityType.VELOCITY_ARMOR_STAND);
        ArmorStand s = ((ArmorStand) stand.getEntity());
        s.setPassenger(player);
        s.setGravity(true);
        s.setVisible(false);
        new BukkitRunnable()
        {
            public void run() // if the launch is longer than 10 seconds, boot off the passenger
            {
                s.eject();
                s.remove();
            }
        }.runTaskLater(Spectaculation.getPlugin(), 200);
        stand.getEntity().setVelocity(new Vector(block.getDataFloat("velX"), block.getDataFloat("velY"), block.getDataFloat("velZ")));
        if (block.getType() == SMaterial.TELEPORTER_LAUNCHER)
        {
            new BukkitRunnable()
            {
                public void run()
                {
                    player.setFallDistance(0.0f);
                    player.teleport(new Location(Bukkit.getWorld(block.getDataString("world")),
                            block.getDataDouble("x"),
                            block.getDataDouble("y"),
                            block.getDataDouble("z"),
                            block.getDataFloat("yaw"),
                            block.getDataFloat("pitch")));
                }
            }.runTaskLater(Spectaculation.getPlugin(), block.getDataLong("delay"));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();
        User user = User.getUser(player.getUniqueId());
        if (!PlayerUtils.STATISTICS_CACHE.containsKey(player.getUniqueId())) PlayerUtils.STATISTICS_CACHE.put(player.getUniqueId(), PlayerUtils.getStatistics(player));
        for (Skill skill : Skill.getSkills())
            skill.onSkillUpdate(user, user.getSkillXP(skill));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        Player player = e.getPlayer();
        User user = User.getUser(player.getUniqueId());
        SlayerQuest quest = user.getSlayerQuest();
        if (quest != null && quest.getSpawned() != 0 && (quest.getKilled() != 0 || quest.getDied() != 0))
        {
            if (quest.getEntity() != null)
                quest.getEntity().remove();
            quest.setDied(System.currentTimeMillis());
        }
        user.save();
    }
}
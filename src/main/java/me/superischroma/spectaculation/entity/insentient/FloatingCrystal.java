package me.superischroma.spectaculation.entity.insentient;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.entity.SEntity;
import me.superischroma.spectaculation.entity.nms.VelocityArmorStand;
import me.superischroma.spectaculation.util.SLog;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public abstract class FloatingCrystal extends VelocityArmorStand
{
    public void onSpawn(LivingEntity entity, SEntity sEntity)
    {
        ArmorStand stand = (ArmorStand) entity;
        stand.setVisible(false);
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
        }.runTaskTimer(Spectaculation.getPlugin(), 15, 15);
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
                stand.getWorld().spigot().playEffect(stand.getEyeLocation().clone().add(SUtil.random(-0.5, 0.5), 0.0, SUtil.random(-0.5, 0.5)),
                        Effect.FIREWORKS_SPARK, 24, 1, 0, 0, 0, 1, 0, 64);
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
                List<Block> farmland = SUtil.getNearbyBlocks(stand.getEyeLocation(), 11, Material.SOIL);
                if (farmland.size() == 0) return;
                List<Block> possible = new ArrayList<>();
                for (Block block : farmland)
                {
                    Block a = block.getLocation().clone().add(0, 1, 0).getBlock();
                    if (a.getType() == Material.AIR)
                        possible.add(a);
                }
                if (possible.size() == 0) return;
                Block above = possible.get(SUtil.random(0, possible.size() - 1));
                if (above == null) return;
                above.setType(Material.CROPS);
                BlockState state = above.getState();
                state.setRawData((byte) 7);
                state.update();
                Location blockLocation = above.getLocation();
                Location crystalLocation = stand.getEyeLocation();
                Vector vector = blockLocation.clone().add(0.5, 0.0, 0.5).toVector().subtract(crystalLocation.clone().toVector());
                double count = 25.0;
                for (int i = 1; i <= (int) count; i++)
                {
                    stand.getWorld().spigot().playEffect(crystalLocation.clone().add(vector.clone().multiply((double) i / count)),
                            Effect.FIREWORKS_SPARK, 24, 1, 0, 0, 0, 1, 0, 64);
                }
            }
        }.runTaskTimer(Spectaculation.getPlugin(), 20, 20);
    }

    public double getXPDropped()
    {
        return 0.0;
    }

    protected abstract String getURL();
}
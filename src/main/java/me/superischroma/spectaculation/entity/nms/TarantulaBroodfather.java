package me.superischroma.spectaculation.entity.nms;

import com.google.common.util.concurrent.AtomicDouble;
import lib.trove.function.TObjectFunction;
import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.enchantment.EnchantmentType;
import me.superischroma.spectaculation.entity.*;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SLog;
import me.superischroma.spectaculation.util.SUtil;
import net.minecraft.server.v1_8_R3.EntitySpider;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSpider;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TarantulaBroodfather extends EntitySpider implements SNMSEntity, EntityFunction, EntityStatistics, SlayerBoss
{
    private static final TieredValue<Double> MAX_HEALTH_VALUES = new TieredValue<>(750.0, 30000.0, 900000.0, 2400000.0);
    private static final TieredValue<Double> DAMAGE_VALUES = new TieredValue<>(35.0, 110.0, 525.0, 1325.0);

    private final int tier;
    private final long end;
    private SEntity hologram;
    private SEntity top;
    private final UUID spawnerUUID;

    public TarantulaBroodfather(Integer tier, UUID spawnerUUID)
    {
        super(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle());
        this.tier = tier;
        this.end = System.currentTimeMillis() + 180000;
        this.spawnerUUID = spawnerUUID;
    }

    public TarantulaBroodfather(World world)
    {
        super(world);
        this.tier = 1;
        this.end = System.currentTimeMillis() + 180000;
        this.spawnerUUID = UUID.randomUUID();
    }

    public void t_()
    {
        super.t_();
        Player player = Bukkit.getPlayer(spawnerUUID);
        if (player == null) return;
        if (System.currentTimeMillis() > end)
        {
            User.getUser(player.getUniqueId()).failSlayerQuest();
            return;
        }
        hologram.getEntity().teleport(getBukkitEntity().getLocation().clone().add(0, 1.3, 0));
        hologram.getEntity().setCustomName(ChatColor.RED + SUtil.getFormattedTime(end - System.currentTimeMillis(), 1000));
        ((Spider) this.bukkitEntity).setTarget(player);
        ((CaveSpider) top.getEntity()).setTarget(player);
    }

    @Override
    public void onSpawn(LivingEntity entity, SEntity sEntity)
    {
        top = new SEntity(entity, SEntityType.TOP_CAVE_SPIDER, this);
        entity.setPassenger(top.getEntity());
        hologram = new SEntity(entity.getLocation().add(0, 1.3, 0), SEntityType.UNCOLLIDABLE_ARMOR_STAND);
        ((ArmorStand) hologram.getEntity()).setVisible(false);
        ((ArmorStand) hologram.getEntity()).setGravity(false);
        hologram.getEntity().setCustomNameVisible(true);
        Player player = Bukkit.getPlayer(spawnerUUID);
        if (player == null) return;
        if (tier >= 2) // noxious
        {
            new BukkitRunnable()
            {
                public void run()
                {
                    if (entity.isDead())
                    {
                        cancel();
                        return;
                    }
                    if (player.getLocation().distance(bukkitEntity.getLocation()) > 5) return;
                    player.damage(getDamageDealt() * 0.5, entity);
                }
            }.runTaskTimer(Spectaculation.getPlugin(), 20, 20);
        }
        new BukkitRunnable()
        {
            public void run()
            {
                Entity e = getBukkitEntity();
                if (e.isDead())
                {
                    cancel();
                    return;
                }
                if (e.getLocation().clone().distance(player.getLocation().clone()) < 5.0) return;
                if (e.getLocation().clone().subtract(0, 1, 0).getBlock().getType() == Material.AIR) return;
                Vector vector = e.getLocation().clone().toVector().subtract(player.getLocation().clone().toVector()).multiply(-1.0).multiply(new Vector(0.1, 0.2, 0.1));
                vector.setY(Math.abs(vector.getY()));
                if (vector.getY() < 0.8)
                    vector.setY(1.5);
                if (vector.getY() > 5.0)
                    vector.setY(5.0);
                e.setVelocity(e.getVelocity().add(vector));
            }
        }.runTaskTimer(Spectaculation.getPlugin(), 40, 40);
    }

    @Override
    public void onDeath(SEntity sEntity, Entity killed, Entity damager)
    {
        hologram.remove();
        top.remove();
    }

    @Override
    public void onDamage(SEntity sEntity, Entity damager, EntityDamageByEntityEvent e, AtomicDouble damage)
    {
        top.getEntity().damage(damage.get());
    }

    @Override
    public String getEntityName()
    {
        return "Tarantula Broodfather";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return MAX_HEALTH_VALUES.getByNumber(tier);
    }

    @Override
    public double getDamageDealt()
    {
        return DAMAGE_VALUES.getByNumber(tier);
    }

    @Override
    public double getMovementSpeed()
    {
        return 0.55;
    }

    @Override
    public LivingEntity spawn(Location location)
    {
        this.world = ((CraftWorld) location.getWorld()).getHandle();
        this.setPosition(location.getX(), location.getY(), location.getZ());
        this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (LivingEntity) this.getBukkitEntity();
    }

    @Override
    public List<EntityDrop> drops()
    {
        List<EntityDrop> drops = new ArrayList<>();
        int web = SUtil.random(1, 3);
        if (tier == 2) web = SUtil.random(9, 18);
        if (tier == 3) web = SUtil.random(28, 48);
        if (tier == 4) web = SUtil.random(52, 64);
        drops.add(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.TARANTULA_WEB).getStack(), web),
                EntityDropType.GUARANTEED, 1.0));
        if (tier >= 2)
        {
            int toxicArrowPoison = 16;
            if (tier == 3) toxicArrowPoison = SUtil.random(24, 30);
            if (tier == 4) toxicArrowPoison = SUtil.random(60, 64);
            drops.add(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.TOXIC_ARROW_POISON).getStack(), toxicArrowPoison),
                    EntityDropType.OCCASIONAL, 0.2));
            drops.add(new EntityDrop(SMaterial.BITE_RUNE, EntityDropType.RARE, 0.05));
        }
        if (tier >= 3)
        {
            SItem arthoBook = SItem.of(SMaterial.ENCHANTED_BOOK);
            arthoBook.addEnchantment(EnchantmentType.BANE_OF_ARTHROPODS, 6);
            drops.add(new EntityDrop(SMaterial.SPIDER_CATALYST, EntityDropType.EXTRAORDINARILY_RARE, 0.01));
            drops.add(new EntityDrop(arthoBook.getStack(), EntityDropType.EXTRAORDINARILY_RARE, 0.01));
            drops.add(new EntityDrop(SMaterial.FLY_SWATTER, EntityDropType.CRAZY_RARE, 0.002));
            drops.add(new EntityDrop(SMaterial.TARANTULA_TALISMAN, EntityDropType.CRAZY_RARE, 0.002));
        }
        if (tier >= 4)
            drops.add(new EntityDrop(SMaterial.DIGESTED_MOSQUITO, EntityDropType.CRAZY_RARE, 0.00054));
        return drops;
    }

    public double getXPDropped()
    {
        return 0.0;
    }

    @Override
    public UUID getSpawnerUUID()
    {
        return spawnerUUID;
    }

    public static class TopCaveSpider implements EntityStatistics, EntityFunction
    {
        private final TarantulaBroodfather parent;

        public TopCaveSpider(TarantulaBroodfather parent)
        {
            this.parent = parent;
        }

        @Override
        public String getEntityName()
        {
            return "";
        }

        @Override
        public double getEntityMaxHealth()
        {
            return Double.MAX_VALUE;
        }

        @Override
        public double getDamageDealt()
        {
            return 0;
        }

        @Override
        public double getXPDropped()
        {
            return 0.0;
        }

        @Override
        public boolean hasNameTag()
        {
            return false;
        }

        @Override
        public void onSpawn(LivingEntity entity, SEntity sEntity)
        {
            entity.setCustomNameVisible(true);
        }

        @Override
        public boolean tick(LivingEntity entity)
        {
            Spider taran = (Spider) parent.getBukkitEntity();
            entity.setCustomName(ChatColor.RED + parent.getEntityName() + " " + ChatColor.GREEN + (int) (taran.getHealth()) +
                    ChatColor.WHITE + "/" + ChatColor.GREEN + (int) taran.getMaxHealth() + ChatColor.RED + "❤");
            return true;
        }

        @Override
        public void onDamage(SEntity sEntity, Entity damager, EntityDamageByEntityEvent e, AtomicDouble damage)
        {
            e.setCancelled(true);
            Spider taran = (Spider) parent.getBukkitEntity();
            taran.damage(0.0, damager);
        }
    }
}
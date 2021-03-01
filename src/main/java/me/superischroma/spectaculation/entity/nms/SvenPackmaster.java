package me.superischroma.spectaculation.entity.nms;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.Getter;
import lombok.Setter;
import me.superischroma.spectaculation.enchantment.EnchantmentType;
import me.superischroma.spectaculation.entity.*;
import me.superischroma.spectaculation.entity.wolf.WolfStatistics;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import net.minecraft.server.v1_8_R3.EntityWolf;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SvenPackmaster extends EntityWolf implements SNMSEntity, EntityFunction, WolfStatistics, SlayerBoss
{
    private static final TieredValue<Double> MAX_HEALTH_VALUES = new TieredValue<>(2000.0, 40000.0, 750000.0, 2000000.0);
    private static final TieredValue<Double> DAMAGE_VALUES = new TieredValue<>(60.0, 200.0, 450.0, 1100.0);
    private static final TieredValue<Double> TRUE_DAMAGE_VALUES = new TieredValue<>(0.0, 10.0, 50.0, 200.0);
    private static final TieredValue<Double> SPEED_VALUES = new TieredValue<>(0.35, 0.4, 0.45, 0.55);

    private final int tier;
    private final long end;
    private SEntity hologram;
    private final UUID spawnerUUID;
    private long lastDamage;
    private boolean pupsSpawned;
    @Getter
    private final List<SEntity> pups;

    public SvenPackmaster(Integer tier, UUID spawnerUUID)
    {
        super(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle());
        this.tier = tier;
        this.end = System.currentTimeMillis() + 240000;
        this.spawnerUUID = spawnerUUID;
        this.lastDamage = System.currentTimeMillis() - 1;
        this.pupsSpawned = false;
        this.pups = new ArrayList<>();
    }

    public SvenPackmaster(World world)
    {
        super(world);
        this.tier = 1;
        this.end = System.currentTimeMillis() + 240000;
        this.spawnerUUID = UUID.randomUUID();
        this.lastDamage = System.currentTimeMillis() - 1;
        this.pupsSpawned = false;
        this.pups = new ArrayList<>();
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
        if (this.getBukkitEntity().getLocation().distance(player.getLocation()) > 15.0 && SUtil.random(0, 15) == 0)
        {
            this.getBukkitEntity().teleport(player.getLocation());
            player.sendMessage(ChatColor.DARK_PURPLE + "The boss teleported to you using some really dark magic!");
        }
        if (System.currentTimeMillis() > lastDamage && (pups.isEmpty() || !pupsSpawned)) // regeneration
            this.setHealth(Math.min(this.getMaxHealth(), this.getHealth() + (this.getHealth() * 0.0005f)));
        if (pups.isEmpty())
            this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(getMovementSpeed());
        else
            this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.3);
        this.getBukkitEntity().getWorld().spigot().playEffect(this.getBukkitEntity().getLocation(),
                Effect.FLAME, 0, 1, (float) SUtil.random(-1.0, 1.0), 0.0f, (float) SUtil.random(-1.0, 1.0), 0.0f, 1, 16);
        this.getBukkitEntity().getWorld().spigot().playEffect(this.getBukkitEntity().getLocation(), Effect.FIREWORKS_SPARK,
                0, 1, (float) SUtil.random(-1.0, 1.0), 0.0f, (float) SUtil.random(-1.0, 1.0), 0.0f, 1, 16);
        ((Wolf) this.getBukkitEntity()).setTarget(player);
        hologram.getEntity().teleport(getBukkitEntity().getLocation().clone().add(0, 1.3, 0));
        hologram.getEntity().setCustomName(ChatColor.RED + SUtil.getFormattedTime(end - System.currentTimeMillis(), 1000));
    }

    @Override
    public void onSpawn(LivingEntity entity, SEntity sEntity)
    {
        Player player = Bukkit.getPlayer(spawnerUUID);
        if (player != null)
            player.playSound(player.getLocation(), Sound.WOLF_HOWL, 1f, 5f);
        hologram = new SEntity(entity.getLocation().add(0, 1.3, 0), SEntityType.UNCOLLIDABLE_ARMOR_STAND);
        ((ArmorStand) hologram.getEntity()).setVisible(false);
        ((ArmorStand) hologram.getEntity()).setGravity(false);
        hologram.getEntity().setCustomNameVisible(true);
    }

    @Override
    public String getEntityName()
    {
        return "Sven Packmaster";
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
        return SPEED_VALUES.getByNumber(tier);
    }

    @Override
    public void onDeath(SEntity sEntity, Entity killed, Entity damager)
    {
        hologram.remove();
        for (SEntity pup : pups)
            pup.getEntity().setHealth(0.0);
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent e)
    {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        User.getUser(player.getUniqueId()).damage(TRUE_DAMAGE_VALUES.getByNumber(tier), EntityDamageEvent.DamageCause.ENTITY_ATTACK, this.getBukkitEntity());
        if (player.getUniqueId().equals(spawnerUUID))
            lastDamage = System.currentTimeMillis() + (15 * 1000);
    }

    @Override
    public LivingEntity spawn(Location location)
    {
        this.world = ((CraftWorld) location.getWorld()).getHandle();
        this.setPosition(location.getX(), location.getY(), location.getZ());
        this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (LivingEntity) this.getBukkitEntity();
    }

    public void onDamage(SEntity sEntity, Entity damager, EntityDamageByEntityEvent e, AtomicDouble damage)
    {
        if (e.getDamager() instanceof Arrow)
        {
            e.setCancelled(true);
            return;
        }
        Player player = Bukkit.getPlayer(spawnerUUID);
        if (player == null) return;
        if (tier >= 3 && sEntity.getEntity().getHealth() - damage.get() < getEntityMaxHealth() / 2.0 && !pupsSpawned) // pups
        {
            pupsSpawned = true;
            for (int i = 0; i < 10; i++)
            {
                SUtil.delay(() ->
                {
                    if (bukkitEntity.isDead())
                        return;
                    pups.add(new SEntity(sEntity.getEntity(), SEntityType.SVEN_PUP, getEntityMaxHealth() * 0.1, getDamageDealt() * 0.5, player, this));
                }, i * 20);
            }
        }
    }

    public List<EntityDrop> drops()
    {
        List<EntityDrop> drops = new ArrayList<>();
        int teeth = SUtil.random(1, 3);
        if (tier == 2) teeth = SUtil.random(9, 18);
        if (tier == 3) teeth = SUtil.random(30, 50);
        if (tier == 4) teeth = SUtil.random(50, 64);
        drops.add(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.WOLF_TOOTH).getStack(), teeth),
                EntityDropType.GUARANTEED, 1.0));
        if (tier >= 2)
        {
            int hamsterWheel = 1;
            if (tier == 3) hamsterWheel = SUtil.random(2, 4);
            if (tier == 4) hamsterWheel = SUtil.random(4, 5);
            drops.add(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.HAMSTER_WHEEL).getStack(), hamsterWheel),
                    EntityDropType.OCCASIONAL, 0.2));
            drops.add(new EntityDrop(SMaterial.SPIRIT_RUNE, EntityDropType.RARE, 0.05));
        }
        if (tier >= 3)
        {
            SItem critBook = SItem.of(SMaterial.ENCHANTED_BOOK);
            critBook.addEnchantment(EnchantmentType.CRITICAL, 6);
            drops.add(new EntityDrop(critBook.getStack(), EntityDropType.EXTRAORDINARILY_RARE, 0.01));
            drops.add(new EntityDrop(SMaterial.RED_CLAW_EGG, EntityDropType.CRAZY_RARE, tier == 3 ? 0.00023 : 0.0023));
        }
        if (tier >= 4)
        {
            drops.add(new EntityDrop(SMaterial.COUTURE_RUNE, EntityDropType.CRAZY_RARE, 0.0015));
            drops.add(new EntityDrop(SMaterial.GRIZZLY_BAIT, EntityDropType.CRAZY_RARE, 0.000538));
            drops.add(new EntityDrop(SMaterial.OVERFLUX_CAPACITOR, EntityDropType.CRAZY_RARE, 0.000385));
        }
        return drops;
    }

    public double getXPDropped()
    {
        return 0.0;
    }

    @Override
    public boolean isBaby()
    {
        return false;
    }

    public boolean isAngry()
    {
        return true;
    }

    @Override
    public UUID getSpawnerUUID()
    {
        return spawnerUUID;
    }
}
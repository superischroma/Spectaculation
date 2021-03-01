package me.superischroma.spectaculation.entity.nms;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.enchantment.EnchantmentType;
import me.superischroma.spectaculation.entity.*;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SLog;
import me.superischroma.spectaculation.util.SUtil;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RevenantHorror extends EntityZombie implements SNMSEntity, EntityFunction, ZombieStatistics, SlayerBoss
{
    private static final TieredValue<Double> MAX_HEALTH_VALUES = new TieredValue<>(500.0, 20000.0, 400000.0, 1500000.0);
    private static final TieredValue<Double> DAMAGE_VALUES = new TieredValue<>(15.0, 50.0, 300.0, 1000.0);
    private static final TieredValue<Double> SPEED_VALUES = new TieredValue<>(0.35, 0.4, 0.45, 0.55);

    private final int tier;
    private boolean enraged;
    private final long end;
    private SEntity hologram;
    private final UUID spawnerUUID;

    public RevenantHorror(Integer tier, UUID spawnerUUID)
    {
        super(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle());
        this.tier = tier;
        this.enraged = false;
        this.end = System.currentTimeMillis() + 180000;
        this.spawnerUUID = spawnerUUID;
    }

    public RevenantHorror(World world)
    {
        super(world);
        this.tier = 1;
        this.enraged = false;
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
        hologram.getEntity().teleport(getBukkitEntity().getLocation().clone().add(0, 2.3, 0));
        hologram.getEntity().setCustomName(ChatColor.RED + SUtil.getFormattedTime(end - System.currentTimeMillis(), 1000));
        ((Zombie) this.bukkitEntity).setTarget(player);
        if (tier >= 3 && !enraged && SUtil.random(0, 200) == 0) // enrage
        {
            enraged = true;
            this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(getMovementSpeed());
            player.playSound(player.getLocation(), Sound.ZOMBIE_WOODBREAK, 1f, 1f);
            player.setVelocity(new Vector(SUtil.random(-1.0, 1.0), SUtil.random(0.0, 0.5), SUtil.random(-1.0, 1.0)));
            new BukkitRunnable()
            {
                public void run()
                {
                    enraged = false;
                    RevenantHorror.this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(getMovementSpeed());
                }
            }.runTaskLater(Spectaculation.getPlugin(), 200);
        }
    }

    @Override
    public void onSpawn(LivingEntity entity, SEntity sEntity)
    {
        hologram = new SEntity(entity.getLocation().add(0, 2.3, 0), SEntityType.UNCOLLIDABLE_ARMOR_STAND);
        ((ArmorStand) hologram.getEntity()).setVisible(false);
        ((ArmorStand) hologram.getEntity()).setGravity(false);
        hologram.getEntity().setCustomNameVisible(true);
        new BukkitRunnable()
        {
            public void run()
            {
                if (entity.isDead())
                {
                    cancel();
                    return;
                }
                Player player = Bukkit.getPlayer(spawnerUUID);
                if (player == null)
                    return;
                player.damage(getDamageDealt() * 0.5, entity);
            }
        }.runTaskTimer(Spectaculation.getPlugin(), 60, 60);
        if (tier >= 2) // pestilence
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
                    Player player = Bukkit.getPlayer(spawnerUUID);
                    if (player == null)
                        return;
                    player.damage(getDamageDealt(), entity);
                }
            }.runTaskTimer(Spectaculation.getPlugin(), 20, 20);
        }
    }

    @Override
    public void onDeath(SEntity sEntity, Entity killed, Entity damager)
    {
        hologram.remove();
    }

    @Override
    public String getEntityName()
    {
        return "Revenant Horror";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return MAX_HEALTH_VALUES.getByNumber(tier);
    }

    @Override
    public double getDamageDealt()
    {
        return DAMAGE_VALUES.getByNumber(tier) * (enraged ? 1.8 : 1.0);
    }

    @Override
    public double getMovementSpeed()
    {
        return SPEED_VALUES.getByNumber(tier) * (enraged ? 1.05 : 1.0);
    }

    public SEntityEquipment getEntityEquipment()
    {
        return new SEntityEquipment(new ItemStack(Material.DIAMOND_HOE),
                SItem.of(SMaterial.REVENANT_HORROR_HEAD).getStack(),
                new ItemStack(Material.DIAMOND_CHESTPLATE),
                new ItemStack(Material.CHAINMAIL_LEGGINGS),
                new ItemStack(Material.DIAMOND_BOOTS));
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
        int revFlesh = SUtil.random(1, 3);
        if (tier == 2) revFlesh = SUtil.random(9, 18);
        if (tier == 3) revFlesh = SUtil.random(30, 50);
        if (tier == 4) revFlesh = SUtil.random(50, 64);
        drops.add(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.REVENANT_FLESH).getStack(), revFlesh),
                EntityDropType.GUARANTEED, 1.0));
        if (tier >= 2)
        {
            int foulFlesh = 1;
            if (tier == 3) foulFlesh = SUtil.random(1, 2);
            if (tier == 4) foulFlesh = SUtil.random(2, 3);
            drops.add(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.FOUL_FLESH).getStack(), foulFlesh),
                    EntityDropType.OCCASIONAL, 0.2));
            drops.add(new EntityDrop(SMaterial.PESTILENCE_RUNE, EntityDropType.RARE, 0.05));
            drops.add(new EntityDrop(SMaterial.UNDEAD_CATALYST, EntityDropType.EXTRAORDINARILY_RARE, 0.01));
            drops.add(new EntityDrop(SMaterial.REVENANT_CATALYST, EntityDropType.EXTRAORDINARILY_RARE, 0.01));
        }
        if (tier >= 3)
        {
            SItem smiteBook = SItem.of(SMaterial.ENCHANTED_BOOK);
            smiteBook.addEnchantment(EnchantmentType.SMITE, 6);
            drops.add(new EntityDrop(smiteBook.getStack(), EntityDropType.EXTRAORDINARILY_RARE, 0.01));
            drops.add(new EntityDrop(SMaterial.BEHEADED_HORROR, EntityDropType.CRAZY_RARE, 0.005));
        }
        if (tier >= 4)
        {
            drops.add(new EntityDrop(SMaterial.SNAKE_RUNE, EntityDropType.CRAZY_RARE, 0.005));
            drops.add(new EntityDrop(SMaterial.SCYTHE_BLADE, EntityDropType.CRAZY_RARE, 7.0 / 13000.0));
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

    @Override
    public boolean isVillager()
    {
        return false;
    }

    @Override
    public UUID getSpawnerUUID()
    {
        return spawnerUUID;
    }
}
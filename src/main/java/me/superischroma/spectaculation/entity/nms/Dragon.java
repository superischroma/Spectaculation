package me.superischroma.spectaculation.entity.nms;

import lombok.Getter;
import lombok.Setter;
import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.entity.*;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import net.minecraft.server.v1_8_R3.*;
import org.apache.commons.lang3.Range;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.*;

public abstract class Dragon extends EntityEnderDragon implements SNMSEntity, EntityFunction, EntityStatistics
{
    public static final long DEFAULT_ATTACK_COOLDOWN = 300;
    public static final Range<Double> DEFAULT_DAMAGE_DEGREE_RANGE = Range.between(0.3, 0.7);
    public static final double DEFAULT_SPEED_MODIFIER = 1.4D;

    private boolean frozen;
    private double yD;
    @Getter
    @Setter
    private double speedModifier;
    @Getter
    @Setter
    private Range<Double> damageDegree;
    @Getter
    private final long attackCooldown;

    protected Dragon(World world, double speedModifier, Range<Double> damageDegree, long attackCooldown)
    {
        super(world);
        this.frozen = false;
        this.yD = 1.0;
        this.speedModifier = speedModifier;
        this.damageDegree = damageDegree;
        this.attackCooldown = attackCooldown;
        try
        {
            Field b = PathfinderGoalSelector.class.getDeclaredField("b");
            Field c = PathfinderGoalSelector.class.getDeclaredField("c");
            b.setAccessible(true);
            c.setAccessible(true);
            ((List) b.get(goalSelector)).clear();
            ((List) c.get(goalSelector)).clear();
            ((List) b.get(targetSelector)).clear();
            ((List) c.get(targetSelector)).clear();
        }
        catch (NoSuchFieldException | IllegalAccessException ex)
        {
            ex.printStackTrace();
        }
    }

    protected Dragon(double speedModifier, Range<Double> damageDegree, long attackCooldown)
    {
        this(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle(), speedModifier, damageDegree, attackCooldown);
    }

    public double getXPDropped()
    {
        return 0.0;
    }

    @Override
    public boolean tick(LivingEntity entity)
    {
        target = null;
        if (frozen)
        {
            entity.setVelocity(new Vector(0, 0, 0));
            return true;
        }
        Location location = entity.getLocation();
        if (location.getY() < SUtil.random(7.0, 13.0))
            yD = SUtil.random(0.6, 1.2);
        if (location.getY() > SUtil.random(57.0, 63.0))
            yD = SUtil.random(-1.2, -0.6);
        if (location.getX() < -718.0 || location.getX() > -623.0 || location.getZ() < -328.0 || location.getZ() > -224.0)
        {
            Vector vector = entity.getLocation().clone().subtract(new Vector(-670.5, 58.0, -275.5)).toVector();
            Location center = location.clone();
            center.setDirection(vector);
            entity.teleport(center);
            entity.setVelocity(vector.clone().normalize().multiply(-1.0).multiply(3.0));
            return true;
        }
        entity.setVelocity(entity.getLocation().getDirection().clone().multiply(-1.0).multiply(speedModifier).setY(yD));
        return true;
    }

    @Override
    public void onSpawn(LivingEntity entity, SEntity sEntity)
    {
        //this.getBoundingBox().a =
        new BukkitRunnable()
        {
            public void run()
            {
                if (entity.isDead())
                {
                    cancel();
                    return;
                }
                switch (SUtil.random(0, 1))
                {
                    case 0:
                    {
                        frozen = true;
                        for (int i = 1; i <= 4; i++)
                            SUtil.lightningLater(entity.getLocation(), true, 10 * i);
                        new BukkitRunnable()
                        {
                            public void run()
                            {
                                if (entity.isDead())
                                    return;
                                for (Entity e : entity.getNearbyEntities(200, 200, 200))
                                {
                                    e.getWorld().strikeLightningEffect(e.getLocation());
                                    double r = SUtil.random(damageDegree.getMinimum(), damageDegree.getMaximum());
                                    if (!(e instanceof LivingEntity)) continue;
                                    LivingEntity le = (LivingEntity) e;
                                    int damage = (int) (le.getMaxHealth() * r);
                                    if (le instanceof Player)
                                        User.getUser(le.getUniqueId()).damage(damage, EntityDamageEvent.DamageCause.ENTITY_ATTACK, entity);
                                    else
                                        le.damage(damage);
                                    e.sendMessage(ChatColor.DARK_PURPLE + "☬ " + ChatColor.RED + getEntityName() +
                                            ChatColor.LIGHT_PURPLE + " used " + ChatColor.YELLOW + "Lightning Strike" +
                                            ChatColor.LIGHT_PURPLE + " on you for " + ChatColor.RED + damage +
                                            " damage.");
                                }
                                frozen = false;
                            }
                        }.runTaskLater(Spectaculation.getPlugin(), 50);
                        return;
                    }
                    case 1:
                    {
                        frozen = true;
                        Entity near = null;
                        for (Entity n : entity.getNearbyEntities(50, 50, 50))
                        {
                            if (n instanceof Player)
                                near = n;
                        }
                        Entity finalNear = near;
                        if (near != null)
                        {
                            SUtil.runIntervalForTicks(() ->
                            {
                                if (entity.isDead())
                                    return;
                                for (int i = 0; i < 15; i++)
                                {
                                    entity.getWorld().spigot().playEffect(entity.getEyeLocation().subtract(0.0, 8.0, 0.0).
                                                    add(entity.getLocation().getDirection().multiply(-5.0))
                                            .add(SUtil.random(-0.5, 0.5), SUtil.random(-0.5, 0.5), SUtil.random(-0.5, 0.5)),
                                            Effect.FLAME, 0, 1, 0.0f, 0.0f, 0.0f, 0.0f, 1, 50);
                                }
                            }, 5, 140);
                            float fn = SUtil.getLookAtYaw(near.getLocation().toVector());
                            new BukkitRunnable()
                            {
                                public void run()
                                {
                                    SUtil.runIntervalForTicks(() ->
                                    {
                                        if (entity.isDead())
                                            return;
                                        if ((int) fn == (int) entity.getLocation().getYaw())
                                            return;
                                        Location location = entity.getLocation().clone();
                                        location.setYaw(entity.getLocation().clone().getYaw() + 1.0f);
                                        entity.teleport(location);
                                    }, 1, 120);
                                }
                            }.runTaskLater(Spectaculation.getPlugin(), 20);
                            new BukkitRunnable()
                            {
                                public void run()
                                {
                                    SUtil.runIntervalForTicks(() ->
                                    {
                                        if (entity.isDead())
                                            return;
                                        Fireball fireball = entity.getWorld().spawn(entity.getEyeLocation().subtract(0.0, 8.0, 0.0)
                                                .add(entity.getLocation().getDirection().multiply(-5.0)), Fireball.class);
                                        fireball.setMetadata("dragon", new FixedMetadataValue(Spectaculation.getPlugin(), sEntity));
                                        fireball.setDirection(finalNear.getLocation().getDirection().multiply(-1.0).normalize());
                                    }, 5, 60);
                                }
                            }.runTaskLater(Spectaculation.getPlugin(), 80);
                        }
                        new BukkitRunnable()
                        {
                            public void run()
                            {
                                frozen = false;
                            }
                        }.runTaskLater(Spectaculation.getPlugin(), 140);
                        return;
                    }
                }
            }
        }.runTaskTimer(Spectaculation.getPlugin(), 100, attackCooldown);
    }

    @Override
    public void onDeath(SEntity sEntity, Entity killed, Entity damager)
    {
        Map<UUID, List<Location>> eyes = new HashMap<>(StaticDragonManager.EYES);
        StaticDragonManager.endFight();
        StringBuilder message = new StringBuilder();
        message.append(ChatColor.GREEN).append(ChatColor.BOLD).append("--------------------------------------\n \n");
        message.append(ChatColor.GOLD).append(ChatColor.BOLD)
                .append(sEntity.getStatistics().getEntityName().toUpperCase()).append(" DOWN!\n \n");
        List<Map.Entry<UUID, Double>> damageDealt = new ArrayList<>(sEntity.getDamageDealt().entrySet());
        damageDealt.sort(Map.Entry.comparingByValue());
        Collections.reverse(damageDealt);
        String name = null;
        if (damager instanceof Player)
            name = damager.getName();
        if (damager instanceof Arrow && ((Arrow) damager).getShooter() instanceof Player)
            name = ((Player) ((Arrow) damager).getShooter()).getName();
        if (name != null)
        {
            message.append(ChatColor.GREEN).append(name)
                    .append(ChatColor.GRAY).append(" dealt the final blow.\n ");
        }
        for (int i = 0; i < Math.min(3, damageDealt.size()); i++)
        {
            message.append("\n");
            Map.Entry<UUID, Double> d = damageDealt.get(i);
            int place = i + 1;
            switch (place)
            {
                case 1: message.append(ChatColor.YELLOW); break;
                case 2: message.append(ChatColor.GOLD); break;
                case 3: message.append(ChatColor.RED); break;
            }
            message.append(ChatColor.BOLD).append(place);
            switch (place)
            {
                case 1: message.append("st"); break;
                case 2: message.append("nd"); break;
                case 3: message.append("rd"); break;
            }
            message.append(" Damager").append(ChatColor.RESET).append(ChatColor.GRAY)
                    .append(" - ").append(ChatColor.GREEN)
                    .append(Bukkit.getOfflinePlayer(d.getKey()).getName())
                    .append(ChatColor.GRAY).append(" - ")
                    .append(ChatColor.YELLOW).append(SUtil.commaify(d.getValue().intValue()));
        }
        message.append("\n").append(ChatColor.RESET).append(ChatColor.YELLOW).append("Your Damage: ")
                .append("%s").append(ChatColor.RESET).append("\n \n");
        message.append(ChatColor.GREEN).append(ChatColor.BOLD).append("--------------------------------------");
        for (Player player : Bukkit.getOnlinePlayers())
        {
            int place = -1, damage = 0;
            for (int i = 0; i < damageDealt.size(); i++)
            {
                Map.Entry<UUID, Double> d = damageDealt.get(i);
                if (d.getKey().equals(player.getUniqueId()))
                {
                    place = i + 1;
                    damage = d.getValue().intValue();
                }
            }
            player.sendMessage(String.format(message.toString(), (place != -1 ?
                    ChatColor.GREEN + SUtil.commaify(damage) + ChatColor.GRAY + " (Position #" + place + ")" :
                    ChatColor.GRAY + "Did not fight!")));
        }
        new BukkitRunnable()
        {
            public void run()
            {
                for (int i = 0; i < damageDealt.size(); i++)
                {
                    Map.Entry<UUID, Double> d = damageDealt.get(i);
                    Player player = Bukkit.getPlayer(d.getKey());
                    if (player == null) continue;
                    int weight = 0;
                    if (eyes.containsKey(player.getUniqueId()))
                        weight += Math.min(400, eyes.get(player.getUniqueId()).size() * 100);
                    if (i == 0) weight += 300;
                    if (i == 1) weight += 250;
                    if (i == 2) weight += 200;
                    if (i >= 3 && i <= 6) weight += 125;
                    if (i >= 7 && i <= 14) weight += 100;
                    if (i >= 15) weight += 75;
                    List<DragonDrop> possibleMajorDrops = new ArrayList<>();
                    SEntityType type = sEntity.getSpecType();
                    SUtil.addIf(new DragonDrop(SMaterial.ASPECT_OF_THE_DRAGONS, 450), possibleMajorDrops, weight >= 450);
                    SUtil.addIf(new DragonDrop(SMaterial.VagueEntityMaterial.HELMET, 400, type), possibleMajorDrops, weight >= 400);
                    SUtil.addIf(new DragonDrop(SMaterial.VagueEntityMaterial.CHESTPLATE, 325, type), possibleMajorDrops, weight >= 325);
                    SUtil.addIf(new DragonDrop(SMaterial.VagueEntityMaterial.LEGGINGS, 350, type), possibleMajorDrops, weight >= 350);
                    SUtil.addIf(new DragonDrop(SMaterial.VagueEntityMaterial.BOOTS, 300, type), possibleMajorDrops, weight >= 300);
                    int remainingWeight = weight;
                    if (possibleMajorDrops.size() > 0)
                    {
                        DragonDrop drop = possibleMajorDrops.get(SUtil.random(0, possibleMajorDrops.size() - 1));
                        SMaterial majorDrop = drop.getMaterial();
                        remainingWeight -= drop.getWeight();
                        if (majorDrop != null)
                        {
                            SItem sItem = SItem.of(majorDrop);
                            Item item = SUtil.spawnPersonalItem(sItem.getStack(), killed.getLocation(), player);
                            item.setMetadata("obtained", new FixedMetadataValue(Spectaculation.getPlugin(), true));
                            item.setCustomNameVisible(true);
                            item.setCustomName(item.getItemStack().getAmount() + "x " + sItem.getFullName());
                        }
                    }
                    List<DragonDrop> minorDrops = new ArrayList<>(Arrays.asList(new DragonDrop(SMaterial.ENDER_PEARL, 0),
                            new DragonDrop(SMaterial.ENCHANTED_ENDER_PEARL, 0)));
                    SUtil.addIf(new DragonDrop(SMaterial.VagueEntityMaterial.FRAGMENT, 22, type), minorDrops, weight >= 22);
                    int frags = 0;
                    for (; remainingWeight >= 22; remainingWeight -= 22, frags += 1);
                    for (DragonDrop minorDrop : minorDrops)
                    {
                        SItem sItem = SItem.of(minorDrop.getMaterial());
                        if (minorDrop.getMaterial() == null) continue;
                        if (minorDrop.getVagueEntityMaterial() != null && frags != 0)
                        {
                            Item item = SUtil.spawnPersonalItem(SUtil.setStackAmount(sItem.getStack(),
                                    frags), killed.getLocation(), player);
                            item.setCustomNameVisible(true);
                            item.setCustomName(item.getItemStack().getAmount() + "x " + sItem.getFullName());
                            continue;
                        }
                        Item item = SUtil.spawnPersonalItem(SUtil.setStackAmount(sItem.getStack(), SUtil.random(5, 10)),
                                killed.getLocation(), player);
                        item.setCustomNameVisible(true);
                        item.setCustomName(item.getItemStack().getAmount() + "x " + sItem.getFullName());
                    }
                }
            }
        }.runTaskLater(Spectaculation.getPlugin(), 200);
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
    public void onAttack(EntityDamageByEntityEvent e)
    {
        int d = SUtil.random(354, 902);
        if (e.getEntity() instanceof Player)
            User.getUser(e.getEntity().getUniqueId()).damage(d, e.getCause(), e.getDamager());
        else if (e.getEntity() instanceof LivingEntity)
            ((LivingEntity) e.getEntity()).damage(d);
        e.getEntity().setVelocity(e.getEntity().getVelocity().multiply(8.0));
        e.getEntity().sendMessage(ChatColor.DARK_PURPLE + "☬ " + ChatColor.RED + getEntityName() +
                ChatColor.LIGHT_PURPLE + " used " + ChatColor.YELLOW + "Rush" +
                ChatColor.LIGHT_PURPLE + " on you for " + ChatColor.RED + d +
                " damage.");
    }

    @Getter
    private static class DragonDrop
    {
        private final SMaterial material;
        private final SMaterial.VagueEntityMaterial vagueEntityMaterial;
        private final int weight;

        public DragonDrop(SMaterial material, int weight)
        {
            this.material = material;
            this.vagueEntityMaterial = null;
            this.weight = weight;
        }

        public DragonDrop(SMaterial.VagueEntityMaterial material, int weight, SEntityType type)
        {
            this.material = material.getEntityArmorPiece(type);
            this.vagueEntityMaterial = material;
            this.weight = weight;
        }
    }
}
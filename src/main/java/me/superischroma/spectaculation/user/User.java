package me.superischroma.spectaculation.user;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.Getter;
import lombok.Setter;
import me.superischroma.spectaculation.skill.*;
import me.superischroma.spectaculation.slayer.SlayerBossType;
import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.collection.ItemCollection;
import me.superischroma.spectaculation.collection.ItemCollectionReward;
import me.superischroma.spectaculation.collection.ItemCollectionRewards;
import me.superischroma.spectaculation.config.Config;
import me.superischroma.spectaculation.entity.SEntity;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.potion.ActivePotionEffect;
import me.superischroma.spectaculation.potion.PotionEffect;
import me.superischroma.spectaculation.potion.PotionEffectType;
import me.superischroma.spectaculation.region.Region;
import me.superischroma.spectaculation.region.RegionType;
import me.superischroma.spectaculation.slayer.SlayerQuest;
import me.superischroma.spectaculation.util.SUtil;
import net.minecraft.server.v1_8_R3.EntityHuman;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class User
{
    public static final int ISLAND_SIZE = 125;

    private static final Map<UUID, User> USER_CACHE = new HashMap<>();
    private static final Spectaculation plugin = Spectaculation.getPlugin();
    private static final File USER_FOLDER = new File(plugin.getDataFolder(), "./users");

    @Getter
    private UUID uuid;
    private final Config config;
    private final Map<ItemCollection, Integer> collections;
    @Getter
    private long coins;
    @Getter
    private long bankCoins;
    @Getter
    private Double islandX;
    @Getter
    private Double islandZ;
    @Getter
    private Region lastRegion;
    @Getter
    private final Map<SMaterial, Integer> quiver;
    @Getter
    private final List<ActivePotionEffect> effects;
    @Getter
    private double farmingXP;
    @Getter
    private double miningXP;
    @Getter
    private double combatXP;
    @Getter
    private double foragingXP;
    private final int[] highestSlayers;
    private final int[] slayerXP;
    @Getter
    @Setter
    private boolean permanentCoins;
    @Getter
    @Setter
    private SlayerQuest slayerQuest;

    private User(UUID uuid)
    {
        this.uuid = uuid;
        this.collections = ItemCollection.getDefaultCollections();
        this.coins = 0;
        this.bankCoins = 0;
        this.islandX = null;
        this.islandZ = null;
        this.lastRegion = null;
        this.quiver = new HashMap<>();
        this.effects = new ArrayList<>();
        this.farmingXP = 0.0;
        this.miningXP = 0.0;
        this.combatXP = 0.0;
        this.foragingXP = 0.0;
        this.highestSlayers = new int[3];
        this.slayerXP = new int[3];
        this.permanentCoins = false;
        if (!USER_FOLDER.exists()) USER_FOLDER.mkdirs();
        String path = uuid.toString() + ".yml";
        File configFile = new File(USER_FOLDER, path);
        boolean save = false;
        try
        {
            if (!configFile.exists())
            {
                save = true;
                configFile.createNewFile();
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        this.config = new Config(USER_FOLDER, path);
        USER_CACHE.put(uuid, this);
        if (save) save();
        load();
    }

    public void load()
    {
        this.uuid = UUID.fromString(config.getString("uuid"));
        if (config.contains("collections"))
        {
            for (String identifier : config.getConfigurationSection("collections").getKeys(false))
                this.collections.put(ItemCollection.getByIdentifier(identifier), config.getInt("collections." + identifier));
        }
        this.coins = config.getLong("coins");
        this.bankCoins = config.getLong("bankCoins");
        this.islandX = config.contains("island.x") ? config.getDouble("island.x") : null;
        this.islandZ = config.contains("island.z") ? config.getDouble("island.z") : null;
        this.lastRegion = config.getString("lastRegion") != null ? Region.get(config.getString("lastRegion")) : null;
        if (config.contains("quiver"))
        {
            for (String m : config.getConfigurationSection("quiver").getKeys(false))
                this.quiver.put(SMaterial.getMaterial(m), config.getInt("quiver." + m));
        }
        if (config.contains("effects"))
        {
            for (String key : config.getConfigurationSection("effects").getKeys(false))
            {
                this.effects.add(new ActivePotionEffect(new PotionEffect(PotionEffectType.getByNamespace(key),
                        config.getInt("effects." + key + ".level"), config.getLong("effects." + key + ".duration")),
                        config.getLong("effects." + key + ".remaining")));
            }
        }
        this.farmingXP = config.getDouble("xp.farming");
        this.miningXP = config.getDouble("xp.mining");
        this.combatXP = config.getDouble("xp.combat");
        this.foragingXP = config.getDouble("xp.foraging");
        this.highestSlayers[0] = config.getInt("slayer.revenantHorror.highest");
        this.highestSlayers[1] = config.getInt("slayer.tarantulaBroodfather.highest");
        this.highestSlayers[2] = config.getInt("slayer.svenPackmaster.highest");
        this.slayerXP[0] = config.getInt("xp.slayer.revenantHorror");
        this.slayerXP[1] = config.getInt("xp.slayer.tarantulaBroodfather");
        this.slayerXP[2] = config.getInt("xp.slayer.svenPackmaster");
        this.permanentCoins = config.getBoolean("permanentCoins");
        this.slayerQuest = (SlayerQuest) config.get("slayer.quest");
    }

    public void save()
    {
        config.set("uuid", uuid.toString());
        config.set("collections", null);
        for (Map.Entry<ItemCollection, Integer> entry : collections.entrySet())
            config.set("collections." + entry.getKey().getIdentifier(), entry.getValue());
        config.set("coins", coins);
        config.set("bankCoins", bankCoins);
        config.set("island.x", islandX);
        config.set("island.z", islandZ);
        config.set("lastRegion", lastRegion.getName());
        config.set("quiver", null);
        for (Map.Entry<SMaterial, Integer> entry : quiver.entrySet())
            config.set("quiver." + entry.getKey().name().toLowerCase(), entry.getValue());
        config.set("effects", null);
        for (ActivePotionEffect effect : effects)
        {
            PotionEffectType type = effect.getEffect().getType();
            config.set("effects." + type.getNamespace() + ".level", effect.getEffect().getLevel());
            config.set("effects." + type.getNamespace() + ".duration", effect.getEffect().getDuration());
            config.set("effects." + type.getNamespace() + ".remaining", effect.getRemaining());
        }
        config.set("xp.farming", farmingXP);
        config.set("xp.mining", miningXP);
        config.set("xp.combat", combatXP);
        config.set("xp.foraging", foragingXP);
        config.set("slayer.revenantHorror.highest", highestSlayers[0]);
        config.set("slayer.tarantulaBroodfather.highest", highestSlayers[1]);
        config.set("slayer.svenPackmaster.highest", highestSlayers[2]);
        config.set("xp.slayer.revenantHorror", slayerXP[0]);
        config.set("xp.slayer.tarantulaBroodfather", slayerXP[1]);
        config.set("xp.slayer.svenPackmaster", slayerXP[2]);
        config.set("permanentCoins", permanentCoins);
        config.set("slayer.quest", slayerQuest);
        config.save();
    }

    public void setIslandLocation(double x, double z)
    {
        this.islandX = x;
        this.islandZ = z;
    }

    public void setLastRegion(Region lastRegion)
    {
        this.lastRegion = lastRegion;
    }

    public void addCoins(long coins)
    {
        this.coins += coins;
    }

    public void subCoins(long coins)
    {
        this.coins -= coins;
    }

    public void setCoins(long coins)
    {
        this.coins = coins;
    }

    public void addBankCoins(long bankCoins)
    {
        this.bankCoins += bankCoins;
    }

    public void subBankCoins(long bankCoins)
    {
        this.bankCoins -= bankCoins;
    }

    public void setBankCoins(long bankCoins)
    {
        this.bankCoins = bankCoins;
    }

    public void addToCollection(ItemCollection collection, int amount)
    {
        int prevTier = collection.getTier(getCollection(collection));
        int i = collections.getOrDefault(collection, 0);
        collections.put(collection, i + amount);
        updateCollection(collection, prevTier);
    }

    public void addToCollection(ItemCollection collection)
    {
        addToCollection(collection, 1);
    }

    public void setCollection(ItemCollection collection, int amount)
    {
        int prevTier = collection.getTier(getCollection(collection));
        collections.put(collection, amount);
        updateCollection(collection, prevTier);
    }

    public void zeroCollection(ItemCollection collection)
    {
        int prevTier = collection.getTier(getCollection(collection));
        collections.put(collection, 0);
        updateCollection(collection, prevTier);
    }

    private void updateCollection(ItemCollection collection, int prevTier)
    {
        int tier = collection.getTier(getCollection(collection));
        if (prevTier != tier)
        {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null)
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 2f);
            StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("------------------------------------------\n");
            builder.append(ChatColor.GOLD).append(ChatColor.BOLD).append("  COLLECTION LEVEL UP ").append(ChatColor.RESET)
                    .append(ChatColor.YELLOW).append(collection.getName()).append(" ");
            if (prevTier != 0)
                builder.append(ChatColor.DARK_GRAY).append(SUtil.toRomanNumeral(prevTier)).append("➜");
            builder.append(ChatColor.YELLOW).append(SUtil.toRomanNumeral(tier)).append("\n");
            ItemCollectionRewards rewards = collection.getRewardsFor(tier);
            if (rewards != null && rewards.size() != 0)
            {
                builder.append(" \n");
                builder.append(ChatColor.GREEN).append(ChatColor.BOLD).append("  REWARD");
                if (rewards.size() != 1)
                    builder.append("S");
                builder.append(ChatColor.RESET);
                for (ItemCollectionReward reward : rewards)
                {
                    reward.onAchieve(player);
                    builder.append("\n    ").append(reward.toRewardString());
                }
            }
            builder.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("------------------------------------------");
            send(builder.toString());
        }
    }

    public int getCollection(ItemCollection collection)
    {
        return collections.get(collection);
    }

    public boolean hasCollection(ItemCollection collection, int tier)
    {
        return collection.getTier(getCollection(collection)) >= tier;
    }

    public void addToQuiver(SMaterial material, int amount)
    {
        int i = quiver.getOrDefault(material, 0);
        setQuiver(material, i + amount);
    }

    public void addToQuiver(SMaterial material)
    {
        addToQuiver(material, 1);
    }

    public void setQuiver(SMaterial material, int amount)
    {
        if (amount == 0)
        {
            quiver.remove(material);
            return;
        }
        quiver.put(material, amount);
    }

    public int getQuiver(SMaterial material)
    {
        return quiver.get(material);
    }

    public void subFromQuiver(SMaterial material, int amount)
    {
        if (!quiver.containsKey(material)) return;
        setQuiver(material, quiver.get(material) - amount);
    }

    public void subFromQuiver(SMaterial material)
    {
        subFromQuiver(material, 1);
    }

    public boolean hasQuiverItem(SMaterial material)
    {
        return quiver.containsKey(material);
    }

    public void clearQuiver()
    {
        quiver.clear();
    }

    public double getSkillXP(Skill skill)
    {
        if (skill instanceof FarmingSkill)
            return farmingXP;
        if (skill instanceof MiningSkill)
            return miningXP;
        if (skill instanceof CombatSkill)
            return combatXP;
        if (skill instanceof ForagingSkill)
            return foragingXP;
        return 0.0;
    }

    public void setSkillXP(Skill skill, double xp)
    {
        double prev = 0.0;
        if (skill instanceof FarmingSkill)
        {
            prev = this.farmingXP;
            this.farmingXP = xp;
        }
        if (skill instanceof MiningSkill)
        {
            prev = this.miningXP;
            this.miningXP = xp;
        }
        if (skill instanceof CombatSkill)
        {
            prev = this.combatXP;
            this.combatXP = xp;
        }
        if (skill instanceof ForagingSkill)
        {
            prev = this.foragingXP;
            this.foragingXP = xp;
        }
        skill.onSkillUpdate(this, prev);
    }

    public void addSkillXP(Skill skill, double xp)
    {
        setSkillXP(skill, getSkillXP(skill) + xp);
    }

    public int getHighestRevenantHorror()
    {
        return highestSlayers[0];
    }

    public void setHighestRevenantHorror(int tier)
    {
        highestSlayers[0] = tier;
    }

    public int getHighestTarantulaBroodfather()
    {
        return highestSlayers[1];
    }

    public void setHighestTarantulaBroodfather(int tier)
    {
        highestSlayers[1] = tier;
    }

    public int getHighestSvenPackmaster()
    {
        return highestSlayers[2];
    }

    public void setHighestSvenPackmaster(int tier)
    {
        highestSlayers[2] = tier;
    }

    public int getZombieSlayerXP()
    {
        return slayerXP[0];
    }

    public void setZombieSlayerXP(int xp)
    {
        slayerXP[0] = xp;
    }

    public int getSpiderSlayerXP()
    {
        return slayerXP[1];
    }

    public void setSpiderSlayerXP(int xp)
    {
        slayerXP[1] = xp;
    }

    public int getWolfSlayerXP()
    {
        return slayerXP[2];
    }

    public void setWolfSlayerXP(int xp)
    {
        slayerXP[2] = xp;
    }

    public void setSlayerXP(SlayerBossType.SlayerMobType type, int xp)
    {
        slayerXP[type.ordinal()] = xp;
    }

    public int getSlayerXP(SlayerBossType.SlayerMobType type)
    {
        return slayerXP[type.ordinal()];
    }

    public int getSlayerCombatXPBuff()
    {
        int buff = 0;
        for (int highest : highestSlayers)
            buff += (highest == 4 ? 5 : highest);
        return buff;
    }

    public void startSlayerQuest(SlayerBossType type)
    {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        this.slayerQuest = new SlayerQuest(type, System.currentTimeMillis());
        player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 2f);
        player.sendMessage("  " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "SLAYER QUEST STARTED!");
        player.sendMessage("   " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "→ " + ChatColor.GRAY + "Slay " + ChatColor.RED +
                SUtil.commaify(type.getSpawnXP()) + " Combat XP" + ChatColor.GRAY + " worth of " + type.getType().getPluralName() + ".");
    }

    public void failSlayerQuest()
    {
        if (slayerQuest == null) return;
        if (slayerQuest.getDied() != 0) return;
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        slayerQuest.setDied(System.currentTimeMillis());
        if (slayerQuest.getEntity() != null)
        {
            slayerQuest.getEntity().remove();
            slayerQuest.getEntity().getFunction().onDeath(slayerQuest.getEntity(), slayerQuest.getEntity().getEntity(), player);
        }
        SUtil.delay(() ->
        {
            player.sendMessage("  " + ChatColor.RED + ChatColor.BOLD + "SLAYER QUEST FAILED!");
            player.sendMessage("   " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "→ " + ChatColor.GRAY +
                    "You died! What a noob!");
        }, 2);
    }

    public void send(String message)
    {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        player.sendMessage(message);
    }

    // this deals TRUE TRUE damage
    public void damageEntity(LivingEntity entity, double damage)
    {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        entity.damage(0.00001);
        PlayerUtils.handleSpecEntity(entity, player, new AtomicDouble(damage));
        entity.setHealth(Math.max(0.0, entity.getHealth() - damage));
    }

    public void damageEntity(LivingEntity entity)
    {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        entity.damage(0.0, player);
    }

    public void damage(double d, EntityDamageEvent.DamageCause cause, Entity entity)
    {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        EntityHuman human = ((CraftHumanEntity) player).getHandle();
        PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
        int trueDefense = statistics.getTrueDefense().addAll();
        d = d - (d * ((double) trueDefense / (double) (trueDefense + 100)));
        if ((player.getHealth() + human.getAbsorptionHearts()) - d <= 0.0)
        {
            kill(cause, entity);
            return;
        }
        float ab = (float) Math.max(0.0, human.getAbsorptionHearts() - d);
        double actual = Math.max(0.0, d - human.getAbsorptionHearts());
        human.setAbsorptionHearts(ab);
        player.setHealth(Math.max(0.0, player.getHealth() - actual));
    }

    public void damage(double d)
    {
        damage(d, EntityDamageEvent.DamageCause.CUSTOM, null);
    }

    public void kill(EntityDamageEvent.DamageCause cause, Entity entity)
    {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        player.setHealth(player.getMaxHealth());
        for (int i = 0; i < player.getInventory().getSize(); i++)
        {
            ItemStack stack = player.getInventory().getItem(i);
            SItem sItem = SItem.find(stack);
            if (sItem == null) continue;
            if (sItem.getType() == SMaterial.REMNANT_OF_THE_EYE && (lastRegion.getType() == RegionType.THE_END || lastRegion.getType() == RegionType.DRAGONS_NEST))
            {
                player.getInventory().setItem(i, new ItemStack(Material.AIR));
                if (cause == EntityDamageEvent.DamageCause.VOID)
                    sendToSpawn();
                player.sendMessage(ChatColor.DARK_PURPLE + "Your Remnant of the Eye saved you from certain death!");
                return;
            }
        }
        player.setVelocity(new Vector(0, 0, 0));
        player.setFallDistance(0.0f);
        sendToSpawn();
        clearPotionEffects();
        String name = null;
        if (entity != null)
        {
            SEntity sEntity = entity.hasMetadata("specEntityObject") ? (SEntity) entity.getMetadata("specEntityObject").get(0).value() : null;
            name = sEntity != null ? sEntity.getStatistics().getEntityName() : entity.getCustomName();
        }
        String message = "You died.";
        String out = "%s died.";
        switch (cause)
        {
            case VOID:
            {
                message = "You fell into the void.";
                out = "%s fell into the void.";
                break;
            }
            case FALL:
            {
                message = "You fell to your death.";
                out = "%s fell to their death.";
                break;
            }
            case ENTITY_ATTACK:
            {
                message = "You were killed by " + name + ChatColor.GRAY + ".";
                out = "%s was killed by " + name + ChatColor.GRAY + ".";
                break;
            }
            case ENTITY_EXPLOSION:
            {
                message = "You were killed by " + name + ChatColor.GRAY + "'s explosion.";
                out = "%s was killed by " + name + ChatColor.GRAY + "'s explosion.";
                break;
            }
            case FIRE:
            case LAVA:
            {
                message = "You burned to death.";
                out = "%s burned to death.";
                break;
            }
            case MAGIC:
            {
                message = "You died by magic.";
                out = "%s was killed by magic.";
                break;
            }
            case POISON:
            {
                message = "You died by poisoning.";
                out = "%s was killed by poisoning.";
                break;
            }
            case LIGHTNING:
            {
                message = "You were struck by lightning and died.";
                out = "%s was struck by lightning and killed.";
                break;
            }
        }
        if (slayerQuest != null && slayerQuest.getKilled() == 0)
            failSlayerQuest();
        player.playSound(player.getLocation(), Sound.HURT_FLESH, 1f, 1f);
        player.sendMessage(ChatColor.RED + " ☠ " + ChatColor.GRAY + message);
        SUtil.broadcastExcept(ChatColor.RED + " ☠ " + ChatColor.GRAY + String.format(out, player.getName()), player);
        if ((isOnIsland() && cause == EntityDamageEvent.DamageCause.VOID) || permanentCoins)
            return;
        int piggyIndex = PlayerUtils.getSpecItemIndex(player, SMaterial.PIGGY_BANK);
        if (piggyIndex != -1 && coins >= 20000)
        {
            SItem cracked = SItem.of(SMaterial.CRACKED_PIGGY_BANK);
            SItem piggy = SItem.find(player.getInventory().getItem(piggyIndex));
            if (piggy.getReforge() != null)
                cracked.setReforge(piggy.getReforge());
            player.getInventory().setItem(piggyIndex, cracked.getStack());
            player.sendMessage(ChatColor.RED + "You died and your piggy bank cracked!");
            return;
        }
        player.playSound(player.getLocation(), Sound.ZOMBIE_METAL, 1f, 2f);
        int crackedPiggyIndex = PlayerUtils.getSpecItemIndex(player, SMaterial.CRACKED_PIGGY_BANK);
        if (crackedPiggyIndex != -1 && coins >= 20000)
        {
            SItem broken = SItem.of(SMaterial.BROKEN_PIGGY_BANK);
            SItem crackedPiggy = SItem.find(player.getInventory().getItem(crackedPiggyIndex));
            if (crackedPiggy.getReforge() != null)
                broken.setReforge(crackedPiggy.getReforge());
            player.getInventory().setItem(crackedPiggyIndex, broken.getStack());
            long sub = (long) (coins * 0.25);
            player.sendMessage(ChatColor.RED + "You died, lost " + SUtil.commaify(sub) + " coins, and your piggy bank broke!");
            coins -= sub;
            save();
            return;
        }
        long sub = coins / 2;
        player.sendMessage(ChatColor.RED + "You died and lost " + SUtil.commaify(sub) + " coins!");
        coins -= sub;
        save();
    }

    public void addPotionEffect(PotionEffect effect)
    {
        effects.add(new ActivePotionEffect(effect, effect.getDuration()));
    }

    public void removePotionEffect(PotionEffectType type)
    {
        for (ActivePotionEffect effect : effects)
        {
            if (effect.getEffect().getType() == type)
                effect.setRemaining(0);
        }
    }

    public ActivePotionEffect getPotionEffect(PotionEffectType type)
    {
        for (ActivePotionEffect effect : effects)
        {
            if (effect.getEffect().getType() == type)
                return effect;
        }
        return null;
    }

    public boolean hasPotionEffect(PotionEffectType type)
    {
        return effects.stream().filter(effect -> effect.getEffect().getType() == type).toArray().length != 0;
    }

    public void clearPotionEffects() // this kind of "sets them up" to be cleared rather than actually clearing them
    {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null)
        {
            for (org.bukkit.potion.PotionEffect effect : player.getActivePotionEffects())
                player.removePotionEffect(effect.getType());
        }
        for (ActivePotionEffect effect : effects)
            effect.setRemaining(0);
    }

    public boolean isOnIsland()
    {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null)
            return false;
        return isOnIsland(player.getLocation());
    }

    public boolean isOnIsland(Block block)
    {
        return isOnIsland(block.getLocation());
    }

    public boolean isOnIsland(Location location)
    {
        World world = Bukkit.getWorld("islands");
        if (world == null)
            return false;
        double x = location.getX();
        double z = location.getZ();
        return world.getUID().equals(location.getWorld().getUID()) &&
                x >= islandX - ISLAND_SIZE && x <= islandX + ISLAND_SIZE && z >= islandZ - ISLAND_SIZE && z <= islandZ + ISLAND_SIZE;
    }

    public boolean isOnUserIsland()
    {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null)
            return false;
        World world = Bukkit.getWorld("islands");
        if (world == null)
            return false;
        double x = player.getLocation().getX();
        double z = player.getLocation().getZ();
        return world.getUID().equals(player.getWorld().getUID()) &&
                x < islandX - ISLAND_SIZE && x > islandX + ISLAND_SIZE && z < islandZ - ISLAND_SIZE && z > islandZ + ISLAND_SIZE;
    }

    public void sendToSpawn()
    {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        if (isOnIsland())
        {
            World world = Bukkit.getWorld("islands");
            player.teleport(world.getHighestBlockAt(SUtil.blackMagic(islandX),
                    SUtil.blackMagic(islandZ)).getLocation().add(0.5, 1.0, 0.5));
        }
        else
        {
            if (lastRegion != null)
            {
                switch (lastRegion.getType())
                {
                    case BANK:
                    case FARM:
                    case RUINS:
                    case FOREST:
                    case LIBRARY:
                    case COAL_MINE:
                    case COAL_MINE_CAVES:
                    case MOUNTAIN:
                    case VILLAGE:
                    case HIGH_LEVEL:
                    case BLACKSMITH:
                    case AUCTION_HOUSE:
                    case WILDERNESS:
                    case BAZAAR_ALLEY:
                    case COLOSSEUM:
                    case GRAVEYARD:
                        player.teleport(new Location(player.getWorld(), -2.5, 70.0, -85.5, 0.0f, 0.0f));
                        break;
                    case GOLD_MINE:
                        player.teleport(new Location(player.getWorld(), -4.5, 74.0, -272.5, 180.0f, 0.0f));
                        break;
                    case DEEP_CAVERN:
                    case GUNPOWDER_MINES:
                    case LAPIS_QUARRY:
                    case PIGMENS_DEN:
                    case SLIMEHILL:
                    case DIAMOND_RESERVE:
                    case OBSIDIAN_SANCTUARY:
                        player.teleport(new Location(player.getWorld(), -4.0, 157.0, -490.5, 180.0f, 0.0f));
                        break;
                    case THE_END:
                    case THE_END_NEST:
                    case DRAGONS_NEST:
                        player.teleport(new Location(player.getWorld(), -498.5, 101.0, -275.0, 90.0f, 0.0f));
                        break;
                    case SPIDERS_DEN:
                    case SPIDERS_DEN_HIVE:
                        player.teleport(new Location(player.getWorld(), -200.5, 84.0, -231.5, 130.0f, 0.0f));
                        break;
                    case BIRCH_PARK:
                    case SPRUCE_WOODS:
                    case DARK_THICKET:
                    case SAVANNA_WOODLAND:
                    case JUNGLE_ISLAND:
                        player.teleport(new Location(player.getWorld(), -276.5, 82.0, -13.5, 90.0f, 0.0f));
                        break;
                    case HOWLING_CAVE:
                        player.teleport(new Location(player.getWorld(), -331.5, 90.0, -55.5, 100.0f, 25.0f));
                        break;
                    case BLAZING_FORTRESS:
                        player.teleport(new Location(player.getWorld(), -310.0, 83.0, -379.5, -180.0f, 0.0f));
                        break;
                    default:
                        player.teleport(player.getWorld().getSpawnLocation());
                        break;
                }
            }
            else
                player.teleport(player.getWorld().getSpawnLocation());
        }
    }

    public static User getUser(UUID uuid)
    {
        if (USER_CACHE.containsKey(uuid)) return USER_CACHE.get(uuid);
        return new User(uuid);
    }

    public static Collection<User> getCachedUsers()
    {
        return USER_CACHE.values();
    }
}
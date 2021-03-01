package me.superischroma.spectaculation.listener;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.entity.SEntity;
import me.superischroma.spectaculation.entity.SlimeStatistics;
import me.superischroma.spectaculation.entity.nms.Dragon;
import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.region.Region;
import me.superischroma.spectaculation.region.RegionType;
import me.superischroma.spectaculation.skill.ForagingSkill;
import me.superischroma.spectaculation.skill.MiningSkill;
import me.superischroma.spectaculation.skill.Skill;
import me.superischroma.spectaculation.user.PlayerUtils;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.Groups;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class WorldListener extends PListener
{
    private static final Map<UUID, List<BlockState>> RESTORER = new HashMap<>();

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e)
    {
        if (e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) return;
        if (e.getEntity() instanceof FallingBlock) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent e)
    {
        if (e.getEntity().getType() == EntityType.ENDERMAN)
            e.setCancelled(true);
        if (e.getBlock().getType() == Material.SOIL && e.getTo() == Material.DIRT)
            e.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e)
    {
        Entity entity = e.getEntity();
        if (entity instanceof EnderDragonPart || entity instanceof EnderDragon || entity instanceof Creeper)
            e.blockList().clear();
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e)
    {
        if (e.getIgnitingEntity() instanceof Fireball)
            e.setCancelled(true);
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent e)
    {
        if (e.getNewState().getType() == Material.DIRT || e.getNewState().getType() == Material.GRASS)
            e.setCancelled(true);
    }

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        Block block = e.getBlock();
        Player player = e.getPlayer();
        User user = User.getUser(player.getUniqueId());
        SMaterial equiv = SMaterial.getSpecEquivalent(block.getType(), block.getData());
        Region region = Region.getRegionOfBlock(block);
        Collection<ItemStack> drops = block.getDrops(e.getPlayer().getItemInHand());
        if (player.getGameMode() != GameMode.CREATIVE)
        {
            boolean allowBreak = false;
            if (region != null)
            {
                if (Groups.FORAGING_REGIONS.contains(region.getType()))
                {
                    if (block.getType() == Material.LOG || block.getType() == Material.LOG_2 || block.getType() == Material.LEAVES ||
                            block.getType() == Material.LEAVES_2)
                    {
                        allowBreak = true;
                        int level = Skill.getLevel(user.getSkillXP(ForagingSkill.INSTANCE), ForagingSkill.INSTANCE.hasSixtyLevels());
                        double d = ForagingSkill.INSTANCE.getDoubleDropChance(level);
                        double t = ForagingSkill.INSTANCE.getTripleDropChance(level);
                        if (SUtil.random(0.0, 1.0) < t)
                            block.getWorld().dropItemNaturally(block.getLocation().clone().add(0.5, 0.5, 0.5), SUtil.setStackAmount(SItem.of(equiv).getStack(), 2));
                        else if (SUtil.random(0.0, 1.0) < d)
                            block.getWorld().dropItemNaturally(block.getLocation().clone().add(0.5, 0.5, 0.5), SItem.of(equiv).getStack());
                        addToRestorer(block, player);
                    }
                }
                if (Groups.FARMING_REGIONS.contains(region.getType()))
                {
                    if (block.getType() == Material.SEEDS || block.getType() == Material.WHEAT ||
                            block.getType() == Material.CROPS || block.getType() == Material.CARROT ||
                            block.getType() == Material.POTATO)
                    {
                        allowBreak = true;
                    }
                }
                if (Groups.MINING_REGIONS.contains(region.getType()))
                {
                    Material type = block.getType();
                    switch (type)
                    {
                        case COAL_ORE:
                        case DIAMOND_BLOCK:
                        case DIAMOND_ORE:
                        case EMERALD_ORE:
                        case GOLD_ORE:
                        case IRON_ORE:
                        case LAPIS_ORE:
                        case REDSTONE_ORE:
                        {
                            block.setType(Material.STONE);
                            break;
                        }
                        case STONE:
                        {
                            if (block.getData() != 0)
                                break;
                            block.setType(Material.COBBLESTONE);
                            break;
                        }
                        case OBSIDIAN:
                        case ENDER_STONE:
                        case NETHERRACK:
                        case COBBLESTONE:
                        {
                            block.setType(Material.BEDROCK);
                            regenerateLater(block, 3 * 20, region.getType());
                            break;
                        }
                    }
                    if (type != block.getType())
                    {
                        e.setCancelled(true);
                        if (equiv.getStatistics() instanceof ExperienceRewardStatistics)
                            Skill.reward(((ExperienceRewardStatistics) equiv.getStatistics()).getRewardedSkill(), ((ExperienceRewardStatistics) equiv.getStatistics()).getRewardXP(), player);
                        int level = Skill.getLevel(user.getSkillXP(MiningSkill.INSTANCE), MiningSkill.INSTANCE.hasSixtyLevels());
                        double d = MiningSkill.INSTANCE.getDoubleDropChance(level);
                        double t = MiningSkill.INSTANCE.getTripleDropChance(level);
                        for (ItemStack drop : drops)
                        {
                            int amount = 1;
                            if (SUtil.random(0.0, 1.0) < t)
                                amount = 3;
                            else if (SUtil.random(0.0, 1.0) < d)
                                amount = 2;
                            block.getWorld().dropItemNaturally(block.getLocation().clone().add(0.5, 0.5, 0.5),
                                    SUtil.setStackAmount(drop, amount));
                        }
                    }
                    if (block.getType() == Material.GLOWSTONE)
                    {
                        allowBreak = true;
                        addToRestorer(block, player);
                    }
                }
            }
            if (!allowBreak)
                e.setCancelled(true);
        }
        if (equiv.getStatistics() instanceof ExperienceRewardStatistics && !e.isCancelled())
            Skill.reward(((ExperienceRewardStatistics) equiv.getStatistics()).getRewardedSkill(), ((ExperienceRewardStatistics) equiv.getStatistics()).getRewardXP(), player);
        SBlock sBlock = SBlock.getBlock(e.getBlock().getLocation());
        if (sBlock != null && !e.isCancelled())
            sBlock.delete();
        if (e.isCancelled() || player.getGameMode() == GameMode.CREATIVE)
            return;
        e.setCancelled(true);
        for (ItemStack drop : drops)
        {
            SItem conv = SItem.convert(drop);
            conv.setOrigin(ItemOrigin.NATURAL_BLOCK);
            block.getWorld().dropItemNaturally(block.getLocation().clone().add(0.5, 0.5, 0.5),
                    conv.getStack());
        }
        block.setType(Material.AIR);
    }

    @EventHandler
    public void onFarmlandDecay(BlockPhysicsEvent e)
    {
        if (e.getChangedType() == Material.SOIL)
            e.setCancelled(true);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent e)
    {
        Entity entity = e.getEntity();
        if (!entity.hasMetadata("specEntityObject")) return;
        SEntity sEntity = (SEntity) entity.getMetadata("specEntityObject").get(0).value();
        sEntity.getFunction().onTarget(sEntity, e);
        if (!(sEntity.getGenericInstance() instanceof Dragon)) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPortalEnter(EntityPortalEnterEvent e)
    {
        Material portalType = e.getLocation().getBlock().getType();
        Entity entity = e.getEntity();
        if (portalType == Material.PORTAL)
        {
            World hub = Bukkit.getWorld(!plugin.config.getString("hub_world").isEmpty() ? plugin.config.getString("hub_world") : "hub");
            if (hub == null)
            {
                entity.sendMessage(ChatColor.RED + "Could not find a hub world to teleport you to!");
                return;
            }
            entity.sendMessage(ChatColor.GRAY + "Sending you to the hub...");
            entity.teleport(hub.getSpawnLocation());
            return;
        }
        if (!(entity instanceof Player)) return;
        entity.sendMessage(ChatColor.GRAY + "Sending you to your island...");
        PlayerUtils.sendToIsland((Player) entity);
    }

    @EventHandler
    public void onPortalCreate(EntityCreatePortalEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onSlimeSplit(SlimeSplitEvent e)
    {
        Slime slime = e.getEntity();
        if (slime.hasMetadata("specEntityObject"))
        {
            List<MetadataValue> values = slime.getMetadata("specEntityObject");
            SEntity sEntity = (SEntity) values.get(0).value();
            if (sEntity.getStatistics() instanceof SlimeStatistics && !((SlimeStatistics) sEntity.getStatistics()).split())
                e.setCancelled(true);
        }
    }

    private static void addToRestorer(Block block, Player player)
    {
        if (RESTORER.containsKey(player.getUniqueId()))
            RESTORER.get(player.getUniqueId()).add(block.getState());
        else
        {
            RESTORER.put(player.getUniqueId(), new ArrayList<>());
            RESTORER.get(player.getUniqueId()).add(block.getState());
            new BukkitRunnable()
            {
                public void run()
                {
                    for (BlockState state : RESTORER.get(player.getUniqueId()))
                    {
                        state.getBlock().setType(state.getType());
                        state.setRawData(state.getRawData());
                        state.update();
                    }
                    RESTORER.remove(player.getUniqueId());
                }
            }.runTaskLater(Spectaculation.getPlugin(), 60 * 20);
        }
    }

    private static BukkitTask regenerateLater(Block block, long ticks, RegionType type)
    {
        return new BukkitRunnable()
        {
            public void run()
            {
                if (block.getType() != Material.BEDROCK)
                    return;
                int r5 = SUtil.random(1, 5);
                switch (type)
                {
                    case COAL_MINE:
                    {
                        if (SUtil.random(1, 15) == 1)
                        {
                            block.setType(Material.COAL_ORE);
                            break;
                        }
                        block.setType(Material.STONE);
                        break;
                    }
                    case GOLD_MINE:
                    case GUNPOWDER_MINES:
                    {
                        if (SUtil.random(1, 20) == 1)
                        {
                            block.setType(Material.GOLD_ORE);
                            break;
                        }
                        if (r5 == 1)
                        {
                            block.setType(Material.IRON_ORE);
                            break;
                        }
                        block.setType(Material.STONE);
                        break;
                    }
                    case LAPIS_QUARRY:
                    {
                        if (r5 == 1)
                        {
                            block.setType(Material.LAPIS_ORE);
                            break;
                        }
                        block.setType(Material.STONE);
                        break;
                    }
                    case PIGMENS_DEN:
                    {
                        if (r5 == 1)
                        {
                            block.setType(Material.REDSTONE_ORE);
                            break;
                        }
                        block.setType(Material.STONE);
                        break;
                    }
                    case SLIMEHILL:
                    {
                        if (r5 == 1)
                        {
                            block.setType(Material.EMERALD_ORE);
                            break;
                        }
                        block.setType(Material.STONE);
                        break;
                    }
                    case DIAMOND_RESERVE:
                    {
                        if (r5 == 1)
                        {
                            block.setType(Material.DIAMOND_ORE);
                            break;
                        }
                        block.setType(Material.STONE);
                        break;
                    }
                    case OBSIDIAN_SANCTUARY:
                    {
                        if (SUtil.random(1, 40) == 1)
                        {
                            block.setType(Material.DIAMOND_BLOCK);
                            break;
                        }
                        if (SUtil.random(1, 30) == 1)
                        {
                            block.setType(Material.OBSIDIAN);
                            break;
                        }
                        if (r5 == 1)
                        {
                            block.setType(Material.DIAMOND_ORE);
                            break;
                        }
                        block.setType(Material.STONE);
                        break;
                    }
                    case THE_END:
                    case DRAGONS_NEST:
                    {
                        block.setType(Material.ENDER_STONE);
                        break;
                    }
                    case BLAZING_FORTRESS:
                    {
                        block.setType(Material.NETHERRACK);
                        break;
                    }
                    default:
                    {
                        block.setType(Material.STONE);
                        break;
                    }
                }
            }
        }.runTaskLater(Spectaculation.getPlugin(), ticks);
    }

}
package me.superischroma.spectaculation;

import me.superischroma.spectaculation.entity.StaticDragonManager;
import me.superischroma.spectaculation.item.Ownable;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.item.SpecificItemType;
import me.superischroma.spectaculation.item.armor.ArmorSet;
import me.superischroma.spectaculation.item.armor.TickingSet;
import me.superischroma.spectaculation.potion.ActivePotionEffect;
import me.superischroma.spectaculation.region.Region;
import me.superischroma.spectaculation.region.RegionType;
import me.superischroma.spectaculation.sidebar.Sidebar;
import me.superischroma.spectaculation.slayer.SlayerQuest;
import me.superischroma.spectaculation.user.*;
import me.superischroma.spectaculation.util.SUtil;
import net.minecraft.server.v1_8_R3.EntityHuman;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Repeater
{
    public static final Map<UUID, Integer> MANA_MAP = new HashMap<>();
    public static final Map<UUID, DefenseReplacement> DEFENSE_REPLACEMENT_MAP = new HashMap<>();

    private final List<BukkitTask> tasks;
    private final List<AtomicInteger> counters;

    public Repeater()
    {
        this.tasks = new ArrayList<>();
        this.counters = new ArrayList<>(5);
        counters.add(new AtomicInteger());
        int[] counters = {0, 0};
        this.tasks.add(new BukkitRunnable()
        {
            public void run()
            {
                SkyBlockCalendar.ELAPSED += 10L;
                for (Player player : Bukkit.getOnlinePlayers())
                {
                    PlayerInventory inventory = player.getInventory();

                    // Some Default Stuff
                    player.setSaturation(1000.0f);
                    player.setFoodLevel(20);
                    UUID uuid = player.getUniqueId();

                    // Add to Statistics Cache if not found
                    if (!PlayerUtils.STATISTICS_CACHE.containsKey(uuid)) PlayerUtils.STATISTICS_CACHE.put(uuid, PlayerUtils.getStatistics(player));
                    PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(uuid);

                    int manaPool = 100 + statistics.getIntelligence().addAll();

                    // Hand Validation and Hand Statistics
                    SItem hand = SItem.find(inventory.getItemInHand());
                    if (hand == null)
                    {
                        hand = SItem.of(inventory.getItemInHand());
                        if (hand != null)
                            player.setItemInHand(hand.getStack());
                    }
                    PlayerUtils.updateHandStatistics(hand, statistics);
                    User user = User.getUser(player.getUniqueId());
                    for (ActivePotionEffect effect : user.getEffects())
                        effect.setRemaining(effect.getRemaining() - 10);
                    PlayerUtils.updatePotionEffects(user, statistics);

                    if (hand != null)
                    {
                        if (hand.getType().getGenericInstance() instanceof Ownable)
                            hand.getData().setString("owner", player.getUniqueId().toString());
                        hand.update();
                        // Quiver Arrows
                        SItem last = SItem.find(inventory.getItem(8));
                        if (hand.getType().getStatistics().getSpecificType() == SpecificItemType.BOW && user.hasQuiverItem(SMaterial.ARROW)
                                && (last == null || last.getType() != SMaterial.QUIVER_ARROW))
                        {
                            inventory.setItem(8, SUtil.setStackAmount(SItem.of(SMaterial.QUIVER_ARROW).getStack(), Math.min(64, user.getQuiver(SMaterial.ARROW))));
                        }
                        if (hand.getType().getStatistics().getSpecificType() != SpecificItemType.BOW)
                            inventory.setItem(8, SItem.of(SMaterial.SKYBLOCK_MENU).getStack());
                    }
                    else
                        inventory.setItem(8, SItem.of(SMaterial.SKYBLOCK_MENU).getStack());

                    // Mana Check
                    if (!MANA_MAP.containsKey(uuid)) MANA_MAP.put(uuid, manaPool);

                    // Mana Addition
                    if (counters[0] == 2)
                    {
                        int mana = MANA_MAP.get(uuid);
                        if (mana <= manaPool)
                        {
                            MANA_MAP.remove(uuid);
                            MANA_MAP.put(uuid, Math.min(manaPool, Math.min(manaPool, mana + (manaPool / 50) +
                                    (int) ((manaPool / 50) * statistics.getManaRegenerationPercentBonus()))));
                        }
                    }

                    // Health Addition
                    if (counters[1] == 4)
                    {
                        if (player.getHealth() <= player.getMaxHealth())
                        {
                            player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + 1.5 + ((int) player.getMaxHealth() * 0.01) +
                                    ((1.5 + ((int) player.getMaxHealth() * 0.01)) * statistics.getHealthRegenerationPercentBonus())));
                        }
                    }

                    // Update Set
                    PlayerUtils.updateSetStatistics(player, SItem.find(inventory.getHelmet()),
                            SItem.find(inventory.getChestplate()), SItem.find(inventory.getLeggings()), SItem.find(inventory.getBoots()), statistics);

                    // Set Health Statistics
                    EntityHuman human = ((CraftHumanEntity) player).getHandle();
                    double health = statistics.getMaxHealth().addAll();
                    player.setHealthScale(Math.min(40.0, 20.0 + ((health - 100.0) / 25.0)));

                    // Set Speed Statistics
                    int defense = statistics.getDefense().addAll();
                    player.setWalkSpeed(Math.min((float) (statistics.getSpeed().addAll() / 5.0), 1.0f));

                    // Display Action Bar
                    float absorption = human.getAbsorptionHearts();
                    ChatColor color = absorption > 0.0f ? ChatColor.GOLD : ChatColor.RED;
                    DefenseReplacement replacement = DEFENSE_REPLACEMENT_MAP.get(player.getUniqueId());
                    if (replacement != null && System.currentTimeMillis() >= replacement.getEnd())
                    {
                        DEFENSE_REPLACEMENT_MAP.remove(player.getUniqueId());
                        replacement = null;
                    }
                    SUtil.sendActionBar(player, color + "" + Math.round(player.getHealth() + absorption)
                            + "/" + statistics.getMaxHealth().addAll() + "❤     " +
                            (replacement == null ? (defense != 0 ? "" + ChatColor.GREEN + defense + "❈ Defense     " : "") :
                                    replacement.getReplacement() + "     ") +
                            ChatColor.AQUA + MANA_MAP.get(player.getUniqueId()) + "/" + manaPool + "✎ Mana");

                    // Ticking Armor Sets
                    statistics.zeroAll(PlayerStatistic.MINER_BUFF);
                    ArmorSet set = SMaterial.getIncompleteArmorSet(inventory);
                    if (set instanceof TickingSet)
                        ((TickingSet) set).tick(player, SItem.find(inventory.getHelmet()), SItem.find(inventory.getChestplate()), SItem.find(inventory.getLeggings()), SItem.find(inventory.getBoots()), Repeater.this.counters);

                    // Sidebar
                    Sidebar sidebar = new Sidebar("" + ChatColor.YELLOW + ChatColor.BOLD + "SKYBLOCK", "SKYBLOCK");
                    sidebar.add(ChatColor.GRAY + SUtil.getDate());
                    sidebar.add("  ");
                    sidebar.add(" " + SkyBlockCalendar.getMonthName() + " " + SUtil.ntify(SkyBlockCalendar.getDay()));
                    boolean day = true;
                    int time = (int) ((SkyBlockCalendar.ELAPSED % 24000) - 6000);
                    if (time < 0)
                        time += 24000;
                    int hours = 6 + (time / 1000);
                    int minutes = (int) ((time % ((hours - 6) * 1000.0)) / 16.66666);
                    String sMin = String.valueOf(minutes);
                    minutes = minutes - Integer.parseInt(sMin.substring(sMin.length() - 1));
                    if (hours >= 24) hours -= 24;
                    if (hours <= 6 || hours >= 20) day = false;
                    sidebar.add(ChatColor.GRAY + " " + (hours > 12 ? hours - 12 : (hours == 0 ? 12 : hours)) + ":" + SUtil.zeroed(minutes) +
                            (hours >= 12 ? "pm" : "am") + " " + (day ? ChatColor.YELLOW + "☀" : ChatColor.AQUA + "☽"));
                    String location = "None";
                    Region region = Region.getAreaOfEntity(player);
                    if (region != null)
                    {
                        user.setLastRegion(region);
                        if (region.getType().getName() != null)
                            location = region.getType().getColor() + region.getType().getName();
                    }
                    if (user.isOnIsland())
                        location = ChatColor.GREEN + "Your Island";
                    if (user.isOnUserIsland())
                        location = ChatColor.AQUA + "Unknown Island";
                    sidebar.add(ChatColor.GRAY + " ⏣ " + location);
                    sidebar.add(" ");
                    StringBuilder coinsDisplay = new StringBuilder();
                    if (user.isPermanentCoins())
                        coinsDisplay.append("Perma: ");
                    else if (PlayerUtils.hasItem(player, SMaterial.PIGGY_BANK))
                        coinsDisplay.append("Piggy: ");
                    else
                        coinsDisplay.append("Purse: ");
                    sidebar.add(coinsDisplay.append(ChatColor.GOLD).append(SUtil.commaify(user.getCoins())).toString());
                    sidebar.add("   ");
                    SlayerQuest quest = user.getSlayerQuest();
                    if (quest != null && quest.getDied() == 0)
                    {
                        sidebar.add("Slayer Quest");
                        sidebar.add(quest.getType().getDisplayName());
                        if (quest.getKilled() != 0)
                            sidebar.add(ChatColor.GREEN + "Boss slain!");
                        else if (quest.getXp() >= quest.getType().getSpawnXP())
                            sidebar.add(ChatColor.YELLOW + "Slay the boss!");
                        else if (quest.getLastKilled() != null)
                        {
                            sidebar.add(ChatColor.YELLOW + " " +
                                    SUtil.commaify((int) (quest.getXp() / quest.getLastKilled().getStatistics().getXPDropped())) + ChatColor.GRAY + "/" +
                                    ChatColor.RED + SUtil.commaify((int) (quest.getType().getSpawnXP() / quest.getLastKilled().getStatistics().getXPDropped())) +
                                    ChatColor.GRAY + " Kills");
                        }
                        else
                        {
                            sidebar.add(ChatColor.GRAY + " (" + ChatColor.YELLOW + SUtil.commaify((int) quest.getXp()) + ChatColor.GRAY +
                                    "/" + ChatColor.RED + SUtil.commaify(quest.getType().getSpawnXP()) + ChatColor.GRAY + ") Combat XP");
                        }
                        sidebar.add("    ");
                    }
                    if (StaticDragonManager.ACTIVE && StaticDragonManager.DRAGON != null && user.getLastRegion() != null &&
                            (user.getLastRegion().getType() == RegionType.THE_END || user.getLastRegion().getType() == RegionType.THE_END_NEST ||
                                    user.getLastRegion().getType() == RegionType.DRAGONS_NEST))
                    {
                        sidebar.add("Dragon HP: " + ChatColor.GREEN + SUtil.commaify((int) StaticDragonManager.DRAGON.getEntity().getHealth())
                                + ChatColor.RED + "❤");
                        if (StaticDragonManager.DRAGON.getDamageDealt().containsKey(uuid))
                        {
                            double dealt = StaticDragonManager.DRAGON.getDamageDealt().get(uuid);
                            sidebar.add("Your Damage: " + ChatColor.RED + SUtil.commaify(SUtil.roundTo(dealt, 1)));
                        }
                        sidebar.add("     ");
                    }
                    sidebar.add(ChatColor.YELLOW + "recreation");
                    sidebar.apply(player);
                }
                counters[0]++;
                counters[1]++;
                if (counters[0] == 3)
                    counters[0] = 1;
                if (counters[1] == 5)
                    counters[1] = 1;
            }
        }.runTaskTimer(Spectaculation.getPlugin(), 0, 10));
    }

    public void stop()
    {
        for (BukkitTask task : this.tasks)
            task.cancel();
    }

    public interface DefenseReplacement
    {
        String getReplacement();
        long getEnd();
    }
}
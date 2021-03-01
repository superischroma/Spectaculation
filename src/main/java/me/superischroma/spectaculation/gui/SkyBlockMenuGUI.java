package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.user.ItemCollection;
import me.superischroma.spectaculation.user.PlayerStatistics;
import me.superischroma.spectaculation.user.PlayerUtils;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class SkyBlockMenuGUI extends GUI
{
    public SkyBlockMenuGUI()
    {
        super("SkyBlock Menu", 54);
    }

    @Override
    public void onOpen(GUIOpenEvent e)
    {
        fill(BLACK_STAINED_GLASS_PANE);
        Player player = e.getPlayer();
        User user = User.getUser(player.getUniqueId());
        PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
        set(GUIClickableItem.getCloseItem(49));
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                GUIType.SKYBLOCK_PROFILE.getGUI().open((Player) e.getWhoClicked());
            }

            @Override
            public int getSlot()
            {
                return 13;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getSkullStack(ChatColor.GREEN + "Your SkyBlock Profile", player.getName(), 1,
                        ChatColor.RED + "  ❤ Health " + ChatColor.WHITE + SUtil.commaify(statistics.getMaxHealth().addAll()) + " HP",
                        ChatColor.GREEN + "  ❈ Defense " + ChatColor.WHITE + SUtil.commaify(statistics.getDefense().addAll()),
                        ChatColor.RED + "  ❁ Strength " + ChatColor.WHITE + SUtil.commaify(statistics.getStrength().addAll()),
                        ChatColor.WHITE + "  ✦ Speed " + SUtil.commaify(((Double) (statistics.getSpeed().addAll() * 100.0)).intValue()),
                        ChatColor.BLUE + "  ☣ Crit Chance " + ChatColor.WHITE + SUtil.commaify(((Double) (statistics.getCritChance().addAll() * 100.0)).intValue()) + "%",
                        ChatColor.BLUE + "  ☠ Crit Damage " + ChatColor.WHITE + SUtil.commaify(((Double) (statistics.getCritDamage().addAll() * 100.0)).intValue()) + "%",
                        ChatColor.AQUA + "  ✎ Intelligence " + ChatColor.WHITE + SUtil.commaify(statistics.getIntelligence().addAll()),
                        ChatColor.YELLOW + "  ⚔ Bonus Attack Speed " + ChatColor.RED + "✗",
                        ChatColor.DARK_AQUA + "  α Sea Creature Chance " + ChatColor.RED + "✗",
                        ChatColor.LIGHT_PURPLE + "  ♣ Pet Luck " + ChatColor.RED + "✗",
                        " ",
                        ChatColor.YELLOW + "Click to view your profile!");
            }
        });
        AtomicInteger completed = new AtomicInteger();
        Collection<ItemCollection> collections = ItemCollection.getCollections();
        for (ItemCollection collection : collections)
        {
            if (user.getCollection(collection) > 0)
                completed.incrementAndGet();
        }
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                GUIType.COLLECTION_MENU.getGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 20;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Collection", Material.PAINTING, (short) 0, 1,
                        ChatColor.GRAY + "View all of the items available",
                        ChatColor.GRAY + "in SkyBlock. Collect more of an",
                        ChatColor.GRAY + "item to unlock rewards on your",
                        ChatColor.GRAY + "way to becoming a master of",
                        ChatColor.GRAY + "SkyBlock!",
                        " ",
                        SUtil.createProgressText("Collection Unlocked", completed.get(), collections.size()),
                        SUtil.createLineProgressBar(20, ChatColor.DARK_GREEN, completed.get(), collections.size()),
                        " ",
                        ChatColor.YELLOW + "Click to view!");
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                GUIType.CRAFTING_TABLE.getGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 31;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Crafting Table", Material.WORKBENCH, (short) 0, 1,
                        ChatColor.GRAY + "Opens the crafting grid.",
                        " ",
                        ChatColor.YELLOW + "Click to open!");
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                GUIType.QUIVER.getGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 44;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getSkullURLStack(ChatColor.GREEN + "Quiver", "1f8405116c1daa7ce2f012591458d50246d0a467bcb95a5a2c033aefd6008b63", 1,
                        ChatColor.GRAY + "A masterfully crafted Quiver",
                        ChatColor.GRAY + "which holds any kind of",
                        ChatColor.GRAY + "projectile you can think of!",
                        " ",
                        ChatColor.YELLOW + "Click to open!");
            }
        });
    }
}
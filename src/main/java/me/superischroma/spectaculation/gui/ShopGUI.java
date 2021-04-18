package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SpecificItemType;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.PaginationList;
import me.superischroma.spectaculation.util.SLog;
import me.superischroma.spectaculation.util.SUtil;
import me.superischroma.spectaculation.util.StackArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ShopGUI extends GUI
{
    private static final Map<UUID, StackArrayList<SItem>> BUYBACK_HISTORY = new HashMap<>();

    private static final int[] INTERIOR = new int[]{
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    private final SItem[] items;
    private int page;

    protected ShopGUI(String title, int page, SItem... items)
    {
        super(title, 54);
        this.page = page;
        this.items = items;
    }

    protected ShopGUI(String name, SItem... items)
    {
        this(name, 1, items);
    }

    @Override
    public void onOpen(GUIOpenEvent e)
    {
        Player player = e.getPlayer();
        User user = User.getUser(player.getUniqueId());
        border(BLACK_STAINED_GLASS_PANE);
        PaginationList<SItem> paged = new PaginationList<>(28);
        paged.addAll(items);
        if (paged.size() == 0) page = 0;
        int finalPage = page;
        if (page > 1)
        {
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    new ShopGUI(title, finalPage - 1, items).open((Player) e.getWhoClicked());
                }
                @Override
                public int getSlot()
                {
                    return 45;
                }
                @Override
                public ItemStack getItem()
                {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "<-");
                }
            });
        }
        if (page != paged.getPageCount())
        {
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    new ShopGUI(title, finalPage + 1, items).open((Player) e.getWhoClicked());
                }
                @Override
                public int getSlot()
                {
                    return 53;
                }
                @Override
                public ItemStack getItem()
                {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "->");
                }
            });
        }
        UUID uuid = player.getUniqueId();
        StackArrayList<SItem> buyback = BUYBACK_HISTORY.get(uuid);
        set(new GUIClickableItem()
        {
            @Override
            public int getSlot()
            {
                return 49;
            }

            @Override
            public void run(InventoryClickEvent e)
            {
                if (!BUYBACK_HISTORY.containsKey(uuid) || BUYBACK_HISTORY.get(player.getUniqueId()).size() == 0)
                    return;
                long value = buyback.last().getType().getStatistics().getValue() * buyback.last().getStack().getAmount();
                if (value > user.getCoins())
                {
                    player.sendMessage(ChatColor.RED + "You don't have enough coins!");
                    return;
                }
                Map<Integer, ItemStack> m = player.getInventory().addItem(buyback.pop().getStack());
                if (m.size() != 0)
                {
                    player.sendMessage(ChatColor.RED + "Free up inventory space to purchase this!");
                    return;
                }
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                user.subCoins(value);
                if (buyback.isEmpty())
                    BUYBACK_HISTORY.remove(uuid);
                new ShopGUI(title, page, items).open(player);
            }

            @Override
            public ItemStack getItem()
            {
                if (!BUYBACK_HISTORY.containsKey(uuid) || BUYBACK_HISTORY.get(player.getUniqueId()).size() == 0)
                {
                    return SUtil.getStack(ChatColor.GREEN + "Sell Item", Material.HOPPER, (short) 0, 1,
                            ChatColor.GRAY + "Click items in your inventory to",
                            ChatColor.GRAY + "sell them to this Shop!");
                }
                SItem last = buyback.last().clone();
                ItemMeta meta = last.getStack().getItemMeta();
                List<String> lore = meta.getLore();
                lore.add(" ");
                lore.add(ChatColor.GRAY + "Cost");
                long price = last.getType().getStatistics().getValue() * last.getStack().getAmount();
                lore.add(ChatColor.GOLD + SUtil.commaify(price) + " Coin" + (price != 1 ? "s" : ""));
                lore.add(" ");
                lore.add(ChatColor.YELLOW + "Click to buyback!");
                meta.setLore(lore);
                last.getStack().setItemMeta(meta);
                return last.getStack();
            }
        });
        List<SItem> p = paged.getPage(page);
        if (p == null) return;
        for (int i = 0; i < p.size(); i++)
        {
            int slot = INTERIOR[i];
            SItem item = p.get(i).clone();
            ItemMeta meta = item.getStack().getItemMeta();
            if (item.getStack().getAmount() != 1)
                meta.setDisplayName(meta.getDisplayName() + ChatColor.DARK_GRAY + " x" + item.getStack().getAmount());
            List<String> lore = meta.getLore();
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Cost");
            long price = item.getType().getStatistics().getPrice() * item.getStack().getAmount();
            lore.add(ChatColor.GOLD + SUtil.commaify(price) + " Coin" + (price != 1 ? "s" : ""));
            lore.add(" ");
            lore.add(ChatColor.YELLOW + "Click to trade!");
            SpecificItemType type = item.getType().getStatistics().getSpecificType();
            if (type == null || type.isStackable())
                lore.add(ChatColor.YELLOW + "Right-Click for more trading options!");
            meta.setLore(lore);
            item.getStack().setItemMeta(meta);
            int finalI = i;
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    if ((type == null || type.isStackable()) && e.getClick() == ClickType.RIGHT)
                    {
                        new ShopTradingOptionsGUI(p.get(finalI), ShopGUI.this).open(player);
                        return;
                    }
                    if (price > user.getCoins())
                    {
                        player.sendMessage(ChatColor.RED + "You don't have enough coins!");
                        return;
                    }
                    Map<Integer, ItemStack> m = player.getInventory().addItem(p.get(finalI).clone().getStack());
                    if (m.size() != 0)
                    {
                        player.sendMessage(ChatColor.RED + "Free up inventory space to purchase this!");
                        return;
                    }
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 2f);
                    user.subCoins(price);
                }

                @Override
                public int getSlot()
                {
                    return slot;
                }

                @Override
                public ItemStack getItem()
                {
                    return item.getStack();
                }
            });
        }
    }

    @Override
    public void onBottomClick(InventoryClickEvent e)
    {
        ItemStack current = e.getCurrentItem();
        if (current == null) return;
        if (current.getType() == Material.AIR) return;
        SItem item = SItem.find(current);
        if (item == null)
            item = SItem.convert(current);
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        User user = User.getUser(player.getUniqueId());
        StackArrayList<SItem> buyback = BUYBACK_HISTORY.get(player.getUniqueId());
        if (buyback == null)
        {
            BUYBACK_HISTORY.put(player.getUniqueId(), new StackArrayList<>());
            buyback = BUYBACK_HISTORY.get(player.getUniqueId());
        }
        buyback.push(item.clone());
        long value = item.getType().getStatistics().getValue() * item.getStack().getAmount();
        user.addCoins(value);
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 2f);
        player.sendMessage(ChatColor.GREEN + "You sold " + item.getFullName() +
                ChatColor.DARK_GRAY + " x" + item.getStack().getAmount() + ChatColor.GREEN + " for " + ChatColor.GOLD + value + " Coin" + (value != 1 ? "s" : "") +
                ChatColor.GREEN + "!");
        player.getInventory().setItem(e.getSlot(), new ItemStack(Material.AIR));
        new ShopGUI(title, page, items).open(player);
    }
}
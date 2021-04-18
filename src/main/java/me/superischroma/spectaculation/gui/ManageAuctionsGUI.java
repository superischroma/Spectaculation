package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.auction.AuctionBid;
import me.superischroma.spectaculation.auction.AuctionItem;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ManageAuctionsGUI extends GUI
{
    private static final int[] INTERIOR = new int[]{
            10, 11, 12, 13, 14, 15, 16
    };

    private Sort sort;

    public ManageAuctionsGUI(Sort sort)
    {
        super("Manage Auctions", 27);
        this.sort = sort;
        border(BLACK_STAINED_GLASS_PANE);
    }

    public ManageAuctionsGUI()
    {
        this(Sort.RECENTLY_UPDATED);
    }

    @Override
    public void onOpen(GUIOpenEvent e)
    {
        Player player = e.getPlayer();
        User user = User.getUser(player.getUniqueId());
        Stream<AuctionItem> stream = user.getAuctions().stream();
        switch (sort)
        {
            case RECENTLY_UPDATED:
            {
                stream = stream.sorted((i1, i2) ->
                {
                    AuctionBid b1 = i1.getRecentBid();
                    AuctionBid b2 = i2.getRecentBid();
                    if (b1 == null)
                        return 1;
                    if (b2 == null)
                        return -1;
                    long t1 = b1.getTimestamp();
                    long t2 = b2.getTimestamp();
                    return Long.compare(t2, t1);
                });
                break;
            }
            case HIGHEST_BID:
            {
                stream = stream.sorted((i1, i2) ->
                {
                    AuctionBid b1 = i1.getTopBid();
                    AuctionBid b2 = i2.getTopBid();
                    if (b1 == null)
                        return 1;
                    if (b2 == null)
                        return -1;
                    return Long.compare(b1.getAmount(), b2.getAmount());
                });
                break;
            }
            case MOST_BIDS:
            {
                stream = stream.sorted((i1, i2) -> Integer.compare(i2.getBids().size(), i1.getBids().size()));
                break;
            }
        }
        List<AuctionItem> items = stream.collect(Collectors.toList());
        int ended = 0;
        for (AuctionItem item : items)
        {
            if (item.isExpired())
                ended++;
        }
        if (ended != 0)
        {
            int finalEnded = ended;
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    for (AuctionItem item : items)
                    {
                        if (item.isExpired())
                            item.claim(player);
                    }
                    player.closeInventory();
                }

                @Override
                public int getSlot()
                {
                    return 21;
                }

                @Override
                public ItemStack getItem()
                {
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.DARK_GRAY + "Ended Auctions");
                    lore.add(" ");
                    lore.add(ChatColor.GRAY + "You got " + ChatColor.GREEN + finalEnded + " item" + (finalEnded != 1 ? "s" : "") +
                            ChatColor.GRAY + " to");
                    lore.add(ChatColor.GRAY + "collect sales/reclaim items.");
                    lore.add(" ");
                    lore.add(ChatColor.YELLOW + "Click to claim!");
                    return SUtil.getStack(ChatColor.GREEN + "Claim All", Material.CAULDRON_ITEM, (short) 0, 1, lore);
                }
            });
        }
        set(GUIClickableItem.createGUIOpenerItem(GUIType.AUCTION_HOUSE, player, ChatColor.GREEN + "Go Back", 22, Material.ARROW, ChatColor.GRAY + "To Auction House"));
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                if (e.isRightClick())
                    new ManageAuctionsGUI(sort.previous()).open(player);
                else
                    new ManageAuctionsGUI(sort.next()).open(player);
            }

            @Override
            public int getSlot()
            {
                return 23;
            }

            @Override
            public ItemStack getItem()
            {
                List<String> lore = new ArrayList<>(Collections.singletonList(" "));
                for (Sort s : Sort.values())
                {
                    if (sort == s)
                        lore.add(ChatColor.AQUA + "▶ " + s.getDisplay());
                    else
                        lore.add(ChatColor.GRAY + s.getDisplay());
                }
                lore.add(" ");
                lore.add(ChatColor.AQUA + "Right-Click to go backwards!");
                lore.add(ChatColor.YELLOW + "Click to switch sort!");
                return SUtil.getStack(ChatColor.GREEN + "Sort", Material.HOPPER, (short) 0, 1, lore);
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                if (items.size() >= 7)
                {
                    player.sendMessage(ChatColor.RED + "You cannot create any more auctions!");
                    return;
                }
                new CreateAuctionGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 24;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Create Auction", Material.GOLD_BARDING,
                        (short) 0, 1,
                        ChatColor.GRAY + "Set your own items on",
                        ChatColor.GRAY + "auction for other players",
                        ChatColor.GRAY + "to purchase.",
                        " ",
                        ChatColor.YELLOW + "Click to become rich!");
            }
        });
        for (int i = 0; i < items.size(); i++)
        {
            AuctionItem item = items.get(i);
            int slot = INTERIOR[i];
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    new AuctionViewGUI(item, ManageAuctionsGUI.this).open(player);
                }
                @Override
                public int getSlot()
                {
                    return slot;
                }
                @Override
                public ItemStack getItem()
                {
                    return item.getDisplayItem(true, true);
                }
            });
        }
    }

    private enum Sort
    {
        RECENTLY_UPDATED("Recently Updated"),
        HIGHEST_BID("Highest Bid"),
        MOST_BIDS("Most Bids");

        private final String display;

        Sort(String display)
        {
            this.display = display;
        }

        public String getDisplay()
        {
            return display;
        }

        public Sort previous()
        {
            int prev = ordinal() - 1;
            if (prev < 0)
                return values()[values().length - 1];
            return values()[prev];
        }

        public Sort next()
        {
            int nex = ordinal() + 1;
            if (nex > values().length - 1)
                return values()[0];
            return values()[nex];
        }
    }
}
package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.auction.AuctionBid;
import me.superischroma.spectaculation.auction.AuctionItem;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class AuctionViewGUI extends GUI
{
    private final AuctionItem item;
    private final GUI ret;
    private final long bid;

    public AuctionViewGUI(AuctionItem item, GUI ret, long bid)
    {
        super((item.isBin() ? "BIN " : "") + "Auction View", 54);
        this.item = item;
        this.ret = ret;
        this.bid = bid;
        fill(BLACK_STAINED_GLASS_PANE);
    }

    public AuctionViewGUI(AuctionItem item, GUI ret)
    {
        this(item, ret, item.nextBid());
    }

    public AuctionViewGUI(AuctionItem item)
    {
        this(item, null);
    }

    @Override
    public void onOpen(GUIOpenEvent e)
    {
        Player player = e.getPlayer();
        User user = User.getUser(player.getUniqueId());
        boolean personal = item.getOwner().getUuid().equals(user.getUuid());
        set(13, item.getDisplayItem(false, user.getUuid().equals(item.getOwner().getUuid())));
        if (item.isBin())
        {
            for (GUIItem item : getBINItems(player))
                set(item);
        }
        else
        {
            for (GUIItem item : getAuctionItems(player))
                set(item);
        }
        if (ret != null)
            set(GUIClickableItem.createGUIOpenerItem(ret, player, ChatColor.GREEN + "Go Back", 49, Material.ARROW, (short) 0, ChatColor.GRAY + "To " + ret.getTitle()));
        else
            set(GUIClickableItem.getCloseItem(49));
    }

    private List<GUIItem> getBINItems(Player player)
    {
        User user = User.getUser(player.getUniqueId());
        boolean personal = item.getOwner().getUuid().equals(user.getUuid());
        List<GUIItem> items = new ArrayList<>();
        if (item.isExpired())
        {
            long topBid = item.getTopBidAmount();
            items.add(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    item.claim(player);
                    player.closeInventory();
                }

                @Override
                public int getSlot()
                {
                    return 31;
                }

                @Override
                public ItemStack getItem()
                {
                    List<String> lore = new ArrayList<>();
                    lore.add(" ");
                    if (item.getBids().size() == 0)
                    {
                        if (item.isBin())
                            lore.add(ChatColor.GRAY + "No one has bought your item.");
                        else
                            lore.add(ChatColor.GRAY + "No one has bid on your item.");
                        lore.add(ChatColor.GREEN + "You may pick it back up.");
                        lore.add(" ");
                        lore.add(ChatColor.YELLOW + "Click to pick up item!");
                    }
                    else
                    {
                        lore.add(ChatColor.GRAY + "Item sold to " + ChatColor.GREEN + Bukkit.getOfflinePlayer(item.getTopBidder().getUuid()));
                        lore.add(ChatColor.GRAY + "for " + ChatColor.GOLD + topBid + " coin" + (topBid != 1 ? "s" : "") + ChatColor.GRAY + "!");
                        lore.add(" ");
                        lore.add(ChatColor.YELLOW + "Click to collect coins!");
                    }
                    return SUtil.getStack(ChatColor.GOLD + "Collect Auction", item.getBids().size() != 0 ? Material.GOLD_BLOCK : Material.GOLD_NUGGET,
                            (short) 0, 1, lore);
                }
            });
        }
        else
        {
            items.add(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    if (personal)
                    {
                        player.sendMessage(ChatColor.RED + "This is your own auction!");
                        return;
                    }
                    if (item.isExpired())
                    {
                        player.sendMessage(ChatColor.RED + "The item you are trying to bid on has already expired!");
                        player.closeInventory();
                        return;
                    }
                    User top = item.getTopBidder();
                    if (top != null && top.getUuid().equals(user.getUuid()))
                    {
                        player.sendMessage(ChatColor.GREEN + "You are already top bid!");
                        return;
                    }
                    if (user.getCoins() < bid)
                    {
                        player.sendMessage(ChatColor.RED + "You cannot afford this bid!");
                        return;
                    }
                    new ConfirmBidGUI(item, bid).open(player);
                }

                @Override
                public int getSlot()
                {
                    return personal && item.getBids().size() == 0 ? 29 : 31;
                }

                @Override
                public ItemStack getItem()
                {
                    List<String> lore = new ArrayList<>();
                    lore.add(" ");
                    if (item.isBin())
                        lore.add(ChatColor.GRAY + "Price: " + ChatColor.GOLD + SUtil.commaify(item.getStarter()) + " coin" + (item.getStarter() != 1 ? "s" : ""));
                    else
                    {
                        lore.add(ChatColor.GRAY + "New bid: " + ChatColor.GOLD + SUtil.commaify(bid) + " coin" + (bid != 1 ? "s" : ""));
                        AuctionBid bid = item.getBid(user);
                        if (bid != null)
                            lore.add(ChatColor.GRAY + "Your previous bid: " + ChatColor.YELLOW + SUtil.commaify(bid.getAmount()) + " coin" + (bid.getAmount() != 1 ? "s" : ""));
                    }
                    lore.add(" ");
                    User top = item.getTopBidder();
                    if (personal)
                        lore.add(ChatColor.GREEN + "This is your own auction!");
                    else if (top != null && top.getUuid().equals(user.getUuid()))
                        lore.add(ChatColor.GREEN + "Already top bid!");
                    else if (user.getCoins() < bid)
                        lore.add(ChatColor.RED + "Cannot afford bid!");
                    else
                        lore.add(ChatColor.YELLOW + "Click to " + (item.isBin() ? "buy" : "bid") + "!");
                    Material icon = user.getCoins() < bid || personal ? Material.POTATO_ITEM : Material.GOLD_NUGGET;
                    if (top != null && top.getUuid().equals(user.getUuid()))
                        icon = Material.GOLD_BLOCK;
                    return SUtil.getStack(ChatColor.GOLD + (item.isBin() ? "Buy Item Right Now" : "Submit Bid"), icon, (short) 0, 1, lore);
                }
            });
        }
        if (personal && item.getBids().size() == 0 && !item.isExpired())
        {
            items.add(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    if (item.isExpired())
                    {
                        player.sendMessage(ChatColor.RED + "The auction you are trying to cancel has already expired!");
                        player.closeInventory();
                        return;
                    }
                    if (item.getBids().size() != 0)
                    {
                        player.sendMessage(ChatColor.RED + "Someone has bid on this item and it cannot be removed!");
                        return;
                    }
                    player.closeInventory();
                    player.getInventory().addItem(item.getItem().getStack());
                    item.delete();
                    player.sendMessage(ChatColor.GREEN + "Your auction has been successfully cancelled!");
                }

                @Override
                public int getSlot()
                {
                    return 33;
                }

                @Override
                public ItemStack getItem()
                {
                    return SUtil.getStack(ChatColor.RED + "Cancel Auction", Material.STAINED_CLAY, (short) 14, 1,
                            ChatColor.GRAY + "You may cancel auctions as",
                            ChatColor.GRAY + "long as they have " + ChatColor.RED + "0" + ChatColor.GRAY + " bids!",
                            " ",
                            ChatColor.YELLOW + "Click to cancel auction!");
                }
            });
        }
        return items;
    }

    private List<GUIItem> getAuctionItems(Player player)
    {
        User user = User.getUser(player.getUniqueId());
        boolean personal = item.getOwner().getUuid().equals(user.getUuid());
        List<GUIItem> items = new ArrayList<>();
        if (item.isExpired())
        {
            long topBid = item.getTopBidAmount();
            items.add(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    item.claim(player);
                    player.closeInventory();
                }

                @Override
                public int getSlot()
                {
                    return 29;
                }

                @Override
                public ItemStack getItem()
                {
                    List<String> lore = new ArrayList<>();
                    lore.add(" ");
                    if (item.getBids().size() == 0)
                    {
                        if (item.isBin())
                            lore.add(ChatColor.GRAY + "No one has bought your item.");
                        else
                            lore.add(ChatColor.GRAY + "No one has bid on your item.");
                        lore.add(ChatColor.GREEN + "You may pick it back up.");
                        lore.add(" ");
                        lore.add(ChatColor.YELLOW + "Click to pick up item!");
                    }
                    else
                    {
                        lore.add(ChatColor.GRAY + "Item sold to " + ChatColor.GREEN + Bukkit.getOfflinePlayer(item.getTopBidder().getUuid()));
                        lore.add(ChatColor.GRAY + "for " + ChatColor.GOLD + topBid + " coin" + (topBid != 1 ? "s" : "") + ChatColor.GRAY + "!");
                        lore.add(" ");
                        lore.add(ChatColor.YELLOW + "Click to collect coins!");
                    }
                    return SUtil.getStack(ChatColor.GOLD + "Collect Auction", item.getBids().size() != 0 ? Material.GOLD_BLOCK : Material.GOLD_NUGGET,
                            (short) 0, 1, lore);
                }
            });
        }
        else
        {
            items.add(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    if (personal)
                    {
                        player.sendMessage(ChatColor.RED + "This is your own auction!");
                        return;
                    }
                    if (item.isExpired())
                    {
                        player.sendMessage(ChatColor.RED + "The item you are trying to bid on has already expired!");
                        player.closeInventory();
                        return;
                    }
                    User top = item.getTopBidder();
                    if (top != null && top.getUuid().equals(user.getUuid()))
                    {
                        player.sendMessage(ChatColor.GREEN + "You are already top bid!");
                        return;
                    }
                    if (user.getCoins() < bid)
                    {
                        player.sendMessage(ChatColor.RED + "You cannot afford this bid!");
                        return;
                    }
                    new ConfirmBidGUI(item, bid).open(player);
                }

                @Override
                public int getSlot()
                {
                    return 29;
                }

                @Override
                public ItemStack getItem()
                {
                    List<String> lore = new ArrayList<>();
                    lore.add(" ");
                    if (item.isBin())
                        lore.add(ChatColor.GRAY + "Price: " + ChatColor.GOLD + SUtil.commaify(item.getStarter()) + " coin" + (item.getStarter() != 1 ? "s" : ""));
                    else
                        lore.add(ChatColor.GRAY + "New bid: " + ChatColor.GOLD + SUtil.commaify(bid) + " coin" + (bid != 1 ? "s" : ""));
                    lore.add(" ");
                    if (personal)
                        lore.add(ChatColor.GREEN + "This is your own auction!");
                    else if (user.getCoins() < bid)
                        lore.add(ChatColor.RED + "Cannot afford purchase!");
                    else
                        lore.add(ChatColor.YELLOW + "Click to " + (item.isBin() ? "buy" : "bid") + "!");
                    return SUtil.getStack(ChatColor.GOLD + (item.isBin() ? "Buy Item Right Now" : "Submit Bid"),
                            user.getCoins() < bid || personal ? Material.POTATO_ITEM : Material.GOLD_NUGGET, (short) 0, 1, lore);
                }
            });
        }
        if (personal && item.getBids().size() == 0 && !item.isExpired())
        {
            items.add(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    if (item.isExpired())
                    {
                        player.sendMessage(ChatColor.RED + "The auction you are trying to cancel has already expired!");
                        player.closeInventory();
                        return;
                    }
                    if (item.getBids().size() != 0)
                    {
                        player.sendMessage(ChatColor.RED + "Someone has bid on this item and it cannot be removed!");
                        return;
                    }
                    player.closeInventory();
                    player.getInventory().addItem(item.getItem().getStack());
                    item.delete();
                    player.sendMessage(ChatColor.GREEN + "Your auction has been successfully cancelled!");
                }

                @Override
                public int getSlot()
                {
                    return 31;
                }

                @Override
                public ItemStack getItem()
                {
                    return SUtil.getStack(ChatColor.RED + "Cancel Auction", Material.STAINED_CLAY, (short) 14, 1,
                            ChatColor.GRAY + "You may cancel auctions as",
                            ChatColor.GRAY + "long as they have " + ChatColor.RED + "0" + ChatColor.GRAY + " bids!",
                            " ",
                            ChatColor.YELLOW + "Click to cancel auction!");
                }
            });
        }
        if (!personal && !item.isExpired() && item.nextBid() <= user.getCoins())
        {
            items.add(new GUIQueryItem()
            {
                @Override
                public GUI onQueryFinish(String query)
                {
                    if (item.isExpired())
                    {
                        player.sendMessage(ChatColor.RED + "The auction you are trying to bid on has already expired!");
                        player.closeInventory();
                    }
                    long l;
                    try
                    {
                        l = Long.parseLong(query);
                    }
                    catch (NumberFormatException ex)
                    {
                        player.sendMessage(ChatColor.RED + "Could not read this number!");
                        return null;
                    }
                    if (l < item.nextBid())
                    {
                        player.sendMessage(ChatColor.RED + "That bid is less than the minimum!");
                        return new AuctionViewGUI(item, ret, bid);
                    }
                    if (bid > user.getCoins())
                    {
                        player.sendMessage(ChatColor.RED + "You cannot afford that bid!");
                        return new AuctionViewGUI(item, ret, bid);
                    }
                    return new AuctionViewGUI(item, ret, l);
                }

                @Override
                public void run(InventoryClickEvent e)
                {
                }

                @Override
                public int getSlot()
                {
                    return 31;
                }

                @Override
                public ItemStack getItem()
                {
                    String display = ChatColor.GOLD + SUtil.commaify(bid) + " coin" + (bid != 1 ? "s" : "");
                    return SUtil.getStack(ChatColor.WHITE + "Bid Amount: " + display,
                            Material.GOLD_INGOT, (short) 0, 1,
                            ChatColor.GRAY + "You need to bid at least",
                            display + ChatColor.GRAY + " to hold",
                            ChatColor.GRAY + "the top bid on this",
                            ChatColor.GRAY + "auction.",
                            " ",
                            ChatColor.GRAY + "The " + ChatColor.YELLOW + "top bid" + ChatColor.GRAY + " on auction",
                            ChatColor.GRAY + "ends wins the item.",
                            " ",
                            ChatColor.GRAY + "If you do not win, you can",
                            ChatColor.GRAY + "claim your bid coins back.",
                            " ",
                            ChatColor.YELLOW + "Click to edit amount!");
                }
            });
        }
        List<String> lore = new ArrayList<>();
        if (item.getBids().size() > 0)
        {
            lore.add(ChatColor.GRAY + "Total bids: " + ChatColor.GREEN + item.getBids().size() + " bid" + (item.getBids().size() != 1 ? "s" : ""));
            for (int i = item.getBids().size() - 1; i >= 0; i--)
            {
                AuctionBid bid = item.getBids().get(i);
                lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "------------");
                lore.add(ChatColor.GRAY + "Bid: " + ChatColor.GOLD + bid.getAmount() + " coin" + (bid.getAmount() != 1 ? "s" : ""));
                lore.add(ChatColor.GRAY + "By: " + ChatColor.GREEN + Bukkit.getOfflinePlayer(bid.getBidder()).getName());
                lore.add(ChatColor.AQUA + SUtil.getAuctionSetupFormattedTime(bid.timeSinceBid()).toLowerCase() + " ago");
            }
        }
        else
        {
            lore.add(ChatColor.GRAY + "No bids have been placed on");
            lore.add(ChatColor.GRAY + "this item yet.");
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Be the first to bid on it!");
        }
        items.add(new GUIItem()
        {
            @Override
            public int getSlot()
            {
                return 33;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.WHITE + "Bid History", Material.MAP, (short) 0, 1, lore);
            }
        });
        return items;
    }

    /*
    private List<GUIItem> getFirstItem(Player player)
    {
        User user = User.getUser(player.getUniqueId());
        boolean personal = item.getOwner().getUuid().equals(user.getUuid());
        List<GUIItem> items = new ArrayList<>();
        if (personal && item.isExpired())
        {
            items.add(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    // todo: collect auction
                }

                @Override
                public int getSlot()
                {
                    return item.isBin() ? 31 : 29;
                }

                @Override
                public ItemStack getItem()
                {
                    List<String> lore = new ArrayList<>();
                    lore.add(" ");
                    if (item.getBids().size() == 0)
                    {
                        if (item.isBin())
                            lore.add(ChatColor.GRAY + "No one has bought your item.");
                        else
                            lore.add(ChatColor.GRAY + "No one has bid on your item.");
                        lore.add(ChatColor.GREEN + "You may pick it back up.");
                        lore.add(" ");
                        lore.add(ChatColor.YELLOW + "Click to pick up item!");
                    }
                    else
                    {
                        long topBid = item.getTopBid();
                        lore.add(ChatColor.GRAY + "Item sold to " + ChatColor.GREEN + Bukkit.getOfflinePlayer(item.getTopBidder().getUuid()));
                        lore.add(ChatColor.GRAY + "for " + ChatColor.GOLD + topBid + " coin" + (topBid != 1 ? "s" : "") + ChatColor.GRAY + "!");
                        lore.add(" ");
                        lore.add(ChatColor.YELLOW + "Click to collect coins!");
                    }
                    return SUtil.getStack(ChatColor.GOLD + "Collect Auction", item.getBids().size() != 0 ? Material.GOLD_BLOCK : Material.GOLD_NUGGET,
                            (short) 0, 1, lore);
                }
            });
        }
        else
        {
            items.add(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {

                }

                @Override
                public int getSlot()
                {
                    return item.isBin() ? 31 : 29;
                }
            });
        }
        if (!item.isBin())
        {
            if (personal && item.getBids().size() == 0)
            {
                items.add(new GUIClickableItem()
                {
                    @Override
                    public void run(InventoryClickEvent e)
                    {

                    }

                    @Override
                    public int getSlot()
                    {
                        return 33;
                    }

                    @Override
                    public ItemStack getItem()
                    {
                        return SUtil.getStack(ChatColor.RED + "Cancel Auction", Material.STAINED_CLAY, (short) 14, 1,
                                ChatColor.GRAY + "You may cancel auctions as",
                                ChatColor.GRAY + "long as they have " + ChatColor.RED + "0" + ChatColor.GRAY + " bids!",
                                " ",
                                ChatColor.YELLOW + "Click to cancel auction!");
                    }
                });
            }
            else
            {
                List<String> lore = new ArrayList<>();
                if (item.getBids().size() > 0)
                {
                    lore.add(ChatColor.GRAY + "Total bids: " + ChatColor.GREEN + item.getBids().size() + " bid" + (item.getBids().size() != 1 ? "s" : ""));
                    for (int i = item.getBids().size() - 1; i >= 0; i--)
                    {
                        AuctionBid bid = item.getBids().get(i);
                        lore.add(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "------------");
                        lore.add(ChatColor.GRAY + "Bid: " + ChatColor.GOLD + bid.getAmount() + " coin" + (bid.getAmount() != 1 ? "s" : ""));
                        lore.add(ChatColor.GRAY + "By: " + ChatColor.GREEN + Bukkit.getOfflinePlayer(bid.getBidder()).getName());
                        lore.add(ChatColor.AQUA + SUtil.getAuctionSetupFormattedTime(bid.timeSinceBid()).toLowerCase() + " ago");
                    }
                }
                else
                {
                    lore.add(ChatColor.GRAY + "No bids have been placed on");
                    lore.add(ChatColor.GRAY + "this item yet.");
                    lore.add(" ");
                    lore.add(ChatColor.GRAY + "Be the first to bid on it!");
                }
                items.add(new GUIItem()
                {
                    @Override
                    public int getSlot()
                    {
                        return 33;
                    }

                    @Override
                    public ItemStack getItem()
                    {
                        return SUtil.getStack(ChatColor.WHITE + "Bid History", Material.MAP, (short) 0, 1, lore);
                    }
                });
            }
        }
    }

     */
}

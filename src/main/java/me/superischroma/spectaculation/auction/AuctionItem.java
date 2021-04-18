package me.superischroma.spectaculation.auction;

import lombok.Getter;
import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.config.Config;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.user.AuctionSettings;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuctionItem
{
    private static final Map<UUID, AuctionItem> AUCTION_ITEM_CACHE = new HashMap<>();
    private static final Spectaculation plugin = Spectaculation.getPlugin();
    private static final File AUCTION_ITEM_FOLDER = new File(plugin.getDataFolder(), "./auctions");

    private Config config;
    @Getter
    private UUID uuid;
    @Getter
    private SItem item;
    @Getter
    private long starter;
    @Getter
    private List<AuctionBid> bids;
    @Getter
    private long end;
    private UUID owner;
    @Getter
    private List<UUID> participants;
    @Getter
    private boolean bin;

    private AuctionItem(UUID uuid, SItem item, long starter, long end, UUID owner, List<UUID> participants, boolean bin)
    {
        this.uuid = uuid;
        this.item = item;
        this.starter = starter;
        this.bids = new ArrayList<>();
        this.end = end;
        this.owner = owner;
        this.participants = participants;
        this.bin = bin;
        if (!AUCTION_ITEM_FOLDER.exists()) AUCTION_ITEM_FOLDER.mkdirs();
        String path = uuid.toString() + ".yml";
        File configFile = new File(AUCTION_ITEM_FOLDER, path);
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
        this.config = new Config(AUCTION_ITEM_FOLDER, path);
        AUCTION_ITEM_CACHE.put(uuid, this);
        if (save) save();
        load();
    }

    public void load()
    {
        this.uuid = UUID.fromString(config.getString("uuid"));
        this.item = (SItem) config.get("item");
        this.starter = config.getLong("starter");
        this.bids = (List<AuctionBid>) config.get("bids");
        this.owner = UUID.fromString(config.getString("owner"));
        List<String> strings = config.getStringList("participants");
        for (String string : strings)
            participants.add(UUID.fromString(string));
        this.end = config.getLong("end");
        this.bin = config.getBoolean("bin");
    }

    public void save()
    {
        config.set("uuid", uuid.toString());
        config.set("item", item);
        config.set("starter", starter);
        config.set("bids", bids);
        config.set("owner", owner.toString());
        List<String> strings = new ArrayList<>();
        for (UUID uuid : participants)
            strings.add(uuid.toString());
        config.set("participants", strings);
        config.set("end", end);
        config.set("bin", bin);
        config.save();
    }

    public boolean delete()
    {
        this.config = null;
        AUCTION_ITEM_CACHE.remove(uuid);
        return new File(AUCTION_ITEM_FOLDER, uuid.toString() + ".yml").delete();
    }

    public User getOwner()
    {
        return User.getUser(owner);
    }

    public boolean isExpired()
    {
        return System.currentTimeMillis() >= end;
    }

    public User getTopBidder()
    {
        UUID uuid = null;
        long b = 0;
        for (AuctionBid bid : bids)
        {
            if (bid.getAmount() > b)
            {
                uuid = bid.getBidder();
                b = bid.getAmount();
            }
        }
        return User.getUser(uuid);
    }

    public AuctionBid getTopBid()
    {
        AuctionBid b = null;
        long l = 0;
        for (AuctionBid bid : bids)
        {
            if (bid.getAmount() > l)
            {
                l = bid.getAmount();
                b = bid;
            }
        }
        return b;
    }

    public long getTopBidAmount()
    {
        long b = 0;
        for (AuctionBid bid : bids)
        {
            if (bid.getAmount() > b)
                b = bid.getAmount();
        }
        return b;
    }

    public AuctionBid getRecentBid()
    {
        return bids.size() == 0 ? null : bids.get(bids.size() - 1);
    }

    public long nextBid()
    {
        long top = getTopBidAmount();
        if (top == 0)
            return starter;
        return Math.round(top * 1.15D);
    }

    public AuctionBid getBid(UUID uuid)
    {
        for (int i = bids.size() - 1; i >= 0; i--)
        {
            AuctionBid bid = bids.get(i);
            if (bid.getBidder().equals(uuid))
                return bid;
        }
        return null;
    }

    public AuctionBid getBid(User user)
    {
        return getBid(user.getUuid());
    }

    public void claim(Player player)
    {
        if (!participants.contains(player.getUniqueId()))
            return;
        User user = User.getUser(player.getUniqueId());
        if (player.getUniqueId().equals(owner))
        {
            if (bids.size() == 0)
                player.getInventory().addItem(item.getStack());
            else
                user.addCoins(getTopBidAmount());
            removeParticipant(user.getUuid());
            return;
        }
        AuctionBid top = getTopBid();
        if (top == null)
            return;
        AuctionBid bid = getBid(user);
        if (bid == null)
            return;
        if (top.getBidder().equals(player.getUniqueId()))
            player.getInventory().addItem(item.getStack());
        else
            user.addCoins(bid.getAmount());
        removeParticipant(player.getUniqueId());
    }

    public void removeParticipant(UUID uuid)
    {
        participants.removeIf(uuid::equals);
        if (participants.size() == 0)
            delete();
    }

    public void bid(User user, long amount)
    {
        if (!participants.contains(user.getUuid()))
            participants.add(user.getUuid());
        AuctionBid prev = getBid(user);
        user.subCoins(amount - (prev != null ? prev.getAmount() : 0));
        bids.add(new AuctionBid(user.getUuid(), amount, System.currentTimeMillis()));
        String bidder = Bukkit.getOfflinePlayer(user.getUuid()).getName();
        if (bin)
        {
            messageOwner(ChatColor.GOLD + "[Auction] " + ChatColor.GREEN + bidder + ChatColor.YELLOW + " bought " + item.getFullName() + ChatColor.YELLOW + " for " +
                    ChatColor.GOLD + SUtil.commaify(starter) + " coin" + (starter != 1 ? "s" : ""));
            end();
            Player player = Bukkit.getPlayer(user.getUuid());
            if (player != null)
                claim(player);
            return;
        }
        messageOwner(ChatColor.GOLD + "[Auction] " + ChatColor.GREEN + bidder + ChatColor.YELLOW + " bid " +
                ChatColor.GOLD + SUtil.commaify(starter) + " coin" + (starter != 1 ? "s" : "") + ChatColor.YELLOW + " on " + item.getFullName());
        for (UUID participant : participants)
        {
            if (participant.equals(user.getUuid()))
                continue;
            Player player = Bukkit.getPlayer(participant);
            if (player == null)
                continue;
            AuctionBid bid = getBid(participant);
            if (bid == null)
                continue;
            long diff = amount - bid.getAmount();
            player.sendMessage(ChatColor.GOLD + "[Auction]" + ChatColor.GREEN + bidder + ChatColor.YELLOW + " outbid you by " +
                    ChatColor.GOLD + diff + " coin" + (diff != 1 ? "s" : "") + ChatColor.YELLOW + " for " + item.getFullName());
        }
    }

    public void messageOwner(String message)
    {
        User ou = getOwner();
        Player owner = Bukkit.getPlayer(ou.getUuid());
        if (owner != null)
            owner.sendMessage(message);
    }

    public void end()
    {
        this.end = System.currentTimeMillis() - 1;
    }

    public long getRemaining()
    {
        return Math.max(0L, end - System.currentTimeMillis());
    }

    public ItemStack getDisplayItem(boolean inspect, boolean yourAuction)
    {
        ItemStack stack = item.getStack().clone();
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------");
        lore.add(ChatColor.GRAY + "Seller: " + ChatColor.GREEN + Bukkit.getOfflinePlayer(owner).getName());
        User top = getTopBidder();
        if (isBin())
            lore.add(ChatColor.GRAY + "Buy it now: " + ChatColor.GOLD + SUtil.commaify(starter) + " coins");
        else if (top == null)
            lore.add(ChatColor.GRAY + "Starting bid: " + ChatColor.GOLD + SUtil.commaify(starter) + " coins");
        else
        {
            lore.add(ChatColor.GRAY + "Bids: " + ChatColor.GREEN + bids.size() + " bids");
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Top bid: " + ChatColor.GOLD + SUtil.commaify(getTopBidAmount()) + " coins");
            lore.add(ChatColor.GRAY + "Bidder: " + ChatColor.GREEN + Bukkit.getOfflinePlayer(top.getUuid()).getName());
        }
        if (yourAuction)
        {
            lore.add(" ");
            lore.add(ChatColor.GREEN + "This is your own auction!");
        }
        lore.add(" ");
        lore.add(ChatColor.GRAY + "Ends in: " + ChatColor.YELLOW + SUtil.getAuctionFormattedTime(getRemaining()));
        if (inspect)
        {
            lore.add(" ");
            lore.add(ChatColor.YELLOW + "Click to inspect!");
        }
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public String toString()
    {
        return "AuctionItem{uuid=" + uuid.toString() + ", item=" + item.toString() + ", bids=" + bids.toString() + ", end=" + end + "}";
    }

    public static AuctionItem createAuction(SItem item, long starter, long end, UUID owner, boolean bin)
    {
        return new AuctionItem(UUID.randomUUID(), item, starter, end, owner, new ArrayList<>(Collections.singletonList(owner)), bin);
    }

    public static AuctionItem getAuction(UUID uuid)
    {
        if (AUCTION_ITEM_CACHE.containsKey(uuid))
            return AUCTION_ITEM_CACHE.get(uuid);
        if (!new File(AUCTION_ITEM_FOLDER, uuid.toString() + ".yml").exists())
            return null;
        return new AuctionItem(uuid, null, 0L, 0L, null, new ArrayList<>(), false);
    }

    public static Collection<AuctionItem> getAuctions()
    {
        if (AUCTION_ITEM_FOLDER == null || !AUCTION_ITEM_FOLDER.exists())
            return new ArrayList<>();
        return AUCTION_ITEM_CACHE.values();
    }

    public static List<AuctionItem> getOwnedAuctions(String name)
    {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        if (player == null)
            return null;
        User user = User.getUser(player.getUniqueId());
        if (user == null)
            return null;
        return user.getAuctions();
    }

    public static CompletableFuture<List<AuctionItem>> search(AuctionSettings settings)
    {
        return CompletableFuture.supplyAsync(() ->
        {
            Stream<AuctionItem> items = getAuctions().stream();
            items = items.filter((item) -> !item.isExpired());
            // filter for category
            items = items.filter((item) -> item.getItem().getType().getStatistics().getCategory() == settings.getCategory());
            // filter for query
            if (settings.getQuery() != null)
            {
                items = items.filter((item) ->
                {
                    String query = settings.getQuery().toLowerCase();
                    String name = item.getItem().getType().getDisplayName(item.getItem().getType().getData()).toLowerCase();
                    String lore = item.getItem().getLore().asBukkitLore().toString().toLowerCase();
                    return query.contains(name) || query.contains(lore) || query.contains(item.getItem().getType().name().toLowerCase());
                });
            }
            // sort
            items = items.sorted((i1, i2) ->
            {
                switch (settings.getSort())
                {
                    case HIGHEST_BID:
                        return Long.compare(i1.getTopBidAmount(), i2.getTopBidAmount());
                    case LOWEST_BID:
                    {
                        if (i1.getTopBidAmount() < i2.getTopBidAmount())
                            return 1;
                        else if (i2.getTopBidAmount() > i1.getTopBidAmount())
                            return -1;
                        else
                            return 0;
                    }
                    case MOST_BIDS:
                        return Long.compare(i1.getBids().size(), i2.getBids().size());
                    case ENDING_SOON:
                        return Long.compare(i2.end - System.currentTimeMillis(), i2.end - System.currentTimeMillis());
                }
                return 0;
            });
            if (settings.getTier() != null)
                items = items.filter((item) -> item.getItem().getRarity() == settings.getTier());
            switch (settings.getType())
            {
                case AUCTIONS_ONLY:
                    items = items.filter((item) -> !item.isBin());
                    break;
                case BIN_ONLY:
                    items = items.filter(AuctionItem::isBin);
                    break;
            }
            return items.collect(Collectors.toList());
        });
    }

    public static void loadAuctionsFromDisk()
    {
        if (!AUCTION_ITEM_FOLDER.exists())
            return;
        for (File f : Objects.requireNonNull(AUCTION_ITEM_FOLDER.listFiles()))
        {
            String name = f.getName();
            if (!name.endsWith(".yml"))
                continue;
            name = name.substring(0, name.length() - 4);
            UUID uuid;
            try
            {
                uuid = UUID.fromString(name);
            }
            catch (IllegalArgumentException ex)
            {
                continue;
            }
            getAuction(uuid);
        }
    }
}
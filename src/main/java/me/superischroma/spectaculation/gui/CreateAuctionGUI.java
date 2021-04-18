package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.auction.AuctionEscrow;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateAuctionGUI extends GUI
{
    private CreateAuctionGUI(String title)
    {
        super(title, 54);
        fill(BLACK_STAINED_GLASS_PANE);
    }

    public CreateAuctionGUI()
    {
        this("Create Auction");
    }

    @Override
    public void early(Player player)
    {
        User user = User.getUser(player.getUniqueId());
        if (user.isAuctionCreationBIN())
            this.title = "Create BIN Auction";
    }

    @Override
    public void onOpen(GUIOpenEvent e)
    {
        Player player = e.getPlayer();
        User user = User.getUser(player.getUniqueId());
        boolean bin = user.isAuctionCreationBIN();
        set(GUIClickableItem.createGUIOpenerItem(GUIType.AUCTION_HOUSE, player, ChatColor.GREEN + "Go Back", 49,
                Material.ARROW, ChatColor.GRAY + "To Auction House"));
        AuctionEscrow escrow = user.getAuctionEscrow();
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                if (user.getAuctionEscrow().getItem() == null) return;
                player.getInventory().addItem(user.getAuctionEscrow().getItem().getStack());
                user.getAuctionEscrow().setItem(null);
                new CreateAuctionGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 13;
            }

            @Override
            public ItemStack getItem()
            {
                if (user.getAuctionEscrow().getItem() != null)
                {
                    SItem display = user.getAuctionEscrow().getItem().clone();
                    ItemMeta meta = display.getStack().getItemMeta();
                    meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "AUCTION FOR ITEM:");
                    List<String> lore = new ArrayList<>(Collections.singletonList(" "));
                    lore.add(ChatColor.GRAY + "" + display.getStack().getAmount() + "x " + escrow.getItem().getFullName());
                    lore.add(display.getRarity().getDisplay());
                    lore.add(" ");
                    lore.add(ChatColor.YELLOW + "Click to pickup!");
                    meta.setLore(lore);
                    display.getStack().setItemMeta(meta);
                    return display.getStack();
                }
                return SUtil.getStack(ChatColor.YELLOW + "Click an item in your inventory!", Material.STONE_BUTTON, (short) 0, 1,
                        ChatColor.GRAY + "Selects it for auction");
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                if (escrow.getItem() == null)
                    return;
                if (user.getCoins() < escrow.getCreationFee(bin))
                {
                    player.sendMessage(ChatColor.RED + "You don't have enough coins to afford this!");
                    return;
                }
                new AuctionConfirmGUI().open(player);
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
                if (escrow.getItem() == null)
                {
                    lore.add(ChatColor.GRAY + "No item selected!");
                    lore.add(" ");
                    lore.add(ChatColor.GRAY + "Click an item in your");
                    lore.add(ChatColor.GRAY + "inventory to select it for");
                    lore.add(ChatColor.GRAY + "this auction.");
                }
                else
                {
                    lore.add(ChatColor.GRAY + "This item will be added to");
                    lore.add(ChatColor.GRAY + "the auction house for other");
                    lore.add(ChatColor.GRAY + "players to purchase.");
                    lore.add(" ");
                    lore.add(ChatColor.GRAY + "Item: " + escrow.getItem().getStack().getAmount() + "x " + escrow.getItem().getFullName());
                    lore.add(ChatColor.GRAY + "Auction duration: " + ChatColor.YELLOW + SUtil.getAuctionSetupFormattedTime(escrow.getDuration()));
                    lore.add(ChatColor.GRAY + "Starting bid: " + ChatColor.GOLD + SUtil.commaify(escrow.getStarter()) + " coins");
                    lore.add(" ");
                    lore.add(ChatColor.GRAY + "Creation fee: " + ChatColor.GOLD + SUtil.commaify(escrow.getCreationFee(user.isAuctionCreationBIN())) + " coins");
                    lore.add(" ");
                    lore.add(ChatColor.YELLOW + "Click to submit!");
                }
                return SUtil.getStack((escrow.getItem() != null ? ChatColor.GREEN : ChatColor.RED) + "Create Auction", Material.STAINED_CLAY,
                        (escrow.getItem() != null ? (short) 13 : (short) 14), 1, lore);
            }
        });
        set(new GUIQueryItem()
        {
            @Override
            public GUI onQueryFinish(String query)
            {
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
                user.getAuctionEscrow().setStarter(l);
                return new CreateAuctionGUI();
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
                List<String> lore = new ArrayList<>();
                if (bin)
                {
                    lore.add(ChatColor.GRAY + "The price at which you want");
                    lore.add(ChatColor.GRAY + "to sell this item.");
                }
                else
                {
                    lore.add(ChatColor.GRAY + "The minimum price a player");
                    lore.add(ChatColor.GRAY + "can offer to obtain your");
                    lore.add(ChatColor.GRAY + "item.");
                    lore.add(" ");
                    lore.add(ChatColor.GRAY + "Once a player bids for your");
                    lore.add(ChatColor.GRAY + "item, other players will");
                    lore.add(ChatColor.GRAY + "have until the auction ends");
                    lore.add(ChatColor.GRAY + "to make a higher bid.");
                }
                lore.add(" ");
                lore.add(ChatColor.GRAY + "Extra fee: " + ChatColor.GOLD + "+" + SUtil.commaify(escrow.getCreationFee(user.isAuctionCreationBIN())) + " coins " +
                        ChatColor.YELLOW + "(" + (bin ? 1 : 5) + "%)");
                lore.add(" ");
                lore.add(ChatColor.YELLOW + "Click to edit!");
                return SUtil.getStack(ChatColor.WHITE + (bin ? "Item price: " : "Starting bid: ") + ChatColor.GOLD + SUtil.commaify(escrow.getStarter()) + " coins",
                        bin ? Material.GOLD_INGOT : Material.POWERED_RAIL, (short) 0, 1,
                        lore);
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                new AuctionDurationGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 33;
            }

            @Override
            public ItemStack getItem()
            {
                List<String> lore = new ArrayList<>();
                if (bin)
                {
                    lore.add(ChatColor.GRAY + "How long the item will be");
                    lore.add(ChatColor.GRAY + "up for sale.");
                }
                else
                {
                    lore.add(ChatColor.GRAY + "How long players will be");
                    lore.add(ChatColor.GRAY + "able to place bids for.");
                    lore.add(" ");
                    lore.add(ChatColor.GRAY + "Note: Bids automatically");
                    lore.add(ChatColor.GRAY + "increase the duration of");
                    lore.add(ChatColor.GRAY + "auctions.");
                }
                lore.add(" ");
                lore.add(ChatColor.YELLOW + "Click to edit!");
                return SUtil.getStack(ChatColor.WHITE + "Duration: " + ChatColor.YELLOW + SUtil.getAuctionSetupFormattedTime(escrow.getDuration()), Material.WATCH, (short) 0, 1, lore);
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                user.setAuctionCreationBIN(!user.isAuctionCreationBIN());
                new CreateAuctionGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 48;
            }

            @Override
            public ItemStack getItem()
            {
                if (bin)
                {
                    return SUtil.getStack(ChatColor.GREEN + "Click to Auction", Material.POWERED_RAIL, (short) 0, 1,
                            ChatColor.GRAY + "With traditional auctions,",
                            ChatColor.GRAY + "multiple buyers compete for the",
                            ChatColor.GRAY + "item by bidding turn by turn.",
                            " ",
                            ChatColor.YELLOW + "Click to switch!");
                }
                return SUtil.getStack(ChatColor.GREEN + "Switch to BIN", Material.GOLD_INGOT, (short) 0, 1,
                        ChatColor.GRAY + "BIN Auctions are simple.",
                        " ",
                        ChatColor.GRAY + "Set a price, then one player may",
                        ChatColor.GRAY + "buy the item at that price.",
                        " ",
                        ChatColor.DARK_GRAY + "(BIN means Buy It Now)",
                        " ",
                        ChatColor.YELLOW + "Click to switch!");
            }
        });
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
        if (user.getAuctionEscrow().getItem() != null) return;
        user.getAuctionEscrow().setItem(item);
        player.getInventory().setItem(e.getSlot(), new ItemStack(Material.AIR));
        new CreateAuctionGUI().open(player);
    }
}
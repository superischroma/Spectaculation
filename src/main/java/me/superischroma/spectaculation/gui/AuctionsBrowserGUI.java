package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.auction.AuctionItem;
import me.superischroma.spectaculation.item.ItemCategory;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.user.AuctionSettings;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.PaginationList;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AuctionsBrowserGUI extends GUI
{
    private static final int[] INTERIOR = new int[]{
            11, 12, 13, 14, 15, 16,
            20, 21, 22, 23, 24, 25,
            29, 30, 31, 32, 33, 34,
            38, 39, 40, 41, 42, 43
    };

    private int page;

    public AuctionsBrowserGUI(int page)
    {
        super("Auctions Browser", 54);
        this.page = page;
    }

    public AuctionsBrowserGUI()
    {
        this(1);
    }

    @Override
    public void onOpen(GUIOpenEvent e)
    {
        Player player = e.getPlayer();
        User user = User.getUser(player.getUniqueId());
        AuctionSettings settings = user.getAuctionSettings();
        ItemCategory category = settings.getCategory();
        PaginationList<AuctionItem> result;
        try
        {
            result = new PaginationList<>(AuctionItem.search(settings).get(), 24);
        }
        catch (InterruptedException | ExecutionException ex)
        {
            player.closeInventory();
            player.sendMessage(ChatColor.RED + "Something went wrong while talking to the Auction House service!");
            return;
        }
        String browsing = ChatColor.GREEN + "Currently browsing!";
        String view = ChatColor.YELLOW + "Click to view items!";
        List<AuctionItem> items = result.getPage(page);
        if (items == null)
            items = new ArrayList<>();
        if (items.size() == 0) page = 0;
        int finalPage = page;
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                settings.setCategory(ItemCategory.WEAPONS);
                new AuctionsBrowserGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 0;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GOLD + "Weapons", Material.GOLD_SWORD, (short) 0, 1,
                        ChatColor.DARK_GRAY + "Category",
                        " ",
                        ChatColor.GRAY + "Examples:",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Swords",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Bows",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Axes",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Magic Weapons",
                        " ",
                        (category == ItemCategory.WEAPONS ? browsing : view));
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                settings.setCategory(ItemCategory.ARMOR);
                new AuctionsBrowserGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 9;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.AQUA + "Armor", Material.DIAMOND_CHESTPLATE, (short) 0, 1,
                        ChatColor.DARK_GRAY + "Category",
                        " ",
                        ChatColor.GRAY + "Examples:",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Hats",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Chestplates",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Leggings",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Boots",
                        " ",
                        (category == ItemCategory.ARMOR ? browsing : view));
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                settings.setCategory(ItemCategory.ACCESSORIES);
                new AuctionsBrowserGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 18;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getSkullURLStack(ChatColor.DARK_GREEN + "Accessories",
                        "f36b821c1afdd5a5d14e3b3bd0a32263c8df5df5db6e1e88bf65e97b27a8530", 1,
                        ChatColor.DARK_GRAY + "Category",
                        " ",
                        ChatColor.GRAY + "Examples:",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Talismans",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Rings",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Orbs",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Artifacts",
                        " ",
                        (category == ItemCategory.ACCESSORIES ? browsing : view));
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                settings.setCategory(ItemCategory.CONSUMABLES);
                new AuctionsBrowserGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 27;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.RED + "Consumables", Material.APPLE, (short) 0, 1,
                        ChatColor.DARK_GRAY + "Category",
                        " ",
                        ChatColor.GRAY + "Examples:",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Potions",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Food",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Books",
                        " ",
                        (category == ItemCategory.CONSUMABLES ? browsing : view));
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                settings.setCategory(ItemCategory.BLOCKS);
                new AuctionsBrowserGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 36;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.YELLOW + "Blocks", Material.COBBLESTONE, (short) 0, 1,
                        ChatColor.DARK_GRAY + "Category",
                        " ",
                        ChatColor.GRAY + "Examples:",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Dirt",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Stone",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Any blocks really",
                        " ",
                        (category == ItemCategory.BLOCKS ? browsing : view));
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                settings.setCategory(ItemCategory.TOOLS_MISC);
                new AuctionsBrowserGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 45;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.LIGHT_PURPLE + "Tools & Misc", Material.STICK, (short) 0, 1,
                        ChatColor.DARK_GRAY + "Category",
                        " ",
                        ChatColor.GRAY + "Examples:",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Tools",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Specials",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Magic",
                        ChatColor.DARK_GRAY + "◼ " + ChatColor.GRAY + "Staff items",
                        " ",
                        (category == ItemCategory.TOOLS_MISC ? browsing : view));
            }
        });
        border(category.getStainedGlassPane(), 1, 53);
        if (page > 1)
        {
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    if (e.isRightClick())
                        new AuctionsBrowserGUI(0).open(player);
                    else
                        new AuctionsBrowserGUI(finalPage - 1).open(player);
                }
                @Override
                public int getSlot()
                {
                    return 46;
                }
                @Override
                public ItemStack getItem()
                {
                    return SUtil.getStack(ChatColor.GREEN + "Previous Page", Material.ARROW, (short) 0, 1,
                            ChatColor.GRAY + "(" + finalPage + "/" + result.getPageCount() + ")",
                            " ",
                            ChatColor.AQUA + "Right-Click to skip!",
                            ChatColor.YELLOW + "Click to turn page!");
                }
            });
        }
        if (page != result.getPageCount())
        {
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    if (e.isRightClick())
                        new AuctionsBrowserGUI(result.getPageCount()).open(player);
                    else
                        new AuctionsBrowserGUI(finalPage + 1).open(player);
                }
                @Override
                public int getSlot()
                {
                    return 53;
                }
                @Override
                public ItemStack getItem()
                {
                    return SUtil.getStack(ChatColor.GREEN + "Next Page", Material.ARROW, (short) 0, 1,
                            ChatColor.GRAY + "(" + finalPage + "/" + result.getPageCount() + ")",
                            " ",
                            ChatColor.AQUA + "Right-Click to skip!",
                            ChatColor.YELLOW + "Click to turn page!");
                }
            });
        }
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                settings.setQuery(null);
                settings.setSort(AuctionSettings.Sort.HIGHEST_BID);
                settings.setTier(null);
                new AuctionsBrowserGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 47;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Reset Settings", Material.ANVIL, (short) 0, 1,
                        ChatColor.GRAY + "Clears your auction",
                        ChatColor.GRAY + "browsing settings except",
                        ChatColor.GRAY + "BIN Filter.",
                        " ",
                        ChatColor.YELLOW + "Click to clear!");
            }
        });
        set(new GUIQueryItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
            }

            @Override
            public GUI onQueryFinish(String query)
            {
                settings.setQuery(query);
                return new AuctionsBrowserGUI();
            }

            @Override
            public int getSlot()
            {
                return 48;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Search", Material.SIGN, (short) 0, 1,
                        ChatColor.GRAY + "Find items by name, type,",
                        ChatColor.GRAY + "lore or enchants.",
                        " ",
                        ChatColor.YELLOW + "Click to search!");
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                if (e.isRightClick())
                    settings.setSort(settings.getSort().previous());
                else
                    settings.setSort(settings.getSort().next());
                new AuctionsBrowserGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 50;
            }

            @Override
            public ItemStack getItem()
            {
                List<String> lore = new ArrayList<>(Collections.singletonList(" "));
                for (AuctionSettings.Sort s : AuctionSettings.Sort.values())
                {
                    if (settings.getSort() == s)
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
                Rarity tier = settings.getTier();
                if (e.isRightClick())
                {
                    if (tier == null)
                        settings.setTier(Rarity.values()[Rarity.values().length - 1]);
                    else if (tier.ordinal() == 0)
                        settings.setTier(null);
                    else
                        settings.setTier(tier.downgrade());
                }
                else
                {
                    if (tier == null)
                        settings.setTier(Rarity.values()[0]);
                    else if (tier.ordinal() == Rarity.values().length - 1)
                        settings.setTier(null);
                    else
                        settings.setTier(tier.upgrade());
                }
                new AuctionsBrowserGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 51;
            }

            @Override
            public ItemStack getItem()
            {
                List<String> lore = new ArrayList<>(Collections.singletonList(" "));
                if (settings.getTier() == null)
                    lore.add(ChatColor.DARK_GRAY + "▶ No filter");
                else
                    lore.add(ChatColor.GRAY + "No filter");
                for (Rarity rarity : Rarity.values())
                {
                    String normal = SUtil.toNormalCase(rarity.name());
                    if (settings.getTier() == rarity)
                        lore.add(rarity.getColor() + "▶ " + normal);
                    else
                        lore.add(ChatColor.GRAY + normal);
                }
                lore.add(" ");
                lore.add(ChatColor.AQUA + "Right-Click to go backwards!");
                lore.add(ChatColor.YELLOW + "Click to switch rarity!");
                return SUtil.getStack(ChatColor.GREEN + "Item Tier", Material.EYE_OF_ENDER, (short) 0, 1, lore);
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                if (e.isRightClick())
                    settings.setType(settings.getType().previous());
                else
                    settings.setType(settings.getType().next());
                new AuctionsBrowserGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 52;
            }

            @Override
            public ItemStack getItem()
            {
                List<String> lore = new ArrayList<>(Collections.singletonList(" "));
                for (AuctionSettings.Type t : AuctionSettings.Type.values())
                {
                    if (settings.getType() == t)
                        lore.add(ChatColor.DARK_AQUA + "▶ " + t.getDisplay());
                    else
                        lore.add(ChatColor.GRAY + t.getDisplay());
                }
                lore.add(" ");
                lore.add(ChatColor.AQUA + "Right-Click to go backwards!");
                lore.add(ChatColor.YELLOW + "Click to switch filter!");
                return SUtil.getStack(ChatColor.GREEN + "BIN Filter", Material.GOLD_BLOCK, (short) 0, 1, lore);
            }
        });
        set(GUIClickableItem.createGUIOpenerItem(GUIType.AUCTION_HOUSE, player, ChatColor.GREEN + "Go Back", 49,
                Material.ARROW, ChatColor.GRAY + "To Auction House"));
        for (int i = 0; i < items.size(); i++)
        {
            int slot = INTERIOR[i];
            AuctionItem item = items.get(i);
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    new AuctionViewGUI(item, AuctionsBrowserGUI.this).open(player);
                }

                @Override
                public int getSlot()
                {
                    return slot;
                }

                @Override
                public ItemStack getItem()
                {
                    return item.getDisplayItem(true, user.getUuid().equals(item.getOwner().getUuid()));
                }
            });
        }
    }
}
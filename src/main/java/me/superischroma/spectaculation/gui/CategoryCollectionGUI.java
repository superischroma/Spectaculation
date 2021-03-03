package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.collection.ItemCollection;
import me.superischroma.spectaculation.collection.ItemCollectionCategory;
import me.superischroma.spectaculation.collection.ItemCollectionReward;
import me.superischroma.spectaculation.collection.ItemCollectionRewards;
import me.superischroma.spectaculation.user.*;
import me.superischroma.spectaculation.util.PaginationList;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryCollectionGUI extends GUI
{
    private static final int[] INTERIOR = new int[]{
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    private final ItemCollectionCategory category;
    private int page;

    public CategoryCollectionGUI(ItemCollectionCategory category, int page)
    {
        super(category.getName() + " Collection", 54);
        this.category = category;
        this.page = page;
    }

    public CategoryCollectionGUI(ItemCollectionCategory category)
    {
        this(category, 1);
    }

    @Override
    public void onOpen(GUIOpenEvent e)
    {
        Player player = e.getPlayer();
        User user = User.getUser(player.getUniqueId());
        border(BLACK_STAINED_GLASS_PANE);
        PaginationList<ItemCollection> paged = new PaginationList<>(28);
        paged.addAll(ItemCollection.getCategoryCollections(category));
        if (paged.size() == 0) page = 0;
        int finalPage = page;
        if (page > 1)
        {
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    new CategoryCollectionGUI(category, finalPage - 1).open((Player) e.getWhoClicked());
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
                    new CategoryCollectionGUI(category, finalPage + 1).open((Player) e.getWhoClicked());
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
        set(GUIClickableItem.createGUIOpenerItem(GUIType.COLLECTION_MENU, player, ChatColor.GREEN + "Go Back", 48,
                Material.ARROW, ChatColor.GRAY + "To Collection"));
        set(GUIClickableItem.getCloseItem(49));
        List<ItemCollection> p = paged.getPage(page);
        if (p == null) return;
        for (int i = 0; i < p.size(); i++)
        {
            int slot = INTERIOR[i];
            ItemCollection collection = p.get(i);
            int amount = user.getCollection(collection);
            if (amount != 0)
            {
                List<String> lore = new ArrayList<>(
                        Arrays.asList(ChatColor.GRAY + "View all your " + collection.getName() + " Collection",
                                ChatColor.GRAY + "progress and rewards!", " "));
                int tier = collection.getTier(amount);
                int next = tier + 1;
                int nextReq = collection.getRequirementForTier(next);
                if (nextReq != -1)
                {
                    String numeral = SUtil.toRomanNumeral(next);
                    lore.add(SUtil.createProgressText("Progress to " + collection.getName() + " " + numeral,
                            amount, nextReq));
                    lore.add(SUtil.createLineProgressBar(20, ChatColor.DARK_GREEN, amount, nextReq));
                    lore.add(" ");
                    ItemCollectionRewards rewards = collection.getRewardsFor(next);
                    if (rewards != null && rewards.size() != 0)
                    {
                        lore.add(ChatColor.GRAY + collection.getName() + " " + numeral + " Reward" + (rewards.size() != 1 ? "s" : ""));
                        for (ItemCollectionReward reward : rewards)
                            lore.add(ChatColor.GRAY + " " + reward.toRewardString());
                        lore.add(" ");
                    }
                }
                lore.add(ChatColor.YELLOW + "Click to view!");
                set(new GUIClickableItem()
                {
                    @Override
                    public void run(InventoryClickEvent e)
                    {
                        new ItemCollectionGUI(collection).open(player);
                    }
                    @Override
                    public int getSlot()
                    {
                        return slot;
                    }
                    @Override
                    public ItemStack getItem()
                    {
                        return SUtil.getStack(ChatColor.YELLOW + collection.getName() + (tier != 0 ? " " + SUtil.toRomanNumeral(tier) : ""),
                                collection.getMaterial().getCraftMaterial(), collection.getData(), 1,
                                lore);
                    }
                });
                continue;
            }
            set(slot, SUtil.getStack(ChatColor.RED + collection.getName(), Material.INK_SACK, (short) 8, 1,
                    ChatColor.GRAY + "Find this item to add it to your",
                    ChatColor.GRAY + "collection and unlock collection",
                    ChatColor.GRAY + "rewards!"));
        }
    }
}
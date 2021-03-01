package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.util.PaginationList;
import me.superischroma.spectaculation.util.SUtil;
import net.minecraft.server.v1_8_R3.Item;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemBrowserGUI extends GUI
{
    private static final int[] INTERIOR = new int[]{
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    public ItemBrowserGUI(String query, int page)
    {
        super("Item Browser", 54);
        border(BLACK_STAINED_GLASS_PANE);
        PaginationList<SMaterial> pagedMaterials = new PaginationList<>(28);
        pagedMaterials.addAll(SMaterial.values());
        pagedMaterials.removeIf(mat -> Item.getById(mat.getCraftMaterial().getId()) == null);
        if (!query.equals(""))
        {
            pagedMaterials.removeIf(material ->
                    !material.name().toLowerCase().contains(query.replaceAll(" ", "_").toLowerCase()));
        }
        if (pagedMaterials.size() == 0) page = 0;
        this.title = "Item Browser | Page " + page + "/" + pagedMaterials.getPageCount();
        int finalPage = page;
        if (page > 1)
        {
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    new ItemBrowserGUI(finalPage - 1).open((Player) e.getWhoClicked());
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
        if (page != pagedMaterials.getPageCount())
        {
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    new ItemBrowserGUI(finalPage + 1).open((Player) e.getWhoClicked());
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
        set(new GUIQueryItem()
        {
            @Override
            public GUI onQueryFinish(String query)
            {
                return new ItemBrowserGUI(query);
            }
            @Override
            public void run(InventoryClickEvent e) {}
            @Override
            public int getSlot()
            {
                return 48;
            }
            @Override
            public ItemStack getItem()
            {
                return SUtil.createNamedItemStack(Material.SIGN, ChatColor.GREEN + "Search");
            }
        });
        set(GUIClickableItem.getCloseItem(50));
        List<SMaterial> p = pagedMaterials.getPage(page);
        if (p == null) return;
        for (int i = 0; i < p.size(); i++)
        {
            int slot = INTERIOR[i];
            SItem sItem = SItem.of(p.get(i), (byte) p.get(i).getData());
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    Player player = (Player) e.getWhoClicked();
                    player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1f, 1f);
                    player.sendMessage(sItem.getType().getDisplayName(sItem.getVariant()) + ChatColor.GRAY + " has been added to your inventory.");
                    player.getInventory().addItem(SItem.of(sItem.getType(), sItem.getVariant()).getStack());
                }
                @Override
                public int getSlot()
                {
                    return slot;
                }
                @Override
                public ItemStack getItem()
                {
                    return sItem.getStack();
                }
            });
        }
    }

    public ItemBrowserGUI(String query)
    {
        this(query, 1);
    }

    public ItemBrowserGUI(int page)
    {
        this("", page);
    }

    public ItemBrowserGUI()
    {
        this(1);
    }
}
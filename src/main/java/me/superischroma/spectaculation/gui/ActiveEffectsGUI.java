package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.potion.ActivePotionEffect;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.PaginationList;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActiveEffectsGUI extends GUI
{
    private static final int[] INTERIOR = new int[]{
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    private int page;

    public ActiveEffectsGUI(int page)
    {
        super("Active Effects", 54);
        this.page = page;
    }

    public ActiveEffectsGUI()
    {
        this(1);
    }

    @Override
    public void onOpen(GUIOpenEvent e)
    {
        Player player = e.getPlayer();
        User user = User.getUser(player.getUniqueId());
        border(BLACK_STAINED_GLASS_PANE);
        PaginationList<ActivePotionEffect> paged = new PaginationList<>(28);
        paged.addAll(user.getEffects());
        if (paged.size() == 0) page = 0;
        int finalPage = page;
        this.title = "(" + page + "/" + paged.getPageCount() + ") Active Effects";
        if (page > 1)
        {
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    new ActiveEffectsGUI(finalPage - 1).open((Player) e.getWhoClicked());
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
                    new ActiveEffectsGUI(finalPage + 1).open((Player) e.getWhoClicked());
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
        set(4, SUtil.getStack(ChatColor.GREEN + "Active Effects", Material.POTION, (short) 0, 1,
            ChatColor.GRAY + "View and manage all of your",
            ChatColor.GRAY + "active potion effects.",
            " ",
            ChatColor.GRAY + "Drink Potions or splash them",
            ChatColor.GRAY + "on the ground to buff yourself!",
            " ",
            ChatColor.GRAY + "Currently Active: " + ChatColor.YELLOW + user.getEffects().size()));
        set(GUIClickableItem.createGUIOpenerItem(GUIType.SKYBLOCK_MENU, player, ChatColor.GREEN + "Go Back", 48,
                Material.ARROW, ChatColor.GRAY + "To SkyBlock Menu"));
        set(GUIClickableItem.getCloseItem(49));
        List<ActivePotionEffect> p = paged.getPage(page);
        if (p == null) return;
        for (int i = 0; i < p.size(); i++)
        {
            int slot = INTERIOR[i];
            ActivePotionEffect effect = p.get(i);
            List<String> lore = new ArrayList<>(Arrays.asList(" "));
            for (String line : SUtil.splitByWordAndLength(effect.getEffect().getDescription(), 20, "\\s"))
                lore.add(ChatColor.GRAY + line);
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Remaining: " + ChatColor.WHITE + effect.getRemainingDisplay());
            lore.add(" ");
            lore.add(SUtil.findPotionRarity(effect.getEffect().getLevel()).getDisplay());
            set(slot, SUtil.getStack(effect.getEffect().getType().getName() + " " + SUtil.toRomanNumeral(effect.getEffect().getLevel()),
                    Material.POTION, effect.getEffect().getType().getColor().getData(), 1, lore));
        }
        new BukkitRunnable()
        {
            public void run()
            {
                if (ActiveEffectsGUI.this != GUI.GUI_MAP.get(player.getUniqueId()))
                    return;
                new ActiveEffectsGUI(page).open(player);
            }
        }.runTaskLater(Spectaculation.getPlugin(), 5);
    }
}
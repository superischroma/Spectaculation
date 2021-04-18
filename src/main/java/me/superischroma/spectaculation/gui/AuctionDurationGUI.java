package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicBoolean;

public class AuctionDurationGUI extends GUI
{
    public AuctionDurationGUI()
    {
        super("Auction Duration", 36);
        fill(BLACK_STAINED_GLASS_PANE);
    }

    @Override
    public void onOpen(GUIOpenEvent e)
    {
        User user = User.getUser(e.getPlayer().getUniqueId());
        set(createTime((short) 14, 1, 10, user));
        set(createTime((short) 6, 6, 11, user));
        set(createTime((short) 1, 12, 12, user));
        set(createTime((short) 4, 24, 13, user));
        set(createTime((short) 0, 48, 14, user));
        AtomicBoolean right = new AtomicBoolean();
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
                    e.getPlayer().sendMessage(ChatColor.RED + "Could not read this number!");
                    return null;
                }
                user.getAuctionEscrow().setDuration(l * (right.get() ? 60000 : 3600000));
                return new CreateAuctionGUI();
            }

            @Override
            public void run(InventoryClickEvent e)
            {
                if (e.isRightClick())
                    right.set(true);
            }

            @Override
            public int getSlot()
            {
                return 16;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Custom Duration", Material.WATCH, (short) 0, 1,
                        ChatColor.GRAY + "Specify how long you want",
                        ChatColor.GRAY + "the auction to last.",
                        " ",
                        ChatColor.AQUA + "Right-click for minutes!",
                        ChatColor.YELLOW + "Click to set hours!");
            }
        });
        set(GUIClickableItem.createGUIOpenerItem(GUIType.CREATE_AUCTION, e.getPlayer(), ChatColor.GREEN + "Go Back", 31, Material.ARROW, (short) 0,
                ChatColor.GRAY + "To Create " + (user.isAuctionCreationBIN() ? "BIN " : "") + "Auction"));
    }

    private static GUIClickableItem createTime(short color, int hours, int slot, User user)
    {
        long millis = hours * 3600000L;
        return new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                user.getAuctionEscrow().setDuration(millis);
                new AuctionDurationGUI().open((Player) e.getWhoClicked());
            }

            @Override
            public int getSlot()
            {
                return slot;
            }

            @Override
            public ItemStack getItem()
            {
                ItemStack stack = SUtil.getStack(ChatColor.GREEN + SUtil.getAuctionSetupFormattedTime(millis), Material.STAINED_CLAY, color, 1,
                        ChatColor.YELLOW + "Click to pick!");
                if (user.getAuctionEscrow().getDuration() == millis)
                    SUtil.enchant(stack);
                return stack;
            }
        };
    }
}
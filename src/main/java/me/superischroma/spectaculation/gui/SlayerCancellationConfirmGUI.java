package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SlayerCancellationConfirmGUI extends GUI
{
    public SlayerCancellationConfirmGUI(User user)
    {
        super("Confirm", 27);
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                user.setSlayerQuest(null);
                e.getWhoClicked().sendMessage(ChatColor.GREEN + "Your Slayer Quest has been cancelled!");
                GUIType.SLAYER.getGUI().open((Player) e.getWhoClicked());
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Confirm", Material.STAINED_CLAY, (short) 13, 1,
                        ChatColor.RED + "Clears " + ChatColor.GRAY + "progress towards",
                        ChatColor.GRAY + "the current Slayer Quest to",
                        ChatColor.GRAY + "let you pick a different",
                        ChatColor.GRAY + "one.",
                        "",
                        ChatColor.RED + "" + ChatColor.BOLD + "CANCELLING THE QUEST!",
                        ChatColor.YELLOW + "Click to cancel the quest!");
            }

            @Override
            public int getSlot()
            {
                return 11;
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                e.getWhoClicked().closeInventory();
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.RED + "Cancel", Material.STAINED_CLAY, (short) 14, 1);
            }

            @Override
            public int getSlot()
            {
                return 15;
            }
        });
    }
}
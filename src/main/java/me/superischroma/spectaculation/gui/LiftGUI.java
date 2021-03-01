package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class LiftGUI extends GUI
{
    public LiftGUI()
    {
        super("Lift", 54);
    }

    @Override
    public void onOpen(GUIOpenEvent e)
    {
        fill(BLACK_STAINED_GLASS_PANE);
        Player player = e.getPlayer();
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                player.closeInventory();
                player.teleport(new Location(player.getWorld(), 44.5, 150.0, -559.5, 90.0f, 0.0f));
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
            }

            @Override
            public int getSlot()
            {
                return 10;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Gunpowder Mines", Material.GOLD_INGOT, (short) 0, 1,
                        ChatColor.GRAY + "Teleports you to the " + ChatColor.AQUA + "Gunpowder",
                        ChatColor.AQUA + "Mines" + ChatColor.GRAY + "!",
                        " ",
                        ChatColor.YELLOW + "Click to travel!");
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                player.closeInventory();
                player.teleport(new Location(player.getWorld(), 44.5, 121.0, -559.5, 90.0f, 0.0f));
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
            }

            @Override
            public int getSlot()
            {
                return 12;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Lapis Quarry", Material.INK_SACK, (short) 4, 1,
                        ChatColor.GRAY + "Teleports you to the " + ChatColor.AQUA + "Lapis",
                        ChatColor.AQUA + "Quarry" + ChatColor.GRAY + "!",
                        " ",
                        ChatColor.YELLOW + "Click to travel!");
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                player.closeInventory();
                player.teleport(new Location(player.getWorld(), 44.5, 101.0, -559.5, 90.0f, 0.0f));
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
            }

            @Override
            public int getSlot()
            {
                return 14;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Pigmen's Den", Material.REDSTONE, (short) 0, 1,
                        ChatColor.GRAY + "Teleports you to the " + ChatColor.AQUA + "Pigmen's",
                        ChatColor.AQUA + "Den" + ChatColor.GRAY + "!",
                        " ",
                        ChatColor.YELLOW + "Click to travel!");
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                player.closeInventory();
                player.teleport(new Location(player.getWorld(), 44.5, 66.0, -559.5, 90.0f, 0.0f));
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
            }

            @Override
            public int getSlot()
            {
                return 16;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Slimehill", Material.EMERALD, (short) 0, 1,
                        ChatColor.GRAY + "Teleports you to the ",
                        ChatColor.AQUA + "Slimehill" + ChatColor.GRAY + "!",
                        " ",
                        ChatColor.YELLOW + "Click to travel!");
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                player.closeInventory();
                player.teleport(new Location(player.getWorld(), 44.5, 38.0, -559.5, 90.0f, 0.0f));
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
            }

            @Override
            public int getSlot()
            {
                return 28;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Diamond Reserve", Material.DIAMOND, (short) 0, 1,
                        ChatColor.GRAY + "Teleports you to the " + ChatColor.AQUA + "Diamond",
                        ChatColor.AQUA + "Reserve" + ChatColor.GRAY + "!",
                        " ",
                        ChatColor.YELLOW + "Click to travel!");
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                player.closeInventory();
                player.teleport(new Location(player.getWorld(), 44.5, 13.0, -559.5, 90.0f, 0.0f));
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
            }

            @Override
            public int getSlot()
            {
                return 30;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Obsidian Sanctuary", Material.OBSIDIAN, (short) 0, 1,
                        ChatColor.GRAY + "Teleports you to the " + ChatColor.AQUA + "Obsidian",
                        ChatColor.AQUA + "Sanctuary" + ChatColor.GRAY + "!",
                        " ",
                        ChatColor.YELLOW + "Click to travel!");
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, -4f);
                player.sendMessage(ChatColor.RED + "The Dwarven Mines are not available yet!");
            }

            @Override
            public int getSlot()
            {
                return 32;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Dwarven Mines", Material.PRISMARINE, (short) 0, 1,
                        ChatColor.RED + "Not available yet!");
            }
        });
    }
}
package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface GUIClickableItem extends GUIItem
{
    void run(InventoryClickEvent e);

    static GUIClickableItem getCloseItem(int slot)
    {
        return new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                e.getWhoClicked().closeInventory();
            }
            @Override
            public int getSlot()
            {
                return slot;
            }
            @Override
            public ItemStack getItem()
            {
                return SUtil.createNamedItemStack(Material.BARRIER, ChatColor.RED + "Close");
            }
        };
    }

    static GUIClickableItem createGUIOpenerItem(GUI gui, Player player, String name, int slot, Material type, short data, String... lore)
    {
        return new GUIClickableItem()
        {
            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(name, type, data, 1, lore);
            }

            @Override
            public void run(InventoryClickEvent e)
            {
                if (gui == null) return;
                gui.open(player);
            }

            @Override
            public int getSlot()
            {
                return slot;
            }
        };
    }

    static GUIClickableItem createGUIOpenerItem(GUIType guiType, Player player, String name, int slot, Material type, short data, String... lore)
    {
        return createGUIOpenerItem(guiType.getGUI(), player, name, slot, type, data, lore);
    }

    static GUIClickableItem createGUIOpenerItem(GUIType guiType, Player player, String name, int slot, Material type, String... lore)
    {
        return createGUIOpenerItem(guiType, player, name, slot, type, (short) 0, lore);
    }
}
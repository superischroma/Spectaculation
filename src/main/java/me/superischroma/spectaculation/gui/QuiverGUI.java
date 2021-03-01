package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class QuiverGUI extends GUI
{
    public QuiverGUI()
    {
        super("Quiver", 36);
        fill(BLACK_STAINED_GLASS_PANE, 27, 35);
        set(GUIClickableItem.getCloseItem(31));
    }

    @Override
    public void onOpen(GUIOpenEvent e)
    {
        set(GUIClickableItem.createGUIOpenerItem(GUIType.SKYBLOCK_MENU, e.getPlayer(), ChatColor.GREEN + "Go Back", 30, Material.ARROW));
        User user = User.getUser(e.getPlayer().getUniqueId());
        Inventory inventory = e.getInventory();
        for (Map.Entry<SMaterial, Integer> entry : user.getQuiver().entrySet())
            inventory.addItem(SUtil.setStackAmount(SItem.of(entry.getKey()).getStack(), entry.getValue()));
    }

    @Override
    public void onClose(InventoryCloseEvent e)
    {
        User user = User.getUser(e.getPlayer().getUniqueId());
        Inventory inventory = e.getInventory();
        user.clearQuiver();
        for (int i = 0; i < 27; i++)
        {
            ItemStack stack = inventory.getItem(i);
            SItem sItem = SItem.find(stack);
            if (sItem == null)
            {
                sItem = SItem.of(stack);
                if (sItem == null)
                    continue;
            }
            user.addToQuiver(sItem.getType(), stack.getAmount());
        }
        user.save();
    }
}
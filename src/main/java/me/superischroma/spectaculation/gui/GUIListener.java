package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.listener.PListener;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GUIListener extends PListener
{
    private static final Map<UUID, GUIQueryItem> QUERY_MAP = new HashMap<>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        GUI gui = GUI.GUI_MAP.get(e.getWhoClicked().getUniqueId());
        if (gui == null) return;
        if (e.getClickedInventory() == e.getView().getTopInventory())
        {
            int slot = e.getSlot();
            GUIItem item = gui.get(slot);
            if (item != null)
            {
                if (!item.canPickup())
                    e.setCancelled(true);
                if (item instanceof GUIClickableItem)
                {
                    GUIClickableItem clickable = (GUIClickableItem) item;
                    clickable.run(e);
                }
                if (item instanceof GUIQueryItem)
                {
                    GUIQueryItem query = (GUIQueryItem) item;
                    Player player = (Player) e.getWhoClicked();
                    player.closeInventory();
                    player.sendMessage(ChatColor.GREEN + "Enter your query:");
                    QUERY_MAP.put(player.getUniqueId(), query);
                }
            }
        }
        else
            gui.onBottomClick(e);
        gui.update(e.getView().getTopInventory());
    }

    @EventHandler
    public void onGUIOpen(GUIOpenEvent e)
    {
        e.getOpened().onOpen(e);
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent e)
    {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = e.getClickedBlock();
        for (GUIType type : GUIType.values())
        {
            GUI gui = type.getGUI();
            if (gui instanceof BlockBasedGUI)
            {
                if (((BlockBasedGUI) gui).getBlock() == block.getType())
                {
                    e.setCancelled(true);
                    gui.open(e.getPlayer());
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        Player player = e.getPlayer();
        if (!QUERY_MAP.containsKey(player.getUniqueId())) return;
        e.setCancelled(true);
        GUIQueryItem item = QUERY_MAP.get(player.getUniqueId());
        player.sendMessage(ChatColor.GOLD + "Querying for: " + e.getMessage());
        GUI next = item.onQueryFinish(e.getMessage());
        if (next != null)
            next.open(player);
        QUERY_MAP.remove(player.getUniqueId());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e)
    {
        if (!(e.getPlayer() instanceof Player)) return;
        Player player = (Player) e.getPlayer();
        GUI gui = GUI.GUI_MAP.get(player.getUniqueId());
        if (gui == null) return;
        gui.onClose(e);
        GUI.GUI_MAP.remove(player.getUniqueId());
    }
}
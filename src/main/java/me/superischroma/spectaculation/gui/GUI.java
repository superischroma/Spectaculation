package me.superischroma.spectaculation.gui;

import lombok.Getter;
import lombok.Setter;
import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class GUI
{
    public static final ItemStack BLACK_STAINED_GLASS_PANE = SUtil.createColoredStainedGlassPane((short) 15, " ");
    public static final ItemStack RED_STAINED_GLASS_PANE = SUtil.createColoredStainedGlassPane((short) 14, " ");
    public static final ItemStack LIME_STAINED_GLASS_PANE = SUtil.createColoredStainedGlassPane((short) 5, " ");
    public static final Map<UUID, GUI> GUI_MAP = new HashMap<>();

    @Getter
    @Setter
    protected String title;
    @Getter
    protected int size;
    @Getter
    protected List<GUIItem> items;

    public GUI(String title, int size)
    {
        this.title = title;
        this.size = size;
        this.items = new ArrayList<>();
    }

    public GUI(String title)
    {
        this(title, 27);
    }

    public void set(GUIItem item)
    {
        items.removeIf(i -> i.getSlot() == item.getSlot());
        items.add(item);
    }

    public void set(int slot, ItemStack stack, boolean pickup)
    {
        if (stack == null)
        {
            items.removeIf(i -> i.getSlot() == slot);
            return;
        }
        set(new GUIItem()
        {
            @Override
            public int getSlot()
            {
                return slot;
            }
            @Override
            public ItemStack getItem()
            {
                return stack;
            }
            @Override
            public boolean canPickup()
            {
                return pickup;
            }
        });
    }

    public void set(int slot, ItemStack stack)
    {
        set(slot, stack, false);
    }

    public GUIItem get(int slot)
    {
        for (GUIItem item : items)
        {
            if (item.getSlot() == slot)
                return item;
        }
        return null;
    }

    public void fill(ItemStack stack, int cornerSlot, int cornerSlot2, boolean overwrite, boolean pickup)
    {
        if (cornerSlot < 0 || cornerSlot > size)
            throw new IllegalArgumentException("Corner 1 of the border described is out of bounds");
        if (cornerSlot2 < 0 || cornerSlot2 > size)
            throw new IllegalArgumentException("Corner 2 of the border described is out of bounds");
        int topLeft = Math.min(cornerSlot, cornerSlot2);
        int bottomRight = Math.max(cornerSlot, cornerSlot2);
        int topRight;
        for (topRight = bottomRight; topRight > topLeft; topRight -= 9);
        int bottomLeft;
        for (bottomLeft = topLeft; bottomLeft < bottomRight; bottomLeft += 9);
        topRight += 9;
        bottomLeft -= 9;
        for (int y = topLeft; y <= bottomLeft; y += 9)
        {
            for (int x = y; x <= topRight - topLeft + y; x++)
            {
                int f = x;
                if (items.stream().filter(item -> item.getSlot() == f).toArray().length != 0 && !overwrite) continue;
                set(x, stack, pickup);
            }
        }
    }

    public void fill(ItemStack stack, int cornerSlot, int cornerSlot2, boolean pickup)
    {
        fill(stack, cornerSlot, cornerSlot2, true, pickup);
    }

    public void fill(ItemStack stack, int cornerSlot, int cornerSlot2)
    {
        fill(stack, cornerSlot, cornerSlot2, false);
    }

    public void fill(ItemStack stack)
    {
        fill(stack, 0, size - 1);
    }

    public void fill(Material material)
    {
        fill(new ItemStack(material));
    }

    public void border(ItemStack stack, int cornerSlot, int cornerSlot2, boolean overwrite, boolean pickup)
    {
        if (cornerSlot < 0 || cornerSlot > size)
            throw new IllegalArgumentException("Corner 1 of the border described is out of bounds");
        if (cornerSlot2 < 0 || cornerSlot2 > size)
            throw new IllegalArgumentException("Corner 2 of the border described is out of bounds");
        int topLeft = Math.min(cornerSlot, cornerSlot2);
        int bottomRight = Math.max(cornerSlot, cornerSlot2);
        int topRight;
        for (topRight = bottomRight; topRight > topLeft; topRight -= 9);
        int bottomLeft;
        for (bottomLeft = topLeft; bottomLeft < bottomRight; bottomLeft += 9);
        topRight += 9;
        bottomLeft -= 9;
        for (int y = topLeft; y <= bottomLeft; y += 9)
        {
            for (int x = y; x <= topRight - topLeft + y; x++)
            {
                int f = x;
                if (items.stream().filter(item -> item.getSlot() == f).toArray().length != 0 && !overwrite) continue;
                if (y == topLeft || y == bottomLeft)
                    set(x, stack, pickup);
                if (x == y || x == topRight - topLeft + y)
                    set(x, stack, pickup);
            }
        }
    }

    public void border(ItemStack stack, int cornerSlot, int cornerSlot2, boolean pickup)
    {
        border(stack, cornerSlot, cornerSlot2, true, pickup);
    }

    public void border(ItemStack stack, int cornerSlot, int cornerSlot2)
    {
        border(stack, cornerSlot, cornerSlot2, false);
    }

    public void border(ItemStack stack)
    {
        border(stack, 0, size - 1);
    }

    public void add(SMaterial material, byte variant, int amount, boolean pickup)
    {
        for (int i = 0; i < (amount / 64); i++)
        {
            int first = firstEmpty();
            if (first == -1)
                return;
            set(first, SUtil.setStackAmount(SItem.of(material, variant).getStack(), 64), pickup);
        }
        int first = firstEmpty();
        if (first == -1)
            return;
        set(first, SUtil.setStackAmount(SItem.of(material, variant).getStack(), amount % 64), pickup);
    }

    public int firstEmpty()
    {
        for (int i = 0; i < size; i++)
        {
            int finalI = i;
            long found = items.stream().filter((item) -> item.getSlot() == finalI).count();
            if (found == 0)
                return i;
        }
        return -1;
    }

    public void open(Player player)
    {
        early(player);
        Inventory inventory = Bukkit.createInventory(player, size, title);
        GUIOpenEvent openEvent = new GUIOpenEvent(player, this, inventory);
        Spectaculation.getPlugin().getServer().getPluginManager().callEvent(openEvent);
        if (openEvent.isCancelled())
            return;
        for (GUIItem item : items)
            inventory.setItem(item.getSlot(), item.getItem());
        player.openInventory(inventory);
        GUI_MAP.remove(player.getUniqueId());
        GUI_MAP.put(player.getUniqueId(), this);
    }

    // to be overridden if necessary
    public void update(Inventory inventory) {}
    public void onOpen(GUIOpenEvent e) {}
    public void onClose(InventoryCloseEvent e) {}
    public void early(Player player) {}
    public void onBottomClick(InventoryClickEvent e) {}
}
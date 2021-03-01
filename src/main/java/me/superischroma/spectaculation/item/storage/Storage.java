package me.superischroma.spectaculation.item.storage;

import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.util.SUtil;
import net.minecraft.server.v1_8_R3.MojangsonParseException;
import net.minecraft.server.v1_8_R3.MojangsonParser;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Storage implements MaterialStatistics, MaterialFunction, ItemData
{
    private static final Map<UUID, Inventory> OPENED_STORAGE_UNITS = new HashMap<>();
    public static Inventory getCurrentStorageOpened(Player player)
    {
        return OPENED_STORAGE_UNITS.get(player.getUniqueId());
    }
    public static void closeCurrentStorage(Player player)
    {
        OPENED_STORAGE_UNITS.remove(player.getUniqueId());
    }

    @Override
    public boolean isStackable()
    {
        return false;
    }

    @Override
    public void onInteraction(PlayerInteractEvent e)
    {
        SItem instance = SItem.find(e.getItem());
        Inventory inventory = Bukkit.createInventory(e.getPlayer(), getSlots(), instance.getType().getDisplayName(instance.getVariant()));
        for (int i = 0; i < getSlots(); i++)
        {
            try
            {
                NBTTagCompound compound = MojangsonParser.parse(new String(SUtil.gzipUncompress(instance.getData().getCompound("storage_data").getByteArray(String.valueOf(i)))));
                if (compound == null || compound.isEmpty()) continue;
                inventory.setItem(i, SItem.of(compound).getStack());
            }
            catch (MojangsonParseException ex)
            {
            }
        }
        OPENED_STORAGE_UNITS.put(e.getPlayer().getUniqueId(), inventory);
        e.getPlayer().openInventory(inventory);
    }

    @Override
    public NBTTagCompound getData()
    {
        return new NBTTagCompound();
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ITEM;
    }

    public abstract int getSlots();
}
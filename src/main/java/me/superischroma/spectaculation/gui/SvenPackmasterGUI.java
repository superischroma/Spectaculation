package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.SlayerBossType;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SvenPackmasterGUI extends GUI
{
    public SvenPackmasterGUI()
    {
        super("Sven Packmaster", 54);
    }

    @Override
    public void onOpen(GUIOpenEvent e)
    {
        fill(BLACK_STAINED_GLASS_PANE);
        Player player = e.getPlayer();
        set(GUIClickableItem.createGUIOpenerItem(GUIType.SLAYER, player, ChatColor.GREEN + "Go Back", 49, Material.ARROW, ChatColor.GRAY + "To Slayer"));
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                new SlayerConfirmGUI(SlayerBossType.SVEN_PACKMASTER_I, () -> User.getUser(player.getUniqueId()).startSlayerQuest(SlayerBossType.SVEN_PACKMASTER_I)).open(player);
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(SlayerBossType.SVEN_PACKMASTER_I.getDisplayName(), SlayerBossType.SVEN_PACKMASTER_I.getType().getIcon(),
                        (short) 0, 1, SlayerBossType.SVEN_PACKMASTER_I.asLore(true));
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
                new SlayerConfirmGUI(SlayerBossType.SVEN_PACKMASTER_II, () -> User.getUser(player.getUniqueId()).startSlayerQuest(SlayerBossType.SVEN_PACKMASTER_II)).open(player);
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(SlayerBossType.SVEN_PACKMASTER_II.getDisplayName(), SlayerBossType.SVEN_PACKMASTER_II.getType().getIcon(),
                        (short) 0, 1, SlayerBossType.SVEN_PACKMASTER_II.asLore(true));
            }

            @Override
            public int getSlot()
            {
                return 12;
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                new SlayerConfirmGUI(SlayerBossType.SVEN_PACKMASTER_III, () -> User.getUser(player.getUniqueId()).startSlayerQuest(SlayerBossType.SVEN_PACKMASTER_III)).open(player);
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(SlayerBossType.SVEN_PACKMASTER_III.getDisplayName(), SlayerBossType.SVEN_PACKMASTER_III.getType().getIcon(),
                        (short) 0, 1, SlayerBossType.SVEN_PACKMASTER_III.asLore(true));
            }

            @Override
            public int getSlot()
            {
                return 13;
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                new SlayerConfirmGUI(SlayerBossType.SVEN_PACKMASTER_IV, () -> User.getUser(player.getUniqueId()).startSlayerQuest(SlayerBossType.SVEN_PACKMASTER_IV)).open(player);
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(SlayerBossType.SVEN_PACKMASTER_IV.getDisplayName(), SlayerBossType.SVEN_PACKMASTER_IV.getType().getIcon(),
                        (short) 0, 1, SlayerBossType.SVEN_PACKMASTER_IV.asLore(true));
            }

            @Override
            public int getSlot()
            {
                return 14;
            }
        });
    }
}
package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.slayer.SlayerBossType;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RevenantHorrorGUI extends GUI
{
    public RevenantHorrorGUI()
    {
        super("Revenant Horror", 54);
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
                new SlayerConfirmGUI(SlayerBossType.REVENANT_HORROR_I, () -> User.getUser(player.getUniqueId()).startSlayerQuest(SlayerBossType.REVENANT_HORROR_I)).open(player);
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(SlayerBossType.REVENANT_HORROR_I.getDisplayName(), SlayerBossType.REVENANT_HORROR_I.getType().getIcon(),
                        (short) 0, 1, SlayerBossType.REVENANT_HORROR_I.asLore(true));
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
                new SlayerConfirmGUI(SlayerBossType.REVENANT_HORROR_II, () -> User.getUser(player.getUniqueId()).startSlayerQuest(SlayerBossType.REVENANT_HORROR_II)).open(player);
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(SlayerBossType.REVENANT_HORROR_II.getDisplayName(), SlayerBossType.REVENANT_HORROR_II.getType().getIcon(),
                        (short) 0, 1, SlayerBossType.REVENANT_HORROR_II.asLore(true));
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
                new SlayerConfirmGUI(SlayerBossType.REVENANT_HORROR_III, () -> User.getUser(player.getUniqueId()).startSlayerQuest(SlayerBossType.REVENANT_HORROR_III)).open(player);
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(SlayerBossType.REVENANT_HORROR_III.getDisplayName(), SlayerBossType.REVENANT_HORROR_III.getType().getIcon(),
                        (short) 0, 1, SlayerBossType.REVENANT_HORROR_III.asLore(true));
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
                new SlayerConfirmGUI(SlayerBossType.REVENANT_HORROR_IV, () -> User.getUser(player.getUniqueId()).startSlayerQuest(SlayerBossType.REVENANT_HORROR_IV)).open(player);
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(SlayerBossType.REVENANT_HORROR_IV.getDisplayName(), SlayerBossType.REVENANT_HORROR_IV.getType().getIcon(),
                        (short) 0, 1, SlayerBossType.REVENANT_HORROR_IV.asLore(true));
            }

            @Override
            public int getSlot()
            {
                return 14;
            }
        });
    }
}
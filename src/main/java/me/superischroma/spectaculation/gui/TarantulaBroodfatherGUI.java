package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.SlayerBossType;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TarantulaBroodfatherGUI extends GUI
{
    public TarantulaBroodfatherGUI()
    {
        super("Tarantula Broodfather", 54);
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
                new SlayerConfirmGUI(SlayerBossType.TARANTULA_BROODFATHER_I, () -> User.getUser(player.getUniqueId()).startSlayerQuest(SlayerBossType.TARANTULA_BROODFATHER_I)).open(player);
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(SlayerBossType.TARANTULA_BROODFATHER_I.getDisplayName(), SlayerBossType.TARANTULA_BROODFATHER_I.getType().getIcon(),
                        (short) 0, 1, SlayerBossType.TARANTULA_BROODFATHER_I.asLore(true));
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
                new SlayerConfirmGUI(SlayerBossType.TARANTULA_BROODFATHER_II, () -> User.getUser(player.getUniqueId()).startSlayerQuest(SlayerBossType.TARANTULA_BROODFATHER_II)).open(player);
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(SlayerBossType.TARANTULA_BROODFATHER_II.getDisplayName(), SlayerBossType.TARANTULA_BROODFATHER_II.getType().getIcon(),
                        (short) 0, 1, SlayerBossType.TARANTULA_BROODFATHER_II.asLore(true));
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
                new SlayerConfirmGUI(SlayerBossType.TARANTULA_BROODFATHER_III, () -> User.getUser(player.getUniqueId()).startSlayerQuest(SlayerBossType.TARANTULA_BROODFATHER_III)).open(player);
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(SlayerBossType.TARANTULA_BROODFATHER_III.getDisplayName(), SlayerBossType.TARANTULA_BROODFATHER_III.getType().getIcon(),
                        (short) 0, 1, SlayerBossType.TARANTULA_BROODFATHER_III.asLore(true));
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
                new SlayerConfirmGUI(SlayerBossType.TARANTULA_BROODFATHER_IV, () -> User.getUser(player.getUniqueId()).startSlayerQuest(SlayerBossType.TARANTULA_BROODFATHER_IV)).open(player);
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(SlayerBossType.TARANTULA_BROODFATHER_IV.getDisplayName(), SlayerBossType.TARANTULA_BROODFATHER_IV.getType().getIcon(),
                        (short) 0, 1, SlayerBossType.TARANTULA_BROODFATHER_IV.asLore(true));
            }

            @Override
            public int getSlot()
            {
                return 14;
            }
        });
    }
}
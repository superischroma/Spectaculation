package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.enchantment.Enchantment;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.util.SLog;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class AnvilGUI extends GUI implements BlockBasedGUI
{
    private static final ItemStack ANVIL_BARRIER = SUtil.getSingleLoreStack(ChatColor.RED + "Anvil", Material.BARRIER, (short) 0, 1, "Place a target item" +
            " in the left slot and a sacrifice item in the right slot to combine Enchantments!");
    private static final ItemStack DEFAULT_COMBINE_ITEMS = SUtil.getStack(ChatColor.GREEN + "Combine Items", Material.ANVIL, (short) 0, 1,
            ChatColor.GRAY + "Combine the items in the slots",
            ChatColor.GRAY + "to the left and right below.");
    private static final String CANNOT_COMBINE = ChatColor.RED + "These items cannot be combined!";

    public AnvilGUI()
    {
        super("Anvil", 54);
        this.fill(BLACK_STAINED_GLASS_PANE);
        this.fill(SUtil.createColoredStainedGlassPane((short) 14, ""), 45, 53);
        this.set(GUIClickableItem.getCloseItem(49));
        for (int i : Arrays.asList(11, 12, 20))
        {
            this.set(i, SUtil.getSingleLoreStack(ChatColor.GOLD + "Item to Upgrade", Material.STAINED_GLASS_PANE, (short) 14, 1,
                    "The item you want to upgrade should be placed in the slot on this side."));
        }
        for (int i : Arrays.asList(14, 15, 24))
        {
            this.set(i, SUtil.getSingleLoreStack(ChatColor.GOLD + "Item to Sacrifice", Material.STAINED_GLASS_PANE, (short) 14, 1,
                    "The item you are sacrificing in order to upgrade the item on the left should be placed in the slot on this side."));
        }
        this.set(29, null);
        this.set(33, null);
        this.set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                ItemStack current = e.getCurrentItem();
                if (current == null) return;
                if (e.getCurrentItem().getType() == Material.BARRIER)
                {
                    e.setCancelled(true);
                    return;
                }
                Inventory inventory = e.getClickedInventory();
                if (!SUtil.isAir(inventory.getItem(29)) || !SUtil.isAir(inventory.getItem(33)))
                {
                    e.setCancelled(true);
                    return;
                }
                new BukkitRunnable()
                {
                    public void run()
                    {
                        inventory.setItem(e.getSlot(), ANVIL_BARRIER);
                    }
                }.runTaskLater(Spectaculation.getPlugin(), 1);
            }

            @Override
            public int getSlot()
            {
                return 13;
            }

            @Override
            public boolean canPickup()
            {
                return true;
            }

            @Override
            public ItemStack getItem()
            {
                return ANVIL_BARRIER;
            }
        });
        this.set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                HumanEntity entity = e.getWhoClicked();
                Inventory inventory = e.getClickedInventory();
                ItemStack upgrade = inventory.getItem(29);
                ItemStack sacrifice = inventory.getItem(33);
                if (upgrade == null || sacrifice == null)
                {
                    entity.sendMessage(CANNOT_COMBINE);
                    return;
                }
                SItem specUpgrade = SItem.find(upgrade);
                SItem specSacrifice = SItem.find(sacrifice);
                if (specUpgrade == null || specSacrifice == null)
                {
                    entity.sendMessage(CANNOT_COMBINE);
                    return;
                }
                if (specUpgrade.getType() != specSacrifice.getType() && specSacrifice.getType() != SMaterial.ENCHANTED_BOOK)
                {
                    entity.sendMessage(CANNOT_COMBINE);
                    return;
                }
                if (specUpgrade.getType() != SMaterial.ENCHANTED_BOOK)
                {
                    for (Enchantment enchantment : specSacrifice.getEnchantments())
                    {
                        if (!enchantment.getType().getCompatibleTypes().contains(specUpgrade.getType().getStatistics().getSpecificType()))
                        {
                            entity.sendMessage(CANNOT_COMBINE);
                            return;
                        }
                    }
                }
                inventory.setItem(22, DEFAULT_COMBINE_ITEMS);
                setItemTo(true, false, inventory);
                setItemTo(false, false, inventory);
                inventory.setItem(29, null);
                inventory.setItem(33, null);
                entity.getWorld().playSound(entity.getLocation(), Sound.ANVIL_USE, 1f, 1f);
            }

            @Override
            public int getSlot()
            {
                return 22;
            }

            @Override
            public ItemStack getItem()
            {
                return DEFAULT_COMBINE_ITEMS;
            }
        });
    }

    @Override
    public void update(Inventory inventory)
    {
        new BukkitRunnable()
        {
            public void run()
            {
                ItemStack select = inventory.getItem(13);
                if (select != null)
                {
                    if (select.getType() != Material.BARRIER && SUtil.isAir(inventory.getItem(29)) && SUtil.isAir(inventory.getItem(33)))
                        return;
                }
                SItem specUpgrade = SItem.find(inventory.getItem(29));
                SItem specSacrifice = SItem.find(inventory.getItem(33));
                boolean upgradeGreen = false, sacrificeGreen = false;
                if (specUpgrade != null)
                {
                    if (SUtil.isEnchantable(specUpgrade))
                        upgradeGreen = true;
                }
                if (specSacrifice != null)
                {
                    if (SUtil.isEnchantable(specSacrifice))
                        sacrificeGreen = true;
                }
                setItemTo(true, upgradeGreen, inventory);
                setItemTo(false, sacrificeGreen, inventory);
                if (upgradeGreen && sacrificeGreen)
                {
                    if (specUpgrade.getType() != specSacrifice.getType() && specSacrifice.getType() != SMaterial.ENCHANTED_BOOK)
                    {
                        setItemTo(true, false, inventory);
                        setItemTo(false, false, inventory);
                        inventory.setItem(13, ANVIL_BARRIER);
                        inventory.setItem(22, DEFAULT_COMBINE_ITEMS);
                        return;
                    }
                    if (specUpgrade.getType() != SMaterial.ENCHANTED_BOOK)
                    {
                        for (Enchantment enchantment : specSacrifice.getEnchantments())
                        {
                            if (!enchantment.getType().getCompatibleTypes().contains(specUpgrade.getType().getStatistics().getSpecificType()))
                            {
                                setItemTo(true, false, inventory);
                                inventory.setItem(13, ANVIL_BARRIER);
                                inventory.setItem(22, DEFAULT_COMBINE_ITEMS);
                                return;
                            }
                        }
                    }
                    SItem display = specUpgrade.clone();
                    for (Enchantment enchantment : specSacrifice.getEnchantments())
                    {
                        Enchantment equiv = display.getEnchantment(enchantment.getType());
                        SLog.info(equiv);
                        if (equiv != null)
                        {
                            SLog.info("equiv isnt null");
                            if (equiv.getLevel() == enchantment.getLevel())
                            {
                                SLog.info("level is equal");
                                display.removeEnchantment(enchantment.getType());
                                display.addEnchantment(enchantment.getType(), enchantment.getLevel() + 1);
                                continue;
                            }
                        }
                        display.addEnchantment(enchantment.getType(), enchantment.getLevel());
                    }
                    inventory.setItem(13, display.getStack());
                    inventory.setItem(22, getCombineItemsForXP(0));
                    return;
                }
                inventory.setItem(13, ANVIL_BARRIER);
                inventory.setItem(22, DEFAULT_COMBINE_ITEMS);
            }
        }.runTaskLater(Spectaculation.getPlugin(), 1);
    }

    @Override
    public Material getBlock()
    {
        return Material.ANVIL;
    }

    private static void setItemTo(boolean upgrade, boolean green, Inventory inventory)
    {
        for (int i : (upgrade ? Arrays.asList(11, 12, 20) : Arrays.asList(14, 15, 24)))
        {
            inventory.setItem(i, SUtil.getSingleLoreStack(ChatColor.GOLD + "Item to " + (upgrade ? "Upgrade" : "Sacrifice"), Material.STAINED_GLASS_PANE,
                    green ? (short) 5 : (short) 14, 1,
                    (upgrade ? "The item you want to upgrade should be placed in the slot on this side." :
            "The item you are sacrificing in order to upgrade the item on the left should be placed in the slot on this side.")));
        }
    }

    private static ItemStack getCombineItemsForXP(int levels)
    {
        return SUtil.getStack(ChatColor.GREEN + "Combine Items", Material.ANVIL, (short) 0, 1,
                ChatColor.GRAY + "Combine the items in the slots",
                ChatColor.GRAY + "to the left and right below.",
                "",
                ChatColor.GRAY + "Cost",
                ChatColor.DARK_AQUA + "" + levels + " Exp Level" + (levels != 1 ? "s" : ""),
                "",
                ChatColor.YELLOW + "Click to combine!");
    }
}

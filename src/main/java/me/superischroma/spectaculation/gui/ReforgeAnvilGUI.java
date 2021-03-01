package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.Reforgable;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.reforge.ReforgeType;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class ReforgeAnvilGUI extends GUI
{
    private static final ItemStack DEFAULT_REFORGE_ITEM = SUtil.getStack(ChatColor.YELLOW + "Reforge Item", Material.ANVIL, (short) 0, 1,
            ChatColor.GRAY + "Place an item above to reforge",
            ChatColor.GRAY + "it! Reforging items adds a",
            ChatColor.GRAY + "random modifier to the item that",
            ChatColor.GRAY + "grants stat boosts.");
    private static final Map<Rarity, Integer> COST_MAP = new HashMap<>();
    private static final List<UUID> COOLDOWN = new ArrayList<>();

    static
    {
        COST_MAP.put(Rarity.COMMON, 250);
        COST_MAP.put(Rarity.UNCOMMON, 500);
        COST_MAP.put(Rarity.RARE, 1000);
        COST_MAP.put(Rarity.EPIC, 2500);
        COST_MAP.put(Rarity.LEGENDARY, 5000);
        COST_MAP.put(Rarity.MYTHIC, 10000);
        COST_MAP.put(Rarity.SUPREME, 15000);
        COST_MAP.put(Rarity.SPECIAL, 25000);
        COST_MAP.put(Rarity.VERY_SPECIAL, 50000);
        COST_MAP.put(Rarity.EXCLUSIVE, 100000);
    }

    public ReforgeAnvilGUI()
    {
        super("Reforge Item", 45);
        fill(BLACK_STAINED_GLASS_PANE);
        //fill(RED_STAINED_GLASS_PANE, 0, 36);
        //fill(RED_STAINED_GLASS_PANE, 8, 44);
        set(GUIClickableItem.getCloseItem(40));
        set(new GUIClickableItem()
        {

            @Override
            public int getSlot()
            {
                return 22;
            }

            @Override
            public ItemStack getItem()
            {
                return DEFAULT_REFORGE_ITEM;
            }

            @Override
            public boolean canPickup()
            {
                return false;
            }

            @Override
            public void run(InventoryClickEvent e)
            {
                SItem sItem = SItem.find(e.getClickedInventory().getItem(13));
                if (sItem == null) return;
                if (!(sItem.getType().getGenericInstance() instanceof Reforgable)) return;
                List<ReforgeType> possible = Arrays.stream(ReforgeType.values())
                        .filter((type) -> type.getReforge().getCompatibleTypes().contains(sItem.getType().getStatistics().getType()) && type.isAccessible())
                        .collect(Collectors.toList());
                Player player = (Player) e.getWhoClicked();
                if (possible.size() == 0)
                {
                    player.sendMessage(ChatColor.RED + "That item cannot be reforged!");
                    return;
                }
                if (COOLDOWN.contains(player.getUniqueId()))
                {
                    player.sendMessage(ChatColor.RED + "Please wait a little bit before doing this!");
                    return;
                }
                User user = User.getUser(player.getUniqueId());
                int cost = COST_MAP.get(sItem.getRarity());
                if (user.getCoins() - cost < 0)
                {
                    player.sendMessage(ChatColor.RED + "You cannot afford to reforge this!");
                    return;
                }
                String prev = sItem.getFullName();
                user.subCoins(cost);
                sItem.setReforge(possible.get(SUtil.random(0, possible.size() - 1)).getReforge());
                player.playSound(player.getLocation(), Sound.ANVIL_USE, 1f, 1f);
                player.sendMessage(ChatColor.GREEN + "You reforged your " + prev + ChatColor.GREEN + " into a " + sItem.getFullName() + ChatColor.GREEN + "!");
                COOLDOWN.add(player.getUniqueId());
                new BukkitRunnable()
                {
                    public void run()
                    {
                        COOLDOWN.remove(player.getUniqueId());
                    }
                }.runTaskLater(Spectaculation.getPlugin(), 20);
            }
        });
        set(13, null);
    }

    @Override
    public void update(Inventory inventory)
    {
        new BukkitRunnable()
        {
            public void run()
            {
                SItem sItem = SItem.find(inventory.getItem(13));
                if (sItem == null)
                {
                    //SUtil.border(inventory, this, RED_STAINED_GLASS_PANE, 0, 36, true, false);
                    //SUtil.border(inventory, this, RED_STAINED_GLASS_PANE, 8, 44, true, false);
                    inventory.setItem(22, DEFAULT_REFORGE_ITEM);
                    return;
                }
                //SUtil.border(inventory, this, LIME_STAINED_GLASS_PANE, 0, 36, true, false);
                //SUtil.border(inventory, this, LIME_STAINED_GLASS_PANE, 8, 44, true, false);
                inventory.setItem(22, SUtil.getStack(ChatColor.GREEN + "Reforge Item", Material.ANVIL, (short) 0, 1,
                        ChatColor.GRAY + "Reforges the above item, giving",
                        ChatColor.GRAY + "it a random item modifier that",
                        ChatColor.GRAY + "boosts its stats.",
                        " ",
                        ChatColor.GRAY + "Cost",
                        ChatColor.GOLD + SUtil.commaify(COST_MAP.get(sItem.getRarity())) + " Coins",
                        " ",
                        ChatColor.YELLOW + "Click to reforge!"));
            }
        }.runTaskLater(Spectaculation.getPlugin(), 1);
    }
}
package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class DepositGUI extends GUI
{
    public DepositGUI()
    {
        super("Bank Deposit", 36);
    }

    @Override
    public void onOpen(GUIOpenEvent e)
    {
        fill(BLACK_STAINED_GLASS_PANE);
        Player player = e.getPlayer();
        set(GUIClickableItem.createGUIOpenerItem(GUIType.BANKER, player, ChatColor.GREEN + "Go Back", 31, Material.ARROW,
                ChatColor.GRAY + "To Personal Bank Account"));
        User user = User.getUser(player.getUniqueId());
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                long coins = user.getCoins();
                user.subCoins(coins);
                user.addBankCoins(coins);
                user.save();
                player.sendMessage(ChatColor.GREEN + "You have deposited " + ChatColor.GOLD + SUtil.commaify(coins) + " coins" + ChatColor.GREEN +
                        "! You now have " + ChatColor.GOLD + SUtil.commaify(user.getBankCoins()) + " coins " + ChatColor.GREEN + "in your account!");
                GUIType.BANKER.getGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 11;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Your whole purse", Material.CHEST, (short) 0, 64,
                        ChatColor.DARK_GRAY + "Bank deposit",
                        " ",
                        ChatColor.GRAY + "Current balance: " + ChatColor.GOLD + SUtil.commaify(user.getBankCoins()),
                        ChatColor.GRAY + "Amount to deposit: " + ChatColor.GOLD + SUtil.commaify(user.getCoins()),
                        " ",
                        ChatColor.YELLOW + "Click to deposit coins!");
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                long coins = user.getCoins() / 2;
                user.subCoins(coins);
                user.addBankCoins(coins);
                user.save();
                player.sendMessage(ChatColor.GREEN + "You have deposited " + ChatColor.GOLD + SUtil.commaify(coins) + " coins" + ChatColor.GREEN +
                        "! You now have " + ChatColor.GOLD + SUtil.commaify(user.getBankCoins()) + " coins " + ChatColor.GREEN + "in your account!");
                GUIType.BANKER.getGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 13;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Half your purse", Material.CHEST, (short) 0, 32,
                        ChatColor.DARK_GRAY + "Bank deposit",
                        " ",
                        ChatColor.GRAY + "Current balance: " + ChatColor.GOLD + SUtil.commaify(user.getBankCoins()),
                        ChatColor.GRAY + "Amount to deposit: " + ChatColor.GOLD + SUtil.commaify(user.getCoins() / 2),
                        " ",
                        ChatColor.YELLOW + "Click to deposit coins!");
            }
        });

        set(new GUIQueryItem()
        {
            @Override
            public GUI onQueryFinish(String query)
            {
                try
                {
                    long coins = Long.parseLong(query);
                    if (coins < 0)
                    {
                        player.sendMessage(ChatColor.RED + "Enter a positive number!");
                        return null;
                    }
                    if (coins > user.getCoins())
                    {
                        player.sendMessage(ChatColor.RED + "You do not have that many coins!");
                        return null;
                    }
                    user.subCoins(coins);
                    user.addBankCoins(coins);
                    user.save();
                    player.sendMessage(ChatColor.GREEN + "You have deposited " + ChatColor.GOLD + SUtil.commaify(coins) + " coins" + ChatColor.GREEN +
                            "! You now have " + ChatColor.GOLD + SUtil.commaify(user.getBankCoins()) + " coins " + ChatColor.GREEN + "in your account!");
                }
                catch (NumberFormatException ex)
                {
                    player.sendMessage(ChatColor.RED + "That is not a valid number!");
                    return null;
                }
                return new BankerGUI();
            }

            @Override
            public void run(InventoryClickEvent e) {}

            @Override
            public int getSlot()
            {
                return 15;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Specific amount", Material.SIGN, (short) 0, 1,
                        ChatColor.GRAY + "Current balance: " + ChatColor.GOLD + SUtil.commaify(user.getBankCoins()),
                        " ",
                        ChatColor.YELLOW + "Click to deposit coins!");
            }
        });
    }
}
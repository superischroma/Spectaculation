package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WithdrawalGUI extends GUI
{
    public WithdrawalGUI()
    {
        super("Bank Withdrawal", 36);
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
                long coins = user.getBankCoins();
                user.addCoins(coins);
                user.subBankCoins(coins);
                user.save();
                player.sendMessage(ChatColor.GREEN + "You have withdrawn " + ChatColor.GOLD + SUtil.commaify(coins) + " coins" + ChatColor.GREEN +
                        "! You now have " + ChatColor.GOLD + SUtil.commaify(user.getBankCoins()) + " coins " + ChatColor.GREEN + "in your account!");
                GUIType.BANKER.getGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 10;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Everything in the account", Material.DROPPER, (short) 0, 64,
                        ChatColor.DARK_GRAY + "Bank withdrawal",
                        " ",
                        ChatColor.GRAY + "Current balance: " + ChatColor.GOLD + SUtil.commaify(user.getBankCoins()),
                        ChatColor.GRAY + "Amount to withdraw: " + ChatColor.GOLD + SUtil.commaify(user.getBankCoins()),
                        " ",
                        ChatColor.YELLOW + "Click to withdraw coins!");
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                long coins = user.getBankCoins() / 2;
                user.addCoins(coins);
                user.subBankCoins(coins);
                user.save();
                player.sendMessage(ChatColor.GREEN + "You have withdrawn " + ChatColor.GOLD + SUtil.commaify(coins) + " coins" + ChatColor.GREEN +
                        "! You now have " + ChatColor.GOLD + SUtil.commaify(user.getBankCoins()) + " coins " + ChatColor.GREEN + "in your account!");
                GUIType.BANKER.getGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 12;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Half the account", Material.DROPPER, (short) 0, 32,
                        ChatColor.DARK_GRAY + "Bank withdrawal",
                        " ",
                        ChatColor.GRAY + "Current balance: " + ChatColor.GOLD + SUtil.commaify(user.getBankCoins()),
                        ChatColor.GRAY + "Amount to withdraw: " + ChatColor.GOLD + SUtil.commaify(user.getBankCoins() / 2),
                        " ",
                        ChatColor.YELLOW + "Click to withdraw coins!");
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                long coins = Math.round(((Long) user.getBankCoins()).doubleValue() * 0.2);
                user.addCoins(coins);
                user.subBankCoins(coins);
                user.save();
                player.sendMessage(ChatColor.GREEN + "You have withdrawn " + ChatColor.GOLD + SUtil.commaify(coins) + " coins" + ChatColor.GREEN +
                        "! You now have " + ChatColor.GOLD + SUtil.commaify(user.getBankCoins()) + " coins " + ChatColor.GREEN + "in your account!");
                GUIType.BANKER.getGUI().open(player);
            }

            @Override
            public int getSlot()
            {
                return 14;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Withdraw 20%", Material.DROPPER, (short) 0, 1,
                        ChatColor.DARK_GRAY + "Bank withdrawal",
                        " ",
                        ChatColor.GRAY + "Current balance: " + ChatColor.GOLD + SUtil.commaify(user.getBankCoins()),
                        ChatColor.GRAY + "Amount to withdraw: " + ChatColor.GOLD + SUtil.commaify(Math.round(((Long) user.getBankCoins()).doubleValue() * 0.2)),
                        " ",
                        ChatColor.YELLOW + "Click to withdraw coins!");
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
                    if (coins > user.getBankCoins())
                    {
                        player.sendMessage(ChatColor.RED + "You do not have that many coins!");
                        return null;
                    }
                    user.addCoins(coins);
                    user.subBankCoins(coins);
                    user.save();
                    player.sendMessage(ChatColor.GREEN + "You have withdrawn " + ChatColor.GOLD + SUtil.commaify(coins) + " coins" + ChatColor.GREEN +
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
                return 16;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Specific amount", Material.SIGN, (short) 0, 1,
                        ChatColor.GRAY + "Current balance: " + ChatColor.GOLD + SUtil.commaify(user.getBankCoins()),
                        " ",
                        ChatColor.YELLOW + "Click to withdraw coins!");
            }
        });
    }
}
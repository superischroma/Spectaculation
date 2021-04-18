package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.auction.AuctionItem;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ConfirmBidGUI extends GUI
{
    private final AuctionItem item;
    private final long amount;

    public ConfirmBidGUI(AuctionItem item, long amount)
    {
        super("Confirm " + (item.isBin() ? "Purchase" : "Bid"), 27);
        this.item = item;
        this.amount = amount;
    }

    @Override
    public void onOpen(GUIOpenEvent e)
    {
        Player player = e.getPlayer();
        User user = User.getUser(e.getPlayer().getUniqueId());
        int count = item.getItem().getStack().getAmount();
        boolean personal = item.getOwner().getUuid().equals(user.getUuid());
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                if (item.isExpired())
                {
                    player.sendMessage(ChatColor.RED + "The item you are trying to bid on has already expired!");
                    player.closeInventory();
                    return;
                }
                User top = item.getTopBidder();
                if (top != null && top.getUuid().equals(user.getUuid()))
                {
                    player.sendMessage(ChatColor.GREEN + "You are already top bid!");
                    return;
                }
                if (user.getCoins() < amount)
                {
                    player.sendMessage(ChatColor.RED + "You cannot afford this bid!");
                    return;
                }
                if (personal)
                {
                    player.sendMessage(ChatColor.RED + "This is your own auction!");
                    return;
                }
                item.bid(user, amount);
                new AuctionViewGUI(item).open(player);
                if (item.isBin())
                    player.sendMessage(ChatColor.GREEN + "Purchased " + item.getItem().getFullName() + ChatColor.GREEN + " successfully!");
                else
                    player.sendMessage(ChatColor.GREEN + "Bid placed on " + item.getItem().getFullName() + ChatColor.GREEN + " successfully!");
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Confirm", Material.STAINED_CLAY, (short) 13, 1,
                        ChatColor.GRAY + (item.isBin() ? "Purchasing" : "Bidding on") + ": " + (count != 1 ? count + "x " : "") + item.getItem().getFullName(),
                        ChatColor.GRAY + "Cost: " + ChatColor.GOLD + SUtil.commaify(amount) + " coin" + (amount != 1 ? "s" : ""));
            }

            @Override
            public int getSlot()
            {
                return 11;
            }
        });
        List<String> bil = new ArrayList<>();
        bil.add(" ");
        bil.add((count != 1 ? count + "x " : "") + item.getItem().getFullName());
        bil.addAll(item.getItem().getLore().asBukkitLore());
        set(13, SUtil.getStack(ChatColor.YELLOW + "" + ChatColor.BOLD + ChatColor.UNDERLINE + (item.isBin() ? "BUYING ITEM" : "BIDDING ON ITEM") + ":",
                item.getItem().getStack().getType(), item.getItem().getStack().getDurability(), item.getItem().getStack().getAmount(), bil));
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                e.getWhoClicked().closeInventory();
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.RED + "Cancel", Material.STAINED_CLAY, (short) 14, 1);
            }

            @Override
            public int getSlot()
            {
                return 15;
            }
        });
    }
}
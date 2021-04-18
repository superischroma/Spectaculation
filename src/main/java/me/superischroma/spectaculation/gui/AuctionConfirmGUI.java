package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.auction.AuctionEscrow;
import me.superischroma.spectaculation.auction.AuctionItem;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AuctionConfirmGUI extends GUI
{
    public AuctionConfirmGUI()
    {
        super("Confirm", 27);
    }

    @Override
    public void onOpen(GUIOpenEvent e)
    {
        Player player = e.getPlayer();
        User user = User.getUser(e.getPlayer().getUniqueId());
        AuctionEscrow escrow = user.getAuctionEscrow();
        if (escrow == null)
            return;
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                AuctionItem item = AuctionItem.createAuction(escrow.getItem(), escrow.getStarter(), System.currentTimeMillis() + escrow.getDuration(), user.getUuid(), user.isAuctionCreationBIN());
                user.subCoins(escrow.getCreationFee(user.isAuctionCreationBIN()));
                user.setAuctionEscrow(new AuctionEscrow());
                new AuctionViewGUI(item).open(player);
            }

            @Override
            public ItemStack getItem()
            {
                int count = escrow.getItem().getStack().getAmount();
                return SUtil.getStack(ChatColor.GREEN + "Confirm", Material.STAINED_CLAY, (short) 13, 1,
                        ChatColor.GRAY + "Auctioning: " + (count != 1 ? count + "x " : "") + escrow.getItem().getFullName(),
                        ChatColor.GRAY + "Cost: " + ChatColor.GOLD + escrow.getCreationFee(user.isAuctionCreationBIN()) + " coins");
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
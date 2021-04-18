package me.superischroma.spectaculation.command;

import me.superischroma.spectaculation.auction.AuctionItem;
import me.superischroma.spectaculation.gui.AuctionViewGUI;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import java.util.List;
import java.util.UUID;

@CommandParameters(description = "Modify your coin amount.", usage = "/<command> <auction uuid/player name>", aliases = "auction,ah")
public class AuctionHouseCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (args.length != 1) throw new CommandArgumentException();
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        try
        {
            UUID uuid = UUID.fromString(args[0]);
            AuctionItem item = AuctionItem.getAuction(uuid);
            if (item == null)
                throw new IllegalArgumentException();
            new AuctionViewGUI(item).open(sender.getPlayer());
        }
        catch (IllegalArgumentException ex)
        {
            List<AuctionItem> items = AuctionItem.getOwnedAuctions(args[0]);
            if (items == null)
                throw new CommandFailException("This player has no auctions!");
            send(ChatColor.RED + "This feature is not complete yet!");
        }
    }
}

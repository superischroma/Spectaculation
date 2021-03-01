package me.superischroma.spectaculation.command;

import me.superischroma.spectaculation.item.SItem;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandParameters(description = "Recombobulate an item from Spec.", aliases = "recom", permission = "spt.item")
public class RecombobulateCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (args.length != 0) throw new CommandArgumentException();
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        Player player = sender.getPlayer();
        ItemStack stack = player.getInventory().getItemInHand();
        if (stack == null) throw new CommandFailException("You don't have anything in your hand!");
        SItem sItem = SItem.find(stack);
        if (sItem == null) throw new CommandFailException("That item is not from Spec!");
        sItem.setRecombobulated(!sItem.isRecombobulated());
        send("Your " + sItem.getType().getDisplayName(sItem.getVariant()) + " is no" +
                (sItem.isRecombobulated() ? "w" : " longer") + " recombobulated.");
    }
}

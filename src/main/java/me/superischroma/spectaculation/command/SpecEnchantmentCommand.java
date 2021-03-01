package me.superischroma.spectaculation.command;

import me.superischroma.spectaculation.enchantment.EnchantmentType;
import me.superischroma.spectaculation.item.SItem;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandParameters(description = "Adds an enchantment from Spec to the specified item.", aliases = "sench", permission = "spt.item")
public class SpecEnchantmentCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (args.length != 2) throw new CommandArgumentException();
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        Player player = sender.getPlayer();
        ItemStack stack = player.getInventory().getItemInHand();
        if (stack == null) throw new CommandFailException("You don't have anything in your hand!");
        SItem sItem = SItem.find(stack);
        if (sItem == null) throw new CommandFailException("That item is not from Spec!");
        EnchantmentType type = EnchantmentType.getByNamespace(args[0]);
        if (type == null) throw new CommandFailException("Invalid enchantment type!");
        int i = Integer.parseInt(args[1]);
        sItem.addEnchantment(type, i);
        send("Your " + sItem.getType().getDisplayName(sItem.getVariant()) + " now has " + type.getName() + " " + i + " on it.");
    }
}

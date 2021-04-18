package me.superischroma.spectaculation.command;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandParameters(description = "Change the material data of an item.", aliases = "mdata,matdata,md", permission = "spt.item")
public class MaterialDataCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (args.length != 1) throw new CommandArgumentException();
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        Player player = sender.getPlayer();
        short data = Short.parseShort(args[0]);
        ItemStack stack = player.getItemInHand();
        if (stack == null)
            throw new CommandFailException("You are not holding anything!");
        stack.setDurability(data);
        send("This item's material data value has been updated to be " + data + ".");
    }
}

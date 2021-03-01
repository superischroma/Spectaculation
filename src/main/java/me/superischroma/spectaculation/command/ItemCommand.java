package me.superischroma.spectaculation.command;

import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

@CommandParameters(description = "Gives an item from Spec.", aliases = "sitem,specitem", permission = "spt.item")
public class ItemCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (args.length < 1 || args.length > 2) throw new CommandArgumentException();
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        Player player = sender.getPlayer();
        SMaterial material = SMaterial.getMaterial(args[0]);
        if (material == null) throw new CommandFailException("Invalid material.");
        PlayerInventory inv = player.getInventory();
        if (inv.firstEmpty() == -1) throw new CommandFailException("No inventory space.");
        byte variant = 0;
        if (args.length == 2)
            variant = Byte.parseByte(args[1]);
        inv.setItem(inv.firstEmpty(), SItem.of(material, variant).getStack());
        send("You have received a(n) " + material.getDisplayName(variant));
    }
}

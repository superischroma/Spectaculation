package me.superischroma.spectaculation.command;

import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.potion.PotionEffect;
import me.superischroma.spectaculation.potion.PotionEffectType;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandParameters(description = "Adds an potion from Spec to the specified item.", aliases = "spot", permission = "spt.item")
public class SpecPotionCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (args.length != 3) throw new CommandArgumentException();
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        Player player = sender.getPlayer();
        ItemStack stack = player.getInventory().getItemInHand();
        if (stack == null) throw new CommandFailException("You don't have anything in your hand!");
        SItem sItem = SItem.find(stack);
        if (sItem == null) throw new CommandFailException("That item is not from Spec!");
        PotionEffectType type = PotionEffectType.getByNamespace(args[0]);
        if (type == null) throw new CommandFailException("Invalid enchantment type!");
        long duration = Long.parseLong(args[1]);
        int level = Integer.parseInt(args[2]);
        sItem.addPotionEffect(new PotionEffect(type, level, duration));
        send("Your " + sItem.getType().getDisplayName(sItem.getVariant()) + " now has " + type.getName() + " " + level + " on it.");
    }
}

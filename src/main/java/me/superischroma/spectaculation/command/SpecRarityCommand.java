package me.superischroma.spectaculation.command;

import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.SItem;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandParameters(description = "Modifies the rarity of an item.", aliases = "srar", permission = "spt.item")
public class SpecRarityCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (args.length > 1) throw new CommandArgumentException();
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        Player player = sender.getPlayer();
        ItemStack stack = player.getInventory().getItemInHand();
        if (stack == null) throw new CommandFailException("You don't have anything in your hand!");
        SItem sItem = SItem.find(stack);
        if (sItem == null) throw new CommandFailException("That item is not from Spec!");
        if (args.length == 0)
        {
            send("Your " + sItem.getType().getDisplayName(sItem.getVariant()) + ChatColor.GRAY +
                    " is " + sItem.getRarity().getDisplay() + ChatColor.GRAY + ".");
            return;
        }
        Rarity prev = sItem.getRarity();
        switch (args[0])
        {
            case "up":
            case "upgrade":
            {
                sItem.upgradeRarity();
                send("Your " + sItem.getType().getDisplayName(sItem.getVariant()) + ChatColor.GRAY + " has been upgraded. (" +
                        prev.getDisplay() + ChatColor.GRAY + " -> " + sItem.getRarity().getDisplay() + ChatColor.GRAY + ")");
                return;
            }
            case "down":
            case "downgrade":
            {
                sItem.downgradeRarity();
                send("Your " + sItem.getType().getDisplayName(sItem.getVariant()) + ChatColor.GRAY + " has been downgraded. (" +
                        prev.getDisplay() + ChatColor.GRAY + " -> " + sItem.getRarity().getDisplay() + ChatColor.GRAY + ")");
                return;
            }
        }
        Rarity chosen = Rarity.getRarity(args[0]);
        if (chosen == null)
            throw new CommandFailException("That rarity does not exist!");
        sItem.setRarity(chosen);
        send("Your " + sItem.getType().getDisplayName(sItem.getVariant()) + ChatColor.GRAY + "'s rarity has been modified. (" +
                prev.getDisplay() + ChatColor.GRAY + " -> " + sItem.getRarity().getDisplay() + ChatColor.GRAY + ")");
    }
}

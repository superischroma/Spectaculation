package me.superischroma.spectaculation.command;

import me.superischroma.spectaculation.potion.ActivePotionEffect;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

@CommandParameters(description = "Get your current active potion effects.", aliases = "seff", permission = "spt.item")
public class SpecEffectsCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (args.length > 1) throw new CommandArgumentException();
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        if (args.length == 1 && args[0].equalsIgnoreCase("clear"))
        {
            sender.getUser().clearPotionEffects();
            send("Cleared active effects.");
            return;
        }
        send("Current active effects:");
        for (ActivePotionEffect effect : sender.getUser().getEffects())
            send(" - " + effect.getEffect().getType().getName() + " " + SUtil.toRomanNumeral(effect.getEffect().getLevel()) + ChatColor.GRAY + " (" + effect.getRemainingDisplay() + ")");
    }
}

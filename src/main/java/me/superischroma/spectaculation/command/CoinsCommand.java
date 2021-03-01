package me.superischroma.spectaculation.command;

import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.command.ConsoleCommandSender;

@CommandParameters(description = "Modify your coin amount.", permission = "spt.balance")
public class CoinsCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (args.length != 0 && args.length != 2) throw new CommandArgumentException();
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        User user = sender.getUser();
        if (args.length == 0)
        {
            user.setPermanentCoins(!user.isPermanentCoins());
            send("Your coins are no" + (user.isPermanentCoins() ? "w" : " longer") + " permanent.");
            return;
        }
        long coins = Long.parseLong(args[1]);
        switch (args[0].toLowerCase())
        {
            case "add":
            {
                user.addCoins(coins);
                send("You have added " + SUtil.commaify(coins) + " to your purse.");
                return;
            }
            case "subtract":
            case "sub":
            {
                user.subCoins(coins);
                send("You have subtracted " + SUtil.commaify(coins) + " from your purse.");
                return;
            }
            case "set":
            {
                user.setCoins(coins);
                send("You have set your purse coins to " + SUtil.commaify(coins) + ".");
                return;
            }
        }
        throw new CommandArgumentException();
    }
}

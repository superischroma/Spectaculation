package me.superischroma.spectaculation.command;

import me.superischroma.spectaculation.region.LauncherRegion;
import me.superischroma.spectaculation.region.RegionType;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Manage world launcher destinations.", usage = "/<command> <region> [add | delete <index> | list]", aliases = "lreg,lregion", permission = "spt.region")
public class LauncherRegionCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        if (args.length == 0 || args.length > 3) throw new CommandArgumentException();
        LauncherRegion launcherRegion = LauncherRegion.get(args[0]);
        if (launcherRegion == null) throw new CommandFailException("That region does not exist!");
        if (launcherRegion.getType() != RegionType.LAUNCHER) throw new CommandFailException("That is not a launcher region!");
        Player player = sender.getPlayer();
        Location location = player.getLocation();
        if (args.length == 3)
        {
            switch (args[1].toLowerCase())
            {
                case "delete":
                {
                    int index = Integer.parseInt(args[2]);
                    Location dest = launcherRegion.getDestinations().get(index - 1);
                    boolean removed = launcherRegion.removeDestination(index - 1);
                    if (!removed) throw new CommandFailException("Invalid index.");
                    launcherRegion.delete(dest);
                    send("Removed (" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ") from the launcher destinations!");
                    return;
                }
            }
        }
        if (args.length == 2)
        {
            switch (args[1].toLowerCase())
            {
                case "add":
                {
                    launcherRegion.addDestination(new Location(player.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
                    launcherRegion.save();
                    send("Added (" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ") as a launcher destination!");
                    return;
                }
                case "list":
                {
                    StringBuilder result = new StringBuilder()
                            .append("Launcher destinations for \"").append(launcherRegion.getName()).append("\"");
                    for (int i = 0; i < launcherRegion.getDestinations().size(); i++)
                        result.append("\n").append(i + 1).append(". ").append(launcherRegion.getDestinations().get(i).toString());
                    send(result.toString());
                    return;
                }
            }
        }
        throw new CommandArgumentException();
    }
}
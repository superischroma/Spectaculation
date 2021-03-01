package me.superischroma.spectaculation.command;

import me.superischroma.spectaculation.region.Region;
import me.superischroma.spectaculation.region.RegionGenerator;
import me.superischroma.spectaculation.region.RegionType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

@CommandParameters(description = "Manage world regions.", usage = "/<command> [create <name> <type> | update <name> [type] | delete <name>]", aliases = "reg", permission = "spt.region")
public class RegionCommand extends SCommand
{
    public static Map<CommandSender, RegionGenerator> REGION_GENERATION_MAP = new HashMap<>();

    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (args.length == 3)
        {
            String name = args[1];
            RegionType type = RegionType.valueOf(args[2].toUpperCase());
            switch (args[0].toLowerCase())
            {
                case "create":
                {
                    if (name.length() > 100) throw new CommandFailException("Name too long!");
                    if (plugin.regionData.exists(name)) throw new CommandFailException("There is already a region named that!");
                    REGION_GENERATION_MAP.put(sender.getSender(), new RegionGenerator("create", name, type));
                    send("Created a region named \"" + name + "\"");
                    send(ChatColor.DARK_AQUA + "Click the first corner of your region.");
                    return;
                }
            }
        }
        if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("delete"))
            {
                String name = args[1];
                Region region = Region.get(name);
                if (region == null) throw new CommandFailException("There is no region named that!");
                region.delete();
                send("Deleted region \"" + name + "\" successfully.");
                return;
            }
        }
        if (args.length == 2 || args.length == 3)
        {
            String name = args[1];
            Region region = Region.get(name);
            if (region == null) throw new CommandFailException("There is no region named that!");
            RegionType type = region.getType();
            if (args.length == 3)
                type = RegionType.valueOf(args[2].toUpperCase());
            if (!args[0].equalsIgnoreCase("update")) throw new CommandArgumentException();
            REGION_GENERATION_MAP.put(sender.getSender(), new RegionGenerator("update", name, type));
            send("Updating \"" + name + "\"");
            send(ChatColor.DARK_AQUA + "Click the first corner of your region.");
            return;
        }
        if (args.length != 0) throw new CommandArgumentException();
        StringBuilder result = new StringBuilder()
                .append("Regions");
        for (Region region : Region.getRegions())
        {
            result.append("\n")
                    .append(" - ").append(region.getName()).append(" (")
                    .append(region.getType().name().toLowerCase()).append(")");
        }
        send(result.toString());
    }
}
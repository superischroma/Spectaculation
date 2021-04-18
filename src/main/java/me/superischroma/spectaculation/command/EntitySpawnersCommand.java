package me.superischroma.spectaculation.command;

import me.superischroma.spectaculation.entity.EntitySpawner;
import me.superischroma.spectaculation.entity.SEntityType;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@CommandParameters(description = "Manage entity spawners.", usage = "/<command> [create <type> | delete <index>]", aliases = "entityspawner,es,spawner,spawners", permission = "spt.entity")
public class EntitySpawnersCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        Player player = sender.getPlayer();
        if (args.length == 0)
        {
            StringBuilder builder = new StringBuilder("Spawners:");
            List<EntitySpawner> spawners = EntitySpawner.getSpawners();
            for (int i = 0; i < spawners.size(); i++)
            {
                EntitySpawner spawner = spawners.get(i);
                builder.append("\n ").append(i + 1).append(": ").append(SUtil.prettify(spawner.getLocation()))
                        .append(" (").append(spawner.getType().name()).append(")");
            }
            send(builder.toString());
            return;
        }
        if (args.length != 2) throw new CommandArgumentException();
        switch (args[0].toLowerCase())
        {
            case "create":
            {
                SEntityType type = SEntityType.getEntityType(args[1]);
                if (type == null)
                    throw new CommandFailException("That is not a valid entity type!");
                EntitySpawner spawner = new EntitySpawner(type, player.getLocation());
                send("New entity spawner has been created at " + SUtil.prettify(spawner.getLocation()) + " with the type " + spawner.getType().getGenericInstance());
                break;
            }
            case "delete":
            {
                int index = Integer.parseInt(args[1]) - 1;
                List<EntitySpawner> spawners = EntitySpawner.getSpawners();
                if (index < 0 || index > spawners.size() - 1)
                    throw new CommandFailException("There is no spawner at that location!");
                spawners.remove(index).delete();
                send("Entity spawner deleted.");
                break;
            }
        }
    }
}

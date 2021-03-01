package me.superischroma.spectaculation.command;

import me.superischroma.spectaculation.entity.SEntity;
import me.superischroma.spectaculation.entity.SEntityType;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "Spawn a mob from Spec.", aliases = "spspec", permission = "spt.spawn")
public class SpawnSpecCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (args.length == 0) throw new CommandArgumentException();
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        Player player = sender.getPlayer();
        SEntityType type = SEntityType.getEntityType(args[0]);
        if (type == null) throw new CommandFailException("Invalid entity type.");
        SEntity entity;
        switch (type)
        {
            case REVENANT_HORROR:
            case SVEN_PACKMASTER:
            case TARANTULA_BROODFATHER:
            {
                if (args.length != 2)
                    throw new CommandArgumentException();
                int tier = Integer.parseInt(args[1]);
                entity = new SEntity(player, type, tier, player.getUniqueId());
                break;
            }
            default:
                entity = new SEntity(player, type);
                break;
        }
        send("You have spawned a(n) " + entity.getStatistics().getEntityName());
    }
}

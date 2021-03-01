package me.superischroma.spectaculation.command;

import net.minecraft.server.v1_8_R3.EntityHuman;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.entity.Player;

@CommandParameters(description = "Modify your absorption amount.", permission = "spt.player")
public class AbsorptionCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (args.length != 1) throw new CommandArgumentException();
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        Player player = sender.getPlayer();
        EntityHuman human = ((CraftHumanEntity) player).getHandle();
        float f = Float.parseFloat(args[0]);
        human.setAbsorptionHearts(f);
        send("You now have " + f + " absorption hearts.");
    }
}

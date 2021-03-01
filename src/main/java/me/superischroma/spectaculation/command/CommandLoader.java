package me.superischroma.spectaculation.command;

import java.util.ArrayList;
import java.util.List;

public class CommandLoader
{
    private final List<SCommand> commands;

    public CommandLoader()
    {
        this.commands = new ArrayList<>();
    }

    public void register(SCommand command)
    {
        commands.add(command);
        command.register();
    }

    public int getCommandAmount()
    {
        return commands.size();
    }
}
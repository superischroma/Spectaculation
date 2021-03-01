package me.superischroma.spectaculation.command;

public class CommandFailException extends RuntimeException
{
    public CommandFailException(String message)
    {
        super(message);
    }
}
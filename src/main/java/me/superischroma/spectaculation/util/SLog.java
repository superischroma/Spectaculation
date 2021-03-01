package me.superischroma.spectaculation.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SLog
{
    private static final Logger LOGGER = Logger.getLogger("Minecraft");
    private static final String PREFIX = "[Spectaculation]";

    private static void log(Object o, Level l)
    {
        LOGGER.log(l, PREFIX + " " + o);
    }

    public static void info(Object o)
    {
        log(o, Level.INFO);
    }

    public static void warn(Object o)
    {
        log(o, Level.WARNING);
    }

    public static void severe(Object o)
    {
        log(o, Level.SEVERE);
    }
}
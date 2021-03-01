package me.superischroma.spectaculation.listener;

import me.superischroma.spectaculation.Spectaculation;
import org.bukkit.event.Listener;

public class PListener implements Listener
{
    private static int amount = 0;

    protected Spectaculation plugin;

    protected PListener()
    {
        this.plugin = Spectaculation.getPlugin();
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        amount++;
    }

    public static int getAmount()
    {
        return amount;
    }
}
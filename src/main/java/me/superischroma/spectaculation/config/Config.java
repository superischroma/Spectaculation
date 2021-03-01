package me.superischroma.spectaculation.config;

import me.superischroma.spectaculation.Spectaculation;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config extends YamlConfiguration
{
    private final Spectaculation plugin;
    private final File file;

    public Config(File parent, String name)
    {
        this.plugin = Spectaculation.getPlugin();
        this.file = new File(parent, name);

        if (!file.exists())
        {
            options().copyDefaults(true);
            plugin.saveResource(name, false);
        }
        load();
    }

    public Config(String name)
    {
        this(Spectaculation.getPlugin().getDataFolder(), name);
    }

    public void load()
    {
        try
        {
            super.load(file);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void save()
    {
        try
        {
            super.save(file);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
package me.superischroma.spectaculation.entity;

import me.superischroma.spectaculation.Spectaculation;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.*;

public final class StaticDragonManager
{
    public static boolean ACTIVE = false;
    public static Map<UUID, List<Location>> EYES = new HashMap<>();
    public static SEntity DRAGON = null;

    public static void endFight()
    {
        if (DRAGON == null) return;
        ACTIVE = false;
        for (List<Location> locations : StaticDragonManager.EYES.values())
        {
            for (Location location : locations)
            {
                Block b = location.getBlock();
                BlockState s = b.getState();
                s.setRawData((byte) 0);
                s.update();
                b.removeMetadata("placer", Spectaculation.getPlugin());
            }
        }
        EYES.clear();
        DRAGON = null;
    }
}
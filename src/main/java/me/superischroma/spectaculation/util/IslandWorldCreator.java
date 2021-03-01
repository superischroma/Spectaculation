package me.superischroma.spectaculation.util;

import lombok.Getter;
import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.user.User;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;
import java.util.Random;
import java.util.UUID;

public class IslandWorldCreator extends WorldCreator
{
    @Getter
    private final World world;

    public IslandWorldCreator(User user)
    {
        super(UUID.randomUUID().toString());
        World world = this.createWorld();
        this.world = world;
        this.generator();
        File file = new File(Spectaculation.getPlugin().config.getString("island_schematic_location"));
        SUtil.pasteSchematic(file, new Location(world, 7.0, 100.0, 7.0), true);
        SUtil.setBlocks(new Location(world, 7.0, 104.0, 44.0),
                new Location(world, 5.0, 100.0, 44.0), Material.PORTAL, false);
        user.setIsland(world);
        user.save();
    }

    @Override
    public ChunkGenerator generator()
    {
        return new ChunkGenerator()
        {
            @Override
            public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome)
            {
                return this.createChunkData(world);
            }
        };
    }
}
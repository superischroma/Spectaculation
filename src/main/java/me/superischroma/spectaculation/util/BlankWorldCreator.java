package me.superischroma.spectaculation.util;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class BlankWorldCreator extends WorldCreator // todo: fix this
{
    public BlankWorldCreator(String name)
    {
        super(name);
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

            @Override
            public byte[] generate(World world, Random random, int x, int z)
            {
                return new byte[32768];
            }

            @Override
            public byte[][] generateBlockSections(World world, Random random, int x, int z, BiomeGrid biomes)
            {
                return new byte[16][16];
            }

            @Override
            public short[][] generateExtBlockSections(World world, Random random, int x, int z, BiomeGrid biomes)
            {
                return new short[world.getMaxHeight() / 16][];
            }
        };
    }
}
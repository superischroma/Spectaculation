package me.superischroma.spectaculation.region;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter
public class RegionGenerator
{
    private final String modificationType;
    @Setter
    private String name;
    @Setter
    private Location firstLocation;
    @Setter
    private Location secondLocation;
    @Setter
    private RegionType type;
    @Setter
    private int phase;

    public RegionGenerator(String modificationType, String name, RegionType type)
    {
        this.modificationType = modificationType;
        this.name = name;
        this.type = type;
        this.phase = 1;
    }
}
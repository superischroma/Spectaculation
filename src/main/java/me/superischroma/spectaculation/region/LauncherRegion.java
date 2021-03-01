package me.superischroma.spectaculation.region;

import lombok.Getter;
import org.bukkit.Location;

import java.util.List;

public class LauncherRegion extends Region
{
    @Getter
    private final List<Location> destinations;

    public LauncherRegion(String name, Location firstLocation, Location secondLocation, RegionType type, List<Location> destinations)
    {
        super(name, firstLocation, secondLocation, type);
        this.destinations = destinations;
    }

    public void addDestination(Location location)
    {
        if (destinations.contains(location))
            return;
        destinations.add(location);
    }

    public boolean removeDestination(Location location)
    {
        return destinations.remove(location);
    }

    public boolean removeDestination(int index)
    {
        if (index < 0 || index > destinations.size()) return false;
        destinations.remove(index);
        return true;
    }

    @Override
    public void save()
    {
        plugin.launcherData.save(this);
    }

    public void delete(Location destination)
    {
        plugin.launcherData.delete(name, destination);
    }

    public static LauncherRegion get(String regionName)
    {
        return plugin.launcherData.get(regionName);
    }
}
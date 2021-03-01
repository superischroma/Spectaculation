package me.superischroma.spectaculation.region;

import lombok.Getter;
import lombok.Setter;
import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.util.SLog;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Region
{
    private static final Map<String, Region> REGION_CACHE = new HashMap<>();

    protected static final Spectaculation plugin = Spectaculation.getPlugin();

    protected final String name;
    @Setter
    protected Location firstLocation;
    @Setter
    protected Location secondLocation;
    @Setter
    protected RegionType type;
    @Getter
    private List<BlockState> capture;

    public Region(String name, Location firstLocation, Location secondLocation, RegionType type)
    {
        this.name = name.toLowerCase();
        this.firstLocation = firstLocation;
        this.secondLocation = secondLocation;
        this.type = type;
        this.capture = null;
        REGION_CACHE.put(this.name, this);
    }

    public void save()
    {
        plugin.regionData.save(this);
    }

    public void delete()
    {
        REGION_CACHE.remove(name);
        plugin.regionData.delete(this);
    }

    public static List<Entity> getPlayersWithinRegionType(RegionType type)
    {
        List<Entity> players = new ArrayList<>();
        for (Region region : getRegionsOfType(type))
            players.addAll(region.getPlayersWithinRegion());
        return players;
    }

    public static Region getAreaOfEntity(Entity entity)
    {
        List<Region> possible = new ArrayList<>();
        for (Region region : getRegions())
        {
            if (!region.getType().isArea()) continue;
            if (region.insideRegion(entity))
                possible.add(region);
        }
        possible.sort(Comparator.comparingInt(r -> r.getType().ordinal()));
        Collections.reverse(possible);
        return possible.size() != 0 ? possible.get(0) : null;
    }

    public static Region getRegionOfEntity(Entity entity)
    {
        for (Region region : getRegions())
        {
            if (region.getType().isArea()) continue;
            if (region.insideRegion(entity))
                return region;
        }
        return null;
    }

    public static Region getRegionOfBlock(Block block)
    {
        List<Region> possible = new ArrayList<>();
        for (Region region : getRegions())
        {
            if (region.insideRegion(block))
                possible.add(region);
        }
        possible.sort(Comparator.comparingInt(r -> r.getType().ordinal()));
        Collections.reverse(possible);
        return possible.size() != 0 ? possible.get(0) : null;
    }

    public boolean insideRegion(Entity entity)
    {
        List<Integer> bounds = getBounds();
        Location location = entity.getLocation();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        return x >= (double) bounds.get(0) && x <= (double) bounds.get(1) &&
                y >= (double) bounds.get(2) && y <= (double) bounds.get(3) &&
                z >= (double) bounds.get(4) && z <= (double) bounds.get(5);
    }

    public boolean insideRegion(Block block)
    {
        List<Integer> bounds = getBounds();
        Location location = block.getLocation();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        return x >= (double) bounds.get(0) && x <= (double) bounds.get(1) &&
                y >= (double) bounds.get(2) && y <= (double) bounds.get(3) &&
                z >= (double) bounds.get(4) && z <= (double) bounds.get(5);
    }

    public List<Location> getAvailableTeleportLocations()
    {
        List<Location> locations = new ArrayList<>();
        for (Location location : getLocations())
        {
            Block block = location.getBlock();
            if (block.getType() == Material.AIR || block.getType() == Material.WATER)
                continue;
            Block above = location.clone().add(0, 1, 0).getBlock();
            Block top = location.clone().add(0, 2, 0).getBlock();
            if ((above.getType() != Material.AIR && above.getType() != Material.WATER) ||
                    (top.getType() != Material.AIR && top.getType() != Material.WATER))
                continue;
            locations.add(above.getLocation());
        }
        return locations;
    }

    public List<Location> getLocations()
    {
        if (!firstLocation.getWorld().getName().equals(secondLocation.getWorld().getName()))
            return null;
        List<Integer> bounds = getBounds();
        World world = firstLocation.getWorld();
        List<Location> locations = new ArrayList<>();
        for (int y = bounds.get(2); y <= bounds.get(3); y++)
        {
            for (int x = bounds.get(0); x <= bounds.get(1); x++)
            {
                for (int z = bounds.get(4); z <= bounds.get(5); z++)
                {
                    locations.add(new Location(world, x, y, z));
                }
            }
        }
        return locations;
    }

    public void captureRegion()
    {
        if (!firstLocation.getWorld().getName().equals(secondLocation.getWorld().getName()))
            return;
        List<Integer> bounds = getBounds();
        World world = firstLocation.getWorld();
        List<BlockState> states = new ArrayList<>();
        for (int y = bounds.get(2); y <= bounds.get(3); y++)
        {
            for (int x = bounds.get(0); x <= bounds.get(1); x++)
            {
                for (int z = bounds.get(4); z <= bounds.get(5); z++)
                {
                    states.add(new Location(world, x, y, z).getBlock().getState());
                }
            }
        }
        capture = states;
    }

    public void pasteRegionCapture()
    {
        if (capture == null)
            return;
        for (BlockState state : capture)
        {
            state.getBlock().setType(state.getType());
            state.setRawData(state.getRawData());
            state.update();
        }
        capture = null;
    }

    public Location getRandomLocation()
    {
        List<Integer> bounds = getBounds();
        return new Location(firstLocation.getWorld(), SUtil.random(bounds.get(0), bounds.get(1)),
                SUtil.random(bounds.get(2), bounds.get(3)), SUtil.random(bounds.get(4), bounds.get(5)));
    }

    public Location getRandomAvailableLocation()
    {
        Location r = getRandomLocation();
        List<Location> possible = new ArrayList<>();
        for (int y = getBounds().get(3); y >= getBounds().get(2); y--)
        {
            Block test = firstLocation.getWorld().getBlockAt(r.getBlockX(), y, r.getBlockZ());
            if (test.getType() != Material.AIR && test.getLocation().clone().add(0, 1, 0).getBlock().getType() == Material.AIR &&
                    test.getLocation().clone().add(0, 2, 0).getBlock().getType() == Material.AIR)
            {
                possible.add(test.getLocation().clone().add(0, 1, 0));
            }
        }
        return !possible.isEmpty() ? SUtil.getRandom(possible) : null;
    }

    public List<Integer> getBounds()
    {
        int sx = Math.min(firstLocation.getBlockX(), secondLocation.getBlockX()),
                ex = Math.max(firstLocation.getBlockX(), secondLocation.getBlockX()),
                sy = Math.min(firstLocation.getBlockY(), secondLocation.getBlockY()),
                ey = Math.max(firstLocation.getBlockY(), secondLocation.getBlockY()),
                sz = Math.min(firstLocation.getBlockZ(), secondLocation.getBlockZ()),
                ez = Math.max(firstLocation.getBlockZ(), secondLocation.getBlockZ());
        return Arrays.asList(sx, ex, sy, ey, sz, ez);
    }

    public List<Entity> getPlayersWithinRegion()
    {
        List<Entity> entities = new ArrayList<>(firstLocation.getWorld().getEntitiesByClasses(Player.class));
        return entities.stream().filter(this::insideRegion).collect(Collectors.toList());
    }

    public static Region create(String name, Location firstLocation, Location secondLocation, RegionType type)
    {
        return plugin.regionData.create(name, firstLocation, secondLocation, type);
    }

    public static Region get(String name)
    {
        if (REGION_CACHE.containsKey(name))
            return REGION_CACHE.get(name);
        return plugin.regionData.get(name);
    }

    public static List<Region> getRegions()
    {
        return new ArrayList<>(REGION_CACHE.values());
    }

    public static List<Region> getRegionsOfType(RegionType type)
    {
        return getRegions().stream().filter(region -> region.getType() == type).collect(Collectors.toList());
    }

    public static void cacheRegions()
    {
        for (Region region : plugin.regionData.getAll())
            REGION_CACHE.put(region.getName(), region);
    }
}

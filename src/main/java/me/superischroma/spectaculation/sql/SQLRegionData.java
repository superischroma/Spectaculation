package me.superischroma.spectaculation.sql;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.region.Region;
import me.superischroma.spectaculation.region.RegionType;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLRegionData
{
    private static final Spectaculation plugin = Spectaculation.getPlugin();

    private final String SELECT = "SELECT * FROM `regions` WHERE name=?";
    private final String SELECT_TYPE = "SELECT * FROM `regions` WHERE type=?";
    private final String INSERT = "INSERT INTO `regions` (`name`, `x1`, `y1`, `z1`, `x2`, `y2`, `z2`, `world`, `type`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private final String UPDATE = "UPDATE `regions` SET x1=?, y1=?, z1=?, x2=?, y2=?, z2=?, world=?, type=? WHERE name=?";
    private final String COUNT = "SELECT COUNT(*) AS rows FROM `regions`";
    private final String DELETE = "DELETE FROM `regions` WHERE name=?";

    public boolean exists(String regionName)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(SELECT);
            statement.setString(1, regionName);
            ResultSet set = statement.executeQuery();
            return set.next();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return false;
    }

    public Region get(String name)
    {
        if (!exists(name)) return null;
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(SELECT);
            statement.setString(1, name);
            ResultSet set = statement.executeQuery();
            set.next();
            Location firstLocation = new Location(plugin.worldData.getWorld(set.getInt("world")), set.getInt("x1"),
                    set.getInt("y1"),
                    set.getInt("z1"));
            Location secondLocation = new Location(plugin.worldData.getWorld(set.getInt("world")), set.getInt("x2"),
                    set.getInt("y2"),
                    set.getInt("z2"));
            RegionType type = RegionType.valueOf(set.getString("type"));
            set.close();
            return new Region(name, firstLocation, secondLocation, type);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Region> getAllOfType(RegionType type)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(SELECT_TYPE);
            statement.setInt(1, type.ordinal());
            ResultSet set = statement.executeQuery();
            List<Region> regions = new ArrayList<>();
            while (set.next())
            {
                String name = set.getString("name");
                Location firstLocation = new Location(plugin.worldData.getWorld(set.getInt("world")), set.getInt("x1"),
                        set.getInt("y1"),
                        set.getInt("z1"));
                Location secondLocation = new Location(plugin.worldData.getWorld(set.getInt("world")), set.getInt("x2"),
                        set.getInt("y2"),
                        set.getInt("z2"));
                regions.add(new Region(name, firstLocation, secondLocation, type));
            }
            set.close();
            return regions;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    public List<Region> getAll()
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `regions`");
            ResultSet set = statement.executeQuery();
            List<Region> regions = new ArrayList<>();
            while (set.next()) regions.add(get(set.getString("name")));
            set.close();
            return regions;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public Region create(String name, Location firstLocation, Location secondLocation, RegionType type)
    {
        name = name.toLowerCase();
        if (exists(name)) return get(name);
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(INSERT);
            statement.setString(1, name);
            statement.setInt(2, (int) firstLocation.getX());
            statement.setInt(3, (int) firstLocation.getY());
            statement.setInt(4, (int) firstLocation.getZ());
            statement.setInt(5, (int) secondLocation.getX());
            statement.setInt(6, (int) secondLocation.getY());
            statement.setInt(7, (int) secondLocation.getZ());
            statement.setInt(8, plugin.worldData.getWorldID(firstLocation.getWorld()));
            statement.setString(9, type.name());
            statement.execute();
            return new Region(name, firstLocation, secondLocation, type);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public void save(Region region)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setInt(1, (int) region.getFirstLocation().getX());
            statement.setInt(2, (int) region.getFirstLocation().getY());
            statement.setInt(3, (int) region.getFirstLocation().getZ());
            statement.setInt(4, (int) region.getSecondLocation().getX());
            statement.setInt(5, (int) region.getSecondLocation().getY());
            statement.setInt(6, (int) region.getSecondLocation().getZ());
            statement.setInt(7, plugin.worldData.getWorldID(region.getFirstLocation().getWorld()));
            statement.setString(8, region.getType().name());
            statement.setString(9, region.getName());
            statement.execute();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void delete(Region region)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setString(1, region.getName());
            statement.execute();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public int getRegionCount()
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(COUNT);
            ResultSet set = statement.executeQuery();
            set.next();
            int count = set.getInt("rows");
            set.close();
            return count;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return 0;
    }
}
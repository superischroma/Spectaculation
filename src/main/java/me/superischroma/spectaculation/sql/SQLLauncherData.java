package me.superischroma.spectaculation.sql;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.region.LauncherRegion;
import me.superischroma.spectaculation.region.Region;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLLauncherData
{
    private static final Spectaculation plugin = Spectaculation.getPlugin();

    private final String SELECT = "SELECT * FROM `launchers` WHERE region_name=? AND x=? AND y=? AND z=?";
    private final String SELECT_NAME = "SELECT * FROM `launchers` WHERE region_name=?";
    private final String INSERT = "INSERT INTO `launchers` (`region_name`, `x`, `y`, `z`) VALUES (?, ?, ?, ?);";
    private final String DELETE = "DELETE FROM `launchers` WHERE region_name=? AND x=? AND y=? AND z=?";

    public boolean exists(String regionName, Location destination)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(SELECT);
            statement.setString(1, regionName);
            statement.setInt(2, (int) destination.getX());
            statement.setInt(3, (int) destination.getY());
            statement.setInt(4, (int) destination.getZ());
            ResultSet set = statement.executeQuery();
            return set.next();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return plugin.regionData.exists(regionName);
    }

    public LauncherRegion get(String regionName)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(SELECT_NAME);
            statement.setString(1, regionName);
            ResultSet set = statement.executeQuery();
            Region region = plugin.regionData.get(regionName);
            if (region == null) return null;
            List<Location> destinations = new ArrayList<>();
            while (set.next())
            {
                Location destination = new Location(region.getFirstLocation().getWorld(), set.getInt("x"),
                        set.getInt("y"),
                        set.getInt("z"));
                destinations.add(destination);
            }
            set.close();
            return new LauncherRegion(region.getName(), region.getFirstLocation(), region.getSecondLocation(), region.getType(), destinations);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public void save(LauncherRegion region)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            for (Location destination : region.getDestinations())
            {
                if (exists(region.getName(), destination)) continue;
                PreparedStatement statement = connection.prepareStatement(INSERT);
                statement.setString(1, region.getName());
                statement.setInt(2, (int) destination.getX());
                statement.setInt(3, (int) destination.getY());
                statement.setInt(4, (int) destination.getZ());
                statement.execute();
            }
            plugin.regionData.save(region);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void delete(String regionName, Location destination)
    {
        if (!exists(regionName, destination)) return;
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setString(1, regionName);
            statement.setInt(2, (int) destination.getX());
            statement.setInt(3, (int) destination.getY());
            statement.setInt(4, (int) destination.getZ());
            statement.execute();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
}
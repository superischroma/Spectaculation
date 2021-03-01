package me.superischroma.spectaculation.sql;

import me.superischroma.spectaculation.Spectaculation;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLWorldData
{
    private static final Spectaculation plugin = Spectaculation.getPlugin();

    private final String SELECT = "SELECT * FROM `worlds` WHERE name=?";
    private final String SELECT_ID = "SELECT * FROM `worlds` WHERE id=?";
    private final String INSERT = "INSERT INTO `worlds` (`id`, `name`) VALUES (?, ?);";
    private final String COUNT = "SELECT COUNT(*) AS rows FROM `worlds`";

    public boolean exists(World world)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(SELECT);
            statement.setString(1, world.getName());
            ResultSet set = statement.executeQuery();
            return set.next();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean existsID(int id)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(SELECT_ID);
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            return set.next();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return false;
    }

    public int getWorldID(World world)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            if (!exists(world))
            {
                PreparedStatement statement = connection.prepareStatement(INSERT);
                statement.setInt(1, getWorldCount() + 1);
                statement.setString(2, world.getName());
                statement.execute();
            }
            PreparedStatement statement = connection.prepareStatement(SELECT);
            statement.setString(1, world.getName());
            ResultSet set = statement.executeQuery();
            set.next();
            int id = set.getInt("id");
            set.close();
            return id;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return -1;
    }

    public World getWorld(int id)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            if (!existsID(id)) return null;
            PreparedStatement statement = connection.prepareStatement(SELECT_ID);
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            set.next();
            String name = set.getString("name");
            set.close();
            return Bukkit.getWorld(name);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public int getWorldCount()
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
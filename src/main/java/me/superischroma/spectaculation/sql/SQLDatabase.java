package me.superischroma.spectaculation.sql;

import me.superischroma.spectaculation.Spectaculation;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLDatabase
{
    private static final Spectaculation plugin = Spectaculation.getPlugin();
    private static final String DATABASE_FILENAME = "database.db";

    private Connection connection;
    private File file;

    public SQLDatabase()
    {
        File file = new File(plugin.getDataFolder(), DATABASE_FILENAME);
        if (!file.exists())
        {
            try
            {
                file.getParentFile().mkdirs();
                file.createNewFile();
                plugin.saveResource(DATABASE_FILENAME, false);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        this.file = file;
    }

    public Connection getConnection()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
            if (connection != null)
            {
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS `worlds` (\n" +
                        "\t`id` SMALLINT,\n" +
                        "\t`name` TEXT\n" +
                        ");").execute();
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS `users` (\n" +
                        "\t`id` INT,\n" +
                        "\t`uuid` TINYTEXT\n" +
                        ");").execute();
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS `regions` (\n" +
                        "\t`name` TINYTEXT,\n" +
                        "\t`x1` INT,\n" +
                        "\t`y1` INT,\n" +
                        "\t`z1` INT,\n" +
                        "\t`x2` INT,\n" +
                        "\t`y2` INT,\n" +
                        "\t`z2` INT,\n" +
                        "\t`world` SMALLINT,\n" +
                        "\t`type` TINYTEXT\n" +
                        ");").execute();
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS `launchers` (\n" +
                        "\t`region_name` TINYTEXT,\n" +
                        "\t`x` INT,\n" +
                        "\t`y` INT,\n" +
                        "\t`z` INT\n" +
                        ");").execute();
                return connection;
            }
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }
}
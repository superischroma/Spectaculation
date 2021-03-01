package me.superischroma.spectaculation.listener;

import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerPingListener extends PListener
{
    @EventHandler
    public void onServerPing(ServerListPingEvent e)
    {
        e.setMotd(ChatColor.GREEN + "               Gunga Server  " + ChatColor.RED + "[1.8-1.16]\n                     " + SUtil.getRandomVisibleColor() + ChatColor.BOLD + "SKYBLOCK STUFF");
    }
}

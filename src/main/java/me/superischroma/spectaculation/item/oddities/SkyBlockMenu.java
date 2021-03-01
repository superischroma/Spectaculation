package me.superischroma.spectaculation.item.oddities;

import me.superischroma.spectaculation.gui.GUIType;
import me.superischroma.spectaculation.item.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SkyBlockMenu implements MaterialStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return ChatColor.GREEN + "SkyBlock Menu " + ChatColor.GRAY + "(Right Click)";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.EXCLUSIVE;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ITEM;
    }

    @Override
    public String getLore()
    {
        return "View all of your SkyBlock progress, including your Skills, Collections, Recipes, and more!";
    }

    @Override
    public boolean displayRarity()
    {
        return false;
    }

    @Override
    public void onInteraction(PlayerInteractEvent e)
    {
        GUIType.SKYBLOCK_MENU.getGUI().open(e.getPlayer());
    }

    @Override
    public void onInventoryClick(SItem instance, InventoryClickEvent e)
    {
        e.setCancelled(true);
        GUIType.SKYBLOCK_MENU.getGUI().open((Player) e.getWhoClicked());
    }
}
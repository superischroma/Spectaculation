package me.superischroma.spectaculation.item.accessory;

import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.SpecificItemType;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class CrackedPiggyBank implements AccessoryStatistics, MaterialFunction
{
    @Override
    public String getURL()
    {
        return "14a7aac08593a1a0bc6666fe0aeedfb195d413fc9cf87c73f4a8c04da6418857";
    }

    @Override
    public String getDisplayName()
    {
        return "Cracked Piggy Bank";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.UNCOMMON;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ACCESSORY;
    }

    @Override
    public SpecificItemType getSpecificType()
    {
        return SpecificItemType.ACCESSORY;
    }

    @Override
    public List<String> getListLore()
    {
        return Arrays.asList("Saves " + ChatColor.RED + "75%" + ChatColor.GRAY + " of your coins on death.",
                "Only when in player inventory.", ChatColor.RED + "Very fragile!",
                "", ChatColor.DARK_GRAY + "Triggers when losing 20k+ coins.");
    }
}
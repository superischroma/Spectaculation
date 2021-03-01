package me.superischroma.spectaculation.item.accessory;

import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.SpecificItemType;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class PiggyBank implements AccessoryStatistics, MaterialFunction
{
    @Override
    public String getURL()
    {
        return "198df42f477f213ff5e9d7fa5a4cc4a69f20d9cef2b90c4ae4f29bd17287b5";
    }

    @Override
    public String getDisplayName()
    {
        return "Piggy Bank";
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
        return Arrays.asList("Saves your coins from death.", "Only when in player inventory.", ChatColor.RED + "Fragile!",
                "", ChatColor.DARK_GRAY + "Triggers when losing 20k+ coins.");
    }
}
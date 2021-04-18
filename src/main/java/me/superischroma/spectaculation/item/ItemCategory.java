package me.superischroma.spectaculation.item;

import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public enum ItemCategory
{
    WEAPONS("Weapons", ChatColor.GOLD, (short) 1),
    ARMOR("Armor", ChatColor.AQUA, (short) 11),
    ACCESSORIES("Accessories", ChatColor.DARK_GREEN, (short) 13),
    CONSUMABLES("Consumables", ChatColor.RED, (short) 14),
    BLOCKS("Blocks", ChatColor.YELLOW, (short) 12),
    TOOLS_MISC("Tools & Misc", ChatColor.LIGHT_PURPLE, (short) 10);

    private final String name;
    private final ChatColor text;
    private final short item;

    ItemCategory(String name, ChatColor text, short item)
    {
        this.name = name;
        this.text = text;
        this.item = item;
    }

    public String getName()
    {
        return name;
    }

    public String getColoredName()
    {
        return text + name;
    }

    public ItemStack getStainedGlassPane()
    {
        return SUtil.createColoredStainedGlassPane(item, " ");
    }
}
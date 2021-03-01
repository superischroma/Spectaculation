package me.superischroma.spectaculation.item.rune;

import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.Rune;
import me.superischroma.spectaculation.item.SpecificItemType;
import org.bukkit.ChatColor;

public class CoutureRune implements Rune
{
    @Override
    public String getDisplayName()
    {
        return ChatColor.AQUA + "◆ Couture Rune";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ITEM;
    }

    @Override
    public SpecificItemType getSpecificType()
    {
        return SpecificItemType.COSMETIC;
    }

    @Override
    public String getURL()
    {
        return "734fb3203233efbae82628bd4fca7348cd071e5b7b52407f1d1d2794e31799ff";
    }
}
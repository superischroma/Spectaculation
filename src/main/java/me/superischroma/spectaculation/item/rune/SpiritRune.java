package me.superischroma.spectaculation.item.rune;

import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.Rune;
import me.superischroma.spectaculation.item.SpecificItemType;
import org.bukkit.ChatColor;

public class SpiritRune implements Rune
{
    @Override
    public String getDisplayName()
    {
        return ChatColor.AQUA + "◆ Spirit Rune";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.RARE;
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
        return "c738b8af8d7ce1a26dc6d40180b3589403e11ef36a66d7c4590037732829542e";
    }
}
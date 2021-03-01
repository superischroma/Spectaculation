package me.superischroma.spectaculation.item.rune;

import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.Rune;
import me.superischroma.spectaculation.item.SpecificItemType;
import org.bukkit.ChatColor;

public class BiteRune implements Rune
{
    @Override
    public String getDisplayName()
    {
        return ChatColor.GREEN + "◆ Bite Rune";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.EPIC;
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
        return "43a1ad4fcc42fb63c681328e42d63c83ca193b333af2a426728a25a8cc600692";
    }
}
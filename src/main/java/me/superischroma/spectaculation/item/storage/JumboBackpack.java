package me.superischroma.spectaculation.item.storage;

import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.SkullStatistics;

public class JumboBackpack extends Storage implements SkullStatistics
{
    @Override
    public int getSlots()
    {
        return 54;
    }

    @Override
    public String getDisplayName()
    {
        return "Jumbo Backpack";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.LEGENDARY;
    }

    @Override
    public String getURL()
    {
        return "1f8405116c1daa7ce2f012591458d50246d0a467bcb95a5a2c033aefd6008b63";
    }
}
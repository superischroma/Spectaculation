package me.superischroma.spectaculation.item.storage;

import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.SkullStatistics;

public class MediumBackpack extends Storage implements SkullStatistics
{
    @Override
    public int getSlots()
    {
        return 18;
    }

    @Override
    public String getDisplayName()
    {
        return "Medium Backpack";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.RARE;
    }

    @Override
    public String getURL()
    {
        return "62f3b3a05481cde77240005c0ddcee1c069e5504a62ce0977879f55a39396146";
    }
}
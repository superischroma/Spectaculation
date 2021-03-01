package me.superischroma.spectaculation.item.oddities;

import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.MaterialStatistics;
import me.superischroma.spectaculation.item.Rarity;

public class Bedrock implements MaterialStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Bedrock";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.BLOCK;
    }

    @Override
    public String getLore()
    {
        return "How do you have this??";
    }
}
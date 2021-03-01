package me.superischroma.spectaculation.item.dragon.young;

import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.SkullStatistics;

public class YoungDragonFragment implements SkullStatistics, MaterialFunction
{
    @Override
    public String getURL()
    {
        return "4b5bd6b64e8bd6c58f5cd1e79a5502d4448bafc006d2fe0568f6a0d6b86d449e";
    }

    @Override
    public String getDisplayName()
    {
        return "Young Dragon Fragment";
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
}
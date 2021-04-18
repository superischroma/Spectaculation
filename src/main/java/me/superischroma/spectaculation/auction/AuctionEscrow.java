package me.superischroma.spectaculation.auction;

import me.superischroma.spectaculation.item.SItem;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class AuctionEscrow implements ConfigurationSerializable
{
    private SItem item;
    private long starter;
    private long duration;

    public AuctionEscrow(SItem item, long starter, long duration)
    {
        this.item = item;
        this.starter = starter;
        this.duration = duration;
    }

    public AuctionEscrow()
    {
        this(null, 50L, 21600000L);
    }

    @Override
    public Map<String, Object> serialize()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("item", item);
        map.put("starter", starter);
        map.put("duration", duration);
        return map;
    }

    public static AuctionEscrow deserialize(Map<String, Object> map)
    {
        return new AuctionEscrow((SItem) map.get("item"), map.get("starter") instanceof Long ? (Long) map.get("starter") : ((Integer) map.get("starter")).longValue(),
                map.get("duration") instanceof Long ? (Long) map.get("duration") : ((Integer) map.get("duration")).longValue());
    }

    public SItem getItem()
    {
        return item;
    }

    public void setItem(SItem item)
    {
        this.item = item;
    }

    public long getStarter()
    {
        return starter;
    }

    public void setStarter(long starter)
    {
        this.starter = starter;
    }

    public long getDuration()
    {
        return duration;
    }

    public void setDuration(long duration)
    {
        this.duration = duration;
    }

    public long getCreationFee(boolean bin)
    {
        return Math.round(starter * (bin ? 0.01 : 0.05));
    }
}
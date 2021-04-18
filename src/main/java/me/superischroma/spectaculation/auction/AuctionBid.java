package me.superischroma.spectaculation.auction;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuctionBid implements ConfigurationSerializable
{
    private UUID bidder;
    private long amount;
    private long timestamp;

    public AuctionBid(UUID bidder, long amount, long timestamp)
    {
        this.bidder = bidder;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    @Override
    public Map<String, Object> serialize()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("bidder", bidder.toString());
        map.put("amount", amount);
        map.put("timestamp", timestamp);
        return map;
    }

    public static AuctionBid deserialize(Map<String, Object> map)
    {
        return new AuctionBid(UUID.fromString((String) map.get("bidder")),
                map.get("amount") instanceof Long ? (Long) map.get("amount") : ((Integer) map.get("amount")).longValue(),
                map.get("timestamp") instanceof Long ? (Long) map.get("timestamp") : ((Integer) map.get("timestamp")).longValue());
    }

    public UUID getBidder()
    {
        return bidder;
    }

    public void setBidder(UUID bidder)
    {
        this.bidder = bidder;
    }

    public long getAmount()
    {
        return amount;
    }

    public void setAmount(long amount)
    {
        this.amount = amount;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public long timeSinceBid()
    {
        return System.currentTimeMillis() - timestamp;
    }
}
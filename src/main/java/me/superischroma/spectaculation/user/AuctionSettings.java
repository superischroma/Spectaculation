package me.superischroma.spectaculation.user;

import me.superischroma.spectaculation.item.ItemCategory;
import me.superischroma.spectaculation.item.Rarity;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class AuctionSettings implements Cloneable, ConfigurationSerializable
{
    private ItemCategory category;
    private String query;
    private Sort sort;
    private Rarity tier;
    private Type type;

    public AuctionSettings(ItemCategory category, String query, Sort sort, Rarity tier, Type type)
    {
        this.category = category;
        this.query = query;
        this.sort = sort;
        this.tier = tier;
        this.type = type;
    }

    public AuctionSettings()
    {
        this(ItemCategory.WEAPONS, null, Sort.HIGHEST_BID, null, Type.SHOW_ALL);
    }

    @Override
    public Map<String, Object> serialize()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("category", category.name());
        map.put("query", query);
        map.put("sort", sort.name());
        if (tier != null)
            map.put("tier", tier.name());
        map.put("type", type.name());
        return map;
    }

    @Override
    public String toString()
    {
        return "AuctionSettings{category=" + category.name() +
                ", query=" + query + ", sort=" + sort.name() + ", tier=" + tier.name() + ", type=" + type.name() + "}";
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof AuctionSettings)) return false;
        AuctionSettings settings = (AuctionSettings) o;
        return category == settings.category && query.equals(settings.query) && sort == settings.sort &&
                tier == settings.tier && type == settings.type;
    }

    @Override
    public AuctionSettings clone()
    {
        return new AuctionSettings(category, query, sort, tier, type);
    }

    public ItemCategory getCategory()
    {
        return category;
    }

    public void setCategory(ItemCategory category)
    {
        this.category = category;
    }

    public String getQuery()
    {
        return query;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public Sort getSort()
    {
        return sort;
    }

    public void setSort(Sort sort)
    {
        this.sort = sort;
    }

    public Rarity getTier()
    {
        return tier;
    }

    public void setTier(Rarity tier)
    {
        this.tier = tier;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public static AuctionSettings deserialize(Map<String, Object> map)
    {
        return new AuctionSettings(ItemCategory.valueOf((String) map.get("category")), map.containsKey("query") ? (String) map.get("query") : null,
                Sort.valueOf((String) map.get("sort")), map.containsKey("tier") ? Rarity.getRarity((String) map.get("tier")) : null, Type.valueOf((String) map.get("type")));
    }

    public enum Sort
    {
        HIGHEST_BID("Highest Bid"),
        LOWEST_BID("Lowest Bid"),
        ENDING_SOON("Ending soon"),
        MOST_BIDS("Most Bids");

        private final String display;

        Sort(String display)
        {
            this.display = display;
        }

        public String getDisplay()
        {
            return display;
        }

        public Sort previous()
        {
            int prev = ordinal() - 1;
            if (prev < 0)
                return values()[values().length - 1];
            return values()[prev];
        }

        public Sort next()
        {
            int nex = ordinal() + 1;
            if (nex > values().length - 1)
                return values()[0];
            return values()[nex];
        }
    }

    public enum Type
    {
        SHOW_ALL("Show All"),
        BIN_ONLY("BIN Only"),
        AUCTIONS_ONLY("Auctions Only");

        private final String display;

        Type(String display)
        {
            this.display = display;
        }

        public String getDisplay()
        {
            return display;
        }

        public Type previous()
        {
            int prev = ordinal() - 1;
            if (prev < 0)
                return values()[values().length - 1];
            return values()[prev];
        }

        public Type next()
        {
            int nex = ordinal() + 1;
            if (nex > values().length - 1)
                return values()[0];
            return values()[nex];
        }
    }
}
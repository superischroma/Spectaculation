package me.superischroma.spectaculation.collection;

import lombok.Getter;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
public class ItemCollection
{
    private static final Map<String, ItemCollection> COLLECTION_MAP = new HashMap<>();

    public static final ItemCollection WHEAT = new ItemCollection("Wheat",
            ItemCollectionCategory.FARMING,
            SMaterial.WHEAT,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(15000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000),
            new ItemCollectionRewards(100000));

    public static final ItemCollection OAK_WOOD = new ItemCollection("Oak Wood",
            ItemCollectionCategory.FORAGING,
            SMaterial.OAK_WOOD,
            (short) 0,
            9);

    public static final ItemCollection STRING = new ItemCollection("String",
            ItemCollectionCategory.COMBAT,
            SMaterial.STRING,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250, new ItemCollectionUpgradeReward("Quiver", ChatColor.GREEN)),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000));

    private final String name;
    private final String identifier;
    private final ItemCollectionCategory category;
    private final SMaterial material;
    private final short data;
    private final List<ItemCollectionRewards> rewards;
    private final boolean temporary; // TODO: remove when rewards are added

    private ItemCollection(String name, ItemCollectionCategory category, SMaterial material, short data, ItemCollectionRewards... rewards)
    {
        this.name = name;
        this.identifier = name.toLowerCase().replaceAll(" ", "_");
        this.category = category;
        this.material = material;
        this.data = data;
        this.rewards = new ArrayList<>(Arrays.asList(rewards));
        this.temporary = false;
        COLLECTION_MAP.put(identifier, this);
    }

    // more of a temporary constructor until all items are added
    private ItemCollection(String name, ItemCollectionCategory category, SMaterial material, short data, int size)
    {
        this.name = name;
        this.identifier = name.toLowerCase().replaceAll(" ", "_");
        this.category = category;
        this.material = material;
        this.data = data;
        this.rewards = new ArrayList<>();
        for (int i = 0; i < size; i++)
            rewards.add(null);
        this.temporary = true;
        COLLECTION_MAP.put(identifier, this);
    }

    public static String[] getProgress(Player player, ItemCollectionCategory category)
    {
        User user = User.getUser(player.getUniqueId());
        AtomicInteger found = new AtomicInteger(), completed = new AtomicInteger();
        Collection<ItemCollection> collections = category != null ? getCategoryCollections(category) : getCollections();
        for (ItemCollection collection : collections)
        {
            if (user.getCollection(collection) > 0)
                found.incrementAndGet();
            if (user.getCollection(collection) >= collection.getMaxAmount())
                completed.incrementAndGet();
        }
        String title;
        String progress;
        if (collections.size() == found.get())
        {
            title = SUtil.createProgressText("Collection Maxed Out", completed.get(), collections.size());
            progress = SUtil.createLineProgressBar(20, ChatColor.DARK_GREEN, completed.get(), collections.size());
        }
        else
        {
            title = SUtil.createProgressText("Collection Unlocked", found.get(), collections.size());
            progress = SUtil.createLineProgressBar(20, ChatColor.DARK_GREEN, found.get(), collections.size());
        }
        return new String[]{title, progress};
    }

    public int getMaxAmount()
    {
        if (rewards.size() == 0 || rewards.get(rewards.size() - 1) == null)
            return 0;
        return rewards.get(rewards.size() - 1).getRequirement();
    }

    public int getTierAmount()
    {
        return rewards.size();
    }

    public int getTier(int amount)
    {
        int tier = 0;
        for (ItemCollectionRewards reward : rewards)
        {
            if (reward == null) continue;
            if (reward.getRequirement() > amount)
                break;
            tier++;
        }
        return tier;
    }

    public int getRequirementForTier(int tier)
    {
        tier -= 1;
        if (tier < 0 || tier > rewards.size() - 1)
            return -1;
        ItemCollectionRewards reward = rewards.get(tier);
        if (reward == null)
            return -1;
        return reward.getRequirement();
    }

    public ItemCollectionRewards getRewardsFor(int tier)
    {
        tier -= 1;
        if (tier < 0 || tier > rewards.size())
            return null;
        return rewards.get(tier);
    }

    public static ItemCollection getByIdentifier(String identifier)
    {
        return COLLECTION_MAP.get(identifier.toLowerCase());
    }

    public static ItemCollection getByMaterial(SMaterial material, short data)
    {
        for (ItemCollection collection : COLLECTION_MAP.values())
        {
            if (collection.material == material && collection.data == data)
                return collection;
        }
        return null;
    }

    public static Map<ItemCollection, Integer> getDefaultCollections()
    {
        Map<ItemCollection, Integer> collections = new HashMap<>();
        for (ItemCollection collection : COLLECTION_MAP.values())
            collections.put(collection, 0);
        return collections;
    }

    public static Collection<ItemCollection> getCollections()
    {
        return COLLECTION_MAP.values();
    }

    public static List<ItemCollection> getCategoryCollections(ItemCollectionCategory category)
    {
        return getCollections().stream().filter(collection -> collection.category == category).collect(Collectors.toList());
    }
}
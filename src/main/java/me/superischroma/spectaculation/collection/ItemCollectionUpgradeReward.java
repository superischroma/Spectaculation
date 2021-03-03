package me.superischroma.spectaculation.collection;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ItemCollectionUpgradeReward extends ItemCollectionReward
{
    private final String name;
    private final ChatColor color;

    public ItemCollectionUpgradeReward(String name, ChatColor color)
    {
        super(Type.UPGRADE);
        this.name = name;
        this.color = color;
    }

    public String toRewardString()
    {
        return color + name + " Upgrade";
    }

    @Override
    public void onAchieve(Player player) {} // no immediate rewards
}
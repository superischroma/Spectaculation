package me.superischroma.spectaculation.collection;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemCollectionRewards extends ArrayList<ItemCollectionReward>
{
    @Getter
    private final int requirement;

    public ItemCollectionRewards(int requirement, ItemCollectionReward... rewards)
    {
        super(Arrays.asList(rewards));
        this.requirement = requirement;
    }
}
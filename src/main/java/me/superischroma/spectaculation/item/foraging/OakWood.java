package me.superischroma.spectaculation.item.foraging;

import me.superischroma.spectaculation.item.ExperienceRewardStatistics;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.skill.ForagingSkill;
import me.superischroma.spectaculation.skill.Skill;

public class OakWood implements ExperienceRewardStatistics, MaterialFunction
{
    @Override
    public double getRewardXP()
    {
        return 6.0;
    }

    @Override
    public Skill getRewardedSkill()
    {
        return ForagingSkill.INSTANCE;
    }

    @Override
    public String getDisplayName()
    {
        return "Oak Wood";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.COMMON;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ITEM;
    }
}
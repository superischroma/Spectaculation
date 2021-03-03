package me.superischroma.spectaculation.item.farming;

import me.superischroma.spectaculation.item.ExperienceRewardStatistics;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.skill.FarmingSkill;
import me.superischroma.spectaculation.skill.Skill;

public class Cactus implements ExperienceRewardStatistics, MaterialFunction
{
    @Override
    public double getRewardXP()
    {
        return 2.0;
    }

    @Override
    public Skill getRewardedSkill()
    {
        return FarmingSkill.INSTANCE;
    }

    @Override
    public String getDisplayName()
    {
        return "Cactus";
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
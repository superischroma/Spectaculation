package me.superischroma.spectaculation.item.mining;

import me.superischroma.spectaculation.item.ExperienceRewardStatistics;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.skill.MiningSkill;
import me.superischroma.spectaculation.skill.Skill;

public class Netherrack implements ExperienceRewardStatistics, MaterialFunction
{
    @Override
    public double getRewardXP()
    {
        return 0.5;
    }

    @Override
    public Skill getRewardedSkill()
    {
        return MiningSkill.INSTANCE;
    }

    @Override
    public String getDisplayName()
    {
        return "Netherrack";
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
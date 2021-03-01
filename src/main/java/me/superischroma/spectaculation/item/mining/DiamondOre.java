package me.superischroma.spectaculation.item.mining;

import me.superischroma.spectaculation.item.ExperienceRewardStatistics;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.skill.MiningSkill;
import me.superischroma.spectaculation.skill.Skill;

public class DiamondOre implements ExperienceRewardStatistics, MaterialFunction
{
    @Override
    public double getRewardXP()
    {
        return 10.0;
    }

    @Override
    public Skill getRewardedSkill()
    {
        return MiningSkill.INSTANCE;
    }

    @Override
    public String getDisplayName()
    {
        return "Diamond Ore";
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
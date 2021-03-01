package me.superischroma.spectaculation.item.mining;

import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.skill.MiningSkill;
import me.superischroma.spectaculation.skill.Skill;

public class Stone implements ExperienceRewardStatistics, MaterialFunction
{
    @Override
    public double getRewardXP()
    {
        return 1.0;
    }

    @Override
    public Skill getRewardedSkill()
    {
        return MiningSkill.INSTANCE;
    }

    @Override
    public String getDisplayName()
    {
        return "Stone";
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
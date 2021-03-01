package me.superischroma.spectaculation.item;

import me.superischroma.spectaculation.skill.Skill;

public interface ExperienceRewardStatistics extends MaterialStatistics
{
    double getRewardXP();
    Skill getRewardedSkill();
}

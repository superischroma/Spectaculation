package me.superischroma.spectaculation.skill;

import me.superischroma.spectaculation.user.PlayerStatistic;
import me.superischroma.spectaculation.user.PlayerStatistics;
import me.superischroma.spectaculation.user.PlayerUtils;
import me.superischroma.spectaculation.user.User;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class ForagingSkill extends Skill
{
    public static final ForagingSkill INSTANCE = new ForagingSkill();

    @Override
    public String getName()
    {
        return "Foraging";
    }

    @Override
    public String getAlternativeName()
    {
        return "Logger";
    }

    @Override
    public List<String> getDescription()
    {
        return Arrays.asList("Cut trees and forage for other", "plants to earn Foraging XP!");
    }

    public double getDoubleDropChance(int level)
    {
        return (level * 4.0) / 100.0;
    }

    public double getTripleDropChance(int level)
    {
        return ((level - 25.0) * 4.0) / 100.0;
    }

    public double getStrength(int level)
    {
        return level < 15.0 ? level : level + (level - 14.0);
    }

    @Override
    public List<String> getLevelUpInformation(int level, int lastLevel, boolean showOld)
    {
        String dropChance = (showOld ? ChatColor.DARK_GRAY + "" + lastLevel * 4 + "➜" : "") + ChatColor.GREEN + level * 4;
        if (level > 25)
            dropChance = (showOld ? ChatColor.DARK_GRAY + "" + (lastLevel - 25) * 4 + "➜" : "") + ChatColor.GREEN + (level - 25) * 4;
        return Arrays.asList(ChatColor.WHITE + " Grants " + dropChance + "%" + ChatColor.WHITE + " chance",
                ChatColor.WHITE + " to drop " + (level > 25 ? "3" : "2") + "x logs.", ChatColor.DARK_GRAY + "+" +
                ChatColor.GREEN + (level >= 15 ? "2" : "1") + " " + ChatColor.RED + "❁ Strength");
    }

    @Override
    public boolean hasSixtyLevels()
    {
        return false;
    }

    @Override
    public void onSkillUpdate(User user, double previousXP)
    {
        super.onSkillUpdate(user, previousXP);
        PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(user.getUuid());
        statistics.zeroAll(PlayerStatistic.FORAGING);
        statistics.getStrength().set(PlayerStatistic.FORAGING, getStrength(getLevel(user.getSkillXP(this), hasSixtyLevels())));
    }
}
package me.superischroma.spectaculation.skill;

import me.superischroma.spectaculation.user.PlayerStatistic;
import me.superischroma.spectaculation.user.PlayerStatistics;
import me.superischroma.spectaculation.user.PlayerUtils;
import me.superischroma.spectaculation.user.User;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class FarmingSkill extends Skill
{
    public static final FarmingSkill INSTANCE = new FarmingSkill();

    @Override
    public String getName()
    {
        return "Farming";
    }

    @Override
    public String getAlternativeName()
    {
        return "Farmhand";
    }

    @Override
    public List<String> getDescription()
    {
        return Arrays.asList("Harvest crops and shear sheep to", "earn Farming XP!");
    }

    public double getDoubleDropChance(int level)
    {
        return (level * 4.0) / 100.0;
    }

    public double getHealth(int level)
    {
        int health = level * 2;
        if (level >= 15)
            health += (level - 14);
        if (level >= 20)
            health += (level - 19);
        if (level >= 26)
            health += (level - 25);
        return health;
    }

    @Override
    public List<String> getLevelUpInformation(int level, int lastLevel, boolean showOld)
    {
        String dropChance = (showOld ? ChatColor.DARK_GRAY + "" + lastLevel * 4 + "➜" : "") + ChatColor.GREEN + level * 4;
        int healthPlus = 2;
        if (level >= 15)
            healthPlus = 3;
        if (level >= 20)
            healthPlus = 4;
        if (level >= 26)
            healthPlus = 5;
        return Arrays.asList(ChatColor.WHITE + " Grants " + dropChance + "%" + ChatColor.WHITE + " chance",
                ChatColor.WHITE + " to drop 2x crops.", ChatColor.DARK_GRAY + "+" +
                        ChatColor.GREEN + healthPlus + " " + ChatColor.RED + "❤ Health");
    }

    @Override
    public boolean hasSixtyLevels()
    {
        return true;
    }

    @Override
    public void onSkillUpdate(User user, double previousXP)
    {
        super.onSkillUpdate(user, previousXP);
        PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(user.getUuid());
        statistics.zeroAll(PlayerStatistic.FARMING);
        statistics.getMaxHealth().set(PlayerStatistic.FARMING, getHealth(getLevel(user.getSkillXP(this), hasSixtyLevels())));
    }
}
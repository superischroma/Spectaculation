package me.superischroma.spectaculation.skill;

import me.superischroma.spectaculation.Repeater;
import me.superischroma.spectaculation.item.ExperienceRewardStatistics;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.item.pet.Pet;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Skill
{
    public static final List<Integer> XP_GOALS = Arrays.asList(0, 50, 175, 375, 675, 1175, 1925, 2925, 4425, 6425, 9925, 14925, 22425, 32425, 47425,
            67425, 97425, 147425, 222425, 322425, 522425, 822425, 1222425, 1722425, 2322425, 3022425, 3822425, 4722425,
            5722425, 6822425, 8022425, 9322425, 10722425, 12222425, 13822425, 15522425, 17322425, 19222425, 21222425,
            23322425, 25522425, 27822425, 30222425, 32722425, 35322425, 38072425, 40972425, 44072425, 47472425, 51172425,
            55172425, 59472425, 64072425, 68972425, 74172425, 79672425, 85472425, 91572425, 97972425, 104672425, 111672425);

    public static final List<Integer> COIN_REWARDS = Arrays.asList(0, 25, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1300,
            1400, 1500, 1600, 1700, 1800, 1900, 2000, 2200, 2400, 2600, 2800, 3000, 3500, 4000, 5000, 6000, 7500, 10000, 12500,
            15000, 17500, 20000, 25000, 30000, 35000, 40000, 45000, 50000, 60000, 70000, 80000, 90000, 100000, 125000, 150000,
            175000, 200000, 250000, 300000, 350000, 400000, 450000, 500000, 600000, 700000, 800000, 1000000);

    public static int getLevel(double xp, boolean sixty)
    {
        if (xp == 0.0)
            return 0;
        if (xp >= 55172425.0 && !sixty)
            return 50;
        for (int i = XP_GOALS.size() - 1; i >= 0; i--)
        {
            if (XP_GOALS.get(i) < xp)
                return i;
        }
        return 60;
    }

    public static int getCoinReward(int level)
    {
        return COIN_REWARDS.get(level);
    }

    public static List<Skill> getSkills()
    {
        return Arrays.asList(FarmingSkill.INSTANCE, MiningSkill.INSTANCE, CombatSkill.INSTANCE, ForagingSkill.INSTANCE);
    }

    public static double getNextXPGoal(double xp, boolean sixty)
    {
        if (xp >= 55172425.0 && !sixty)
            return 0.0;
        for (int i = 0; i < XP_GOALS.size(); i++)
        {
            int goal = XP_GOALS.get(i);
            if (goal > xp)
                return goal - SUtil.getOrDefault(XP_GOALS, i - 1, 0);
        }
        return 0.0;
    }

    public static double getNextOverallXPGoal(double xp, boolean sixty)
    {
        if (xp >= 55172425.0 && !sixty)
            return 0.0;
        for (int goal : XP_GOALS)
        {
            if (goal > xp)
                return goal;
        }
        return 0.0;
    }

    public static double getXPUntilLevelUp(double xp, boolean sixty)
    {
        double goal = getNextXPGoal(xp, sixty);
        if (goal == 0.0)
            return Double.POSITIVE_INFINITY;
        return goal - xp;
    }

    public static Repeater.DefenseReplacement getProgressReplacement(Skill skill, double xp, double additive, long end)
    {
        return new Repeater.DefenseReplacement()
        {
            @Override
            public String getReplacement()
            {
                int next = (int) getNextXPGoal(xp, skill.hasSixtyLevels());
                return ChatColor.DARK_AQUA + "+" + SUtil.commaify(additive) + " " + skill.getName() + " (" +
                        SUtil.commaify(next - (getNextOverallXPGoal(xp, skill.hasSixtyLevels()) - xp)) + "/" + SUtil.commaify(next) + ")";
            }

            @Override
            public long getEnd()
            {
                return end;
            }
        };
    }

    public static void reward(Skill rewarded, double rewardXP, Player player)
    {
        User user = User.getUser(player.getUniqueId());
        Pet pet = user.getActivePetClass();
        if (pet != null && pet.getSkill() == rewarded)
        {
            Pet.PetItem item = user.getActivePet();
            int prevLevel = Pet.getLevel(item.getXp(), item.getRarity());
            item.setXp(item.getXp() + rewardXP);
            int level = Pet.getLevel(item.getXp(), item.getRarity());
            if (prevLevel != level)
            {
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 2f);
                player.sendMessage(ChatColor.GREEN + "Your " + item.getRarity().getColor() + item.getType().getDisplayName(item.getType().getData()) +
                        ChatColor.GREEN + " levelled up to level " + ChatColor.BLUE + level + ChatColor.GREEN + "!");
            }
        }
        user.addSkillXP(rewarded, rewardXP);
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1f, 2f);
        if (rewardXP > 0.0)
            Repeater.DEFENSE_REPLACEMENT_MAP.put(player.getUniqueId(), getProgressReplacement(rewarded, user.getSkillXP(rewarded), rewardXP, System.currentTimeMillis() + 6000));
    }

    public abstract String getName();
    public abstract String getAlternativeName();
    public abstract List<String> getDescription();
    public abstract List<String> getLevelUpInformation(int level, int lastLevel, boolean showOld);
    public abstract boolean hasSixtyLevels();
    public void onSkillUpdate(User user, double previousXP)
    {
        int prevLevel = getLevel(previousXP, hasSixtyLevels());
        int level = getLevel(user.getSkillXP(this), hasSixtyLevels());
        if (prevLevel != level)
        {
            Player player = Bukkit.getPlayer(user.getUuid());
            if (player != null)
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
            StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.DARK_AQUA).append(ChatColor.BOLD).append("------------------------------------------\n");
            builder.append(ChatColor.AQUA).append(ChatColor.BOLD).append("  SKILL LEVEL UP ").append(ChatColor.RESET)
                    .append(ChatColor.DARK_AQUA).append(getName()).append(" ");
            if (prevLevel != 0)
                builder.append(ChatColor.DARK_GRAY).append(SUtil.toRomanNumeral(prevLevel)).append("➜");
            builder.append(ChatColor.DARK_AQUA).append(SUtil.toRomanNumeral(level)).append("\n");
            builder.append(" \n");
            builder.append(ChatColor.GREEN).append(ChatColor.BOLD).append("  REWARDS");
            for (String line : getRewardLore(level, prevLevel, true))
                builder.append("\n   ").append(line);
            builder.append(ChatColor.DARK_AQUA).append(ChatColor.BOLD).append("------------------------------------------");
            user.send(builder.toString());
            user.addCoins(getCoinReward(level));
        }
    }
    public List<String> getRewardLore(int level, int prevLevel, boolean showOld)
    {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + getAlternativeName() + " " + SUtil.toRomanNumeral(level) + ChatColor.WHITE);
        lore.addAll(getLevelUpInformation(level, prevLevel, showOld));
        lore.add(ChatColor.DARK_GRAY + "+" + ChatColor.GOLD + SUtil.commaify(getCoinReward(level)) + ChatColor.GRAY + " Coins");
        return lore;
    }
}

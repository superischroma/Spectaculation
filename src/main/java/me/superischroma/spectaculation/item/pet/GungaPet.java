package me.superischroma.spectaculation.item.pet;

import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.RarityValue;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.skill.CombatSkill;
import me.superischroma.spectaculation.skill.Skill;
import me.superischroma.spectaculation.util.SLog;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GungaPet extends Pet
{
    @Override
    public List<PetAbility> getPetAbilities(SItem instance)
    {
        int level = getLevel(instance);
        RarityValue<Double> annihCh = new RarityValue<>(10.0, 10.0, 10.0, 8.0, 6.0, 6.0);
        RarityValue<Integer> gingaCh = new RarityValue<>(50, 40, 30, 20, 10, 10);
        BigDecimal annih = BigDecimal.valueOf(1.0 / (annihCh.getForRarity(instance.getRarity()) - (level * 0.02))).setScale(1, RoundingMode.HALF_EVEN);
        BigDecimal pig = BigDecimal.valueOf(1.0 / (150.0 - level)).setScale(3, RoundingMode.HALF_EVEN);
        List<PetAbility> abilities = new ArrayList<>(Collections.singletonList(new PetAbility()
        {
            @Override
            public String getName()
            {
                return "GUNGA GINGA!";
            }

            @Override
            public List<String> getDescription(SItem instance)
            {
                return Arrays.asList("On damage you will occasionally", "spit straight facts in the chat.");
            }

            @Override
            public void onDamage(EntityDamageByEntityEvent e)
            {
                if (SUtil.random(0, gingaCh.getForRarity(instance.getRarity())) == 0)
                    ((Player) e.getDamager()).chat("GUNGA GINGA!");
            }
        }));
        if (instance.getRarity().isAtLeast(Rarity.RARE))
        {
            abilities.add(new PetAbility()
            {
                @Override
                public String getName()
                {
                    return "Self-Annihilation";
                }

                @Override
                public List<String> getDescription(SItem instance)
                {
                    return Arrays.asList("When hurt, you will have a", ChatColor.GREEN + annih.toPlainString() + "%" + ChatColor.GRAY + " chance to explode.");
                }

                @Override
                public void onHurt(EntityDamageByEntityEvent e, Entity damager)
                {
                    if (SUtil.random(0.0, 100.0) > annih.doubleValue())
                        return;
                    Player player = (Player) e.getEntity();
                    player.setHealth(player.getMaxHealth() * 0.1);
                    player.playSound(player.getLocation(), Sound.EXPLODE, 2f, 1f);
                    player.playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, Effect.EXPLOSION_HUGE.getData());
                    player.setVelocity(player.getVelocity().clone().add(new Vector(0.0, 0.4, 0.0)));
                    player.sendMessage(ChatColor.RED + "Your pet has caused you to blow up!");
                }
            });
        }
        if (instance.getRarity().isAtLeast(Rarity.LEGENDARY))
        {
            abilities.add(new PetAbility()
            {
                @Override
                public String getName()
                {
                    return "Golden Pig";
                }

                @Override
                public List<String> getDescription(SItem instance)
                {
                    return Arrays.asList("Have a " + ChatColor.GREEN + pig.toPlainString() + "%" + ChatColor.GRAY + " chance",
                            "to quadruple your damage.");
                }

                @Override
                public void onDamage(EntityDamageByEntityEvent e)
                {
                    if (SUtil.random(0.0, 100.0) > pig.doubleValue())
                        return;
                    ((Player) e.getDamager()).playSound(e.getDamager().getLocation(), Sound.ZOMBIE_METAL, 1f, 1f);
                    e.setDamage(e.getDamage() * 4.0);
                }
            });
        }
        return abilities;
    }

    @Override
    public Skill getSkill()
    {
        return CombatSkill.INSTANCE;
    }

    @Override
    public String getURL()
    {
        return "c17a7f20579fec251ae86e589699c6a325521196a68ed04578fdaac29c7f7723";
    }

    @Override
    public String getDisplayName()
    {
        return "Gunga";
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.PET;
    }

    @Override
    public double getPerIntelligence()
    {
        return -0.3;
    }

    @Override
    public double getPerMagicFind()
    {
        return 0.04;
    }

    @Override
    public double getPerDefense()
    {
        return 0.5;
    }

    @Override
    public double getPerCritDamage()
    {
        return 0.03;
    }
}
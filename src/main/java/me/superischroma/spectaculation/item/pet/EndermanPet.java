package me.superischroma.spectaculation.item.pet;

import com.google.common.util.concurrent.AtomicDouble;
import me.superischroma.spectaculation.entity.SEntity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.RarityValue;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.skill.CombatSkill;
import me.superischroma.spectaculation.skill.Skill;
import me.superischroma.spectaculation.util.Groups;
import me.superischroma.spectaculation.util.SLog;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EndermanPet extends Pet
{
    @Override
    public List<PetAbility> getPetAbilities(SItem instance)
    {
        RarityValue<Double> enderianMul = new RarityValue<>(0.1, 0.2, 0.2, 0.3, 0.3, 0.3);
        RarityValue<Double> savvyMul = new RarityValue<>(0.0, 0.0, 0.4, 0.5, 0.5, 0.5);
        int level = getLevel(instance);
        BigDecimal enderian = new BigDecimal(level * enderianMul.getForRarity(instance.getRarity())).setScale(1, RoundingMode.HALF_EVEN);
        BigDecimal savvy = new BigDecimal(level * savvyMul.getForRarity(instance.getRarity())).setScale(1, RoundingMode.HALF_EVEN);
        BigDecimal zealot = new BigDecimal(level * 0.25).setScale(2, RoundingMode.HALF_EVEN);
        List<PetAbility> abilities = new ArrayList<>(Collections.singletonList(new PetAbility()
        {
            @Override
            public String getName()
            {
                return "Enderian";
            }

            @Override
            public List<String> getDescription(SItem instance)
            {
                return Arrays.asList("Take " + ChatColor.GREEN + enderian.toPlainString() +
                        "%" + ChatColor.GRAY + " less damage", "from end monsters");
            }

            @Override
            public void onHurt(EntityDamageByEntityEvent e, Entity damager)
            {
                SEntity entity = SEntity.findSEntity(damager);
                if (entity == null) return;
                if (Groups.END_MOBS.contains(entity.getSpecType()))
                    e.setDamage(e.getDamage() - ((e.getDamage() * enderian.doubleValue()) * 0.01));
            }
        }));
        if (instance.getRarity().isAtLeast(Rarity.RARE))
        {
            abilities.add(new PetAbility()
            {
                @Override
                public String getName()
                {
                    return "Teleport Savvy";
                }

                @Override
                public List<String> getDescription(SItem instance)
                {
                    return Arrays.asList("Buffs the Aspect of the End", "ability granting " + ChatColor.GREEN + savvy.toPlainString() + ChatColor.GRAY + " weapon",
                            "damage for 5s on use");
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
                    return "Zealot Madness";
                }

                @Override
                public List<String> getDescription(SItem instance)
                {
                    return Arrays.asList("Increases your odds to find a", "special Zealot by " + ChatColor.GREEN + zealot.toPlainString() + "%");
                }

                @Override
                public void onZealotAttempt(AtomicDouble chance)
                {
                    chance.set(chance.get() - (chance.get() * zealot.doubleValue()));
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
        return "6eab75eaa5c9f2c43a0d23cfdce35f4df632e9815001850377385f7b2f039ce1";
    }

    @Override
    public String getDisplayName()
    {
        return "Enderman";
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.PET;
    }

    @Override
    public double getPerCritDamage()
    {
        return 0.0075;
    }
}
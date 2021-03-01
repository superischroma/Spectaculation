package me.superischroma.spectaculation.potion;

import lombok.Getter;
import me.superischroma.spectaculation.Repeater;
import me.superischroma.spectaculation.user.PlayerStatistics;
import me.superischroma.spectaculation.user.PlayerUtils;
import me.superischroma.spectaculation.util.SUtil;
import me.superischroma.spectaculation.util.TriConsumer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.BiConsumer;

public class PotionEffectType
{
    private static final Map<String, PotionEffectType> POTION_EFFECT_TYPE_CACHE = new HashMap<>();

    public static final PotionEffectType STRENGTH = new PotionEffectType(ChatColor.DARK_RED + "Strength", "strength",
            "Increases Strength by %s.", org.bukkit.potion.PotionEffectType.INCREASE_DAMAGE,
            ((statistics, slot, level) ->
                    statistics.getStrength().add(slot, SUtil.getOrDefault(Arrays.asList(5, 12, 20, 30, 40, 50, 60, 75), level - 1, level * 10))),
            false);

    public static final PotionEffectType RABBIT = new PotionEffectType(ChatColor.GREEN + "Rabbit", "rabbit",
            "Grants Jump Boost %s and +%s Speed.", org.bukkit.potion.PotionEffectType.JUMP,
            (((effect, player) ->
                PlayerUtils.replacePotionEffect(player, new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.JUMP, (int) effect.getDuration(),
                        effect.getLevel() % 2 == 0 ? effect.getLevel() / 2 : (effect.getLevel() + 1) / 2)))),
            ((statistics, slot, level) -> statistics.getSpeed().add(slot, level * 10.0 / 100.0)), false);

    public static final PotionEffectType HEALING = new PotionEffectType(ChatColor.RED + "Healing", "healing",
            "Grants an instant %s Health boost.", org.bukkit.potion.PotionEffectType.HEALTH_BOOST,
            ((effect, player) -> player.setHealth(Math.min(player.getMaxHealth(),
                    player.getHealth() + SUtil.getOrDefault(Arrays.asList(20, 50, 100, 150, 200, 300, 400, 500), effect.getLevel() - 1,
                            (effect.getLevel() - 3) * 100)))), true);

    public static final PotionEffectType JUMP_BOOST = new PotionEffectType(ChatColor.AQUA + "Jump Boost", "jump_boost",
            "Increases your jump height.", org.bukkit.potion.PotionEffectType.JUMP,
            (((effect, player) ->
                    PlayerUtils.replacePotionEffect(player, new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.JUMP, (int) effect.getDuration(),
                            effect.getLevel() - 1)))), false);

    public static final PotionEffectType SPEED = new PotionEffectType(ChatColor.BLUE + "Speed", "speed",
            "Grants a %s Speed boost.", org.bukkit.potion.PotionEffectType.SPEED,
            ((statistics, slot, level) -> statistics.getSpeed().add(slot, (level * 5.0) / 100.0)), false);

    public static final PotionEffectType ARCHERY = new PotionEffectType(ChatColor.AQUA + "Archery", "archery",
            "Increases Bow Damage by %s%.", org.bukkit.potion.PotionEffectType.SPEED, false);

    public static final PotionEffectType MANA = new PotionEffectType(ChatColor.BLUE + "Mana", "mana",
            "Grants an instant %s Mana boost.", org.bukkit.potion.PotionEffectType.HARM,
            ((effect, player) ->
            {
                Repeater.MANA_MAP.put(player.getUniqueId(), Repeater.MANA_MAP.get(player.getUniqueId()) +
                        SUtil.getOrDefault(Arrays.asList(25, 50, 75, 100, 150, 200, 300, 400), effect.getLevel() - 1,
                                (effect.getLevel() - 4) * 100));
            }), true);

    public static final PotionEffectType ADRENALINE = new PotionEffectType(ChatColor.RED + "Adrenaline", "adrenaline",
            "Grants %s Absorption health and an increase of %s Speed.", org.bukkit.potion.PotionEffectType.REGENERATION,
            (effect, player) ->
                    PlayerUtils.replacePotionEffect(player, new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.ABSORPTION, (int) effect.getDuration(),
                            (int) (SUtil.getOrDefault(Arrays.asList(20, 40, 60, 80, 100, 150, 200, 300), effect.getLevel() - 1,
                                    (effect.getLevel() - 5) * 100) * 0.25 - 1.0))),
            (statistics, slot, level) -> statistics.getSpeed().add(slot, (level * 5.0) / 100.0), false);

    public static final PotionEffectType CRITICAL = new PotionEffectType(ChatColor.DARK_RED + "Critical", "critical",
            "Gives a %s% increase in Crit Chance and a %s% increase in Crit Damage.", org.bukkit.potion.PotionEffectType.HARM,
            ((statistics, slot, level) ->
            {
                statistics.getCritChance().add(slot, 0.05 + ((level * 5.0) / 100.0));
                statistics.getCritDamage().add(slot, (level * 10.0) / 100.0);
            }), false);

    public static final PotionEffectType ABSORPTION = new PotionEffectType(ChatColor.GOLD + "Absorption", "absorption",
            "Grants a boost of %s Absorption health.", org.bukkit.potion.PotionEffectType.FIRE_RESISTANCE,
            (effect, player) ->
                    PlayerUtils.replacePotionEffect(player, new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.ABSORPTION, (int) effect.getDuration(),
                            (int) (SUtil.getOrDefault(Arrays.asList(20, 40, 60, 80, 100, 150, 200, 300), effect.getLevel() - 1,
                                    (effect.getLevel() - 5) * 100) * 0.25 - 1.0))), false);

    public static final PotionEffectType HASTE = new PotionEffectType(ChatColor.YELLOW + "Haste", "haste",
            "Increases your mining speed.", org.bukkit.potion.PotionEffectType.FIRE_RESISTANCE,
            (((effect, player) ->
            {
                player.removePotionEffect(org.bukkit.potion.PotionEffectType.FAST_DIGGING);
                player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.FAST_DIGGING, (int) effect.getDuration(),
                        effect.getLevel() - 1));
            })), false);

    public static final PotionEffectType RESISTANCE = new PotionEffectType(ChatColor.GREEN + "Resistance", "resistance",
            "Increases Defense by %s.", org.bukkit.potion.PotionEffectType.JUMP,
            ((statistics, slot, level) -> statistics.getDefense().add(slot, SUtil.getOrDefault(Arrays.asList(5, 10, 15, 20, 30, 40, 50, 60),
                    level - 1, (level * 10) - 20))), false);

    public static final PotionEffectType TRUE_RESISTANCE = new PotionEffectType(ChatColor.WHITE + "True Resistance", "true_resistance",
            "Increases True Defense by %s.", org.bukkit.potion.PotionEffectType.WEAKNESS,
            ((statistics, slot, level) -> statistics.getTrueDefense().add(slot, level * 5)), false);

    public static final PotionEffectType STAMINA = new PotionEffectType(ChatColor.GREEN + "Stamina", "stamina",
            "Grants an instant %s Health and %s Mana boost.", org.bukkit.potion.PotionEffectType.JUMP,
            ((effect, player) ->
            {
                player.setHealth(Math.min(player.getMaxHealth(),
                        player.getHealth() + SUtil.getOrDefault(Arrays.asList(150, 215, 315, 515), effect.getLevel() - 1,
                                ((effect.getLevel() + 1) * 100) + 15)));
                Repeater.MANA_MAP.put(player.getUniqueId(), Repeater.MANA_MAP.get(player.getUniqueId()) + (50 * effect.getLevel()));
            }), true);

    public static final PotionEffectType SPIRIT = new PotionEffectType(ChatColor.AQUA + "Spirit", "spirit",
            "Grants a %s increase in Speed and a %s% increase in Crit Damage.", org.bukkit.potion.PotionEffectType.SPEED,
            ((statistics, slot, level) ->
            {
                statistics.getSpeed().add(slot, (level * 10.0) / 100.0);
                statistics.getCritDamage().add(slot, (level * 10.0) / 100.0);
            }), false);

    @Getter
    private final String name;
    @Getter
    private final String namespace;
    private final String description;
    @Getter
    private final org.bukkit.potion.PotionEffectType color;
    @Getter
    private final BiConsumer<PotionEffect, Player> onDrink;
    @Getter
    private final TriConsumer<PlayerStatistics, Integer, Integer> statsUpdate;
    @Getter
    private final boolean instant;

    public PotionEffectType(String name, String namespace, String description, org.bukkit.potion.PotionEffectType color,
                            BiConsumer<PotionEffect, Player> onDrink, TriConsumer<PlayerStatistics, Integer, Integer> statsUpdate, boolean instant)
    {
        this.name = name;
        this.namespace = namespace;
        this.description = description;
        this.color = color;
        this.onDrink = onDrink;
        this.statsUpdate = statsUpdate;
        this.instant = instant;
        POTION_EFFECT_TYPE_CACHE.put(namespace, this);
    }

    public PotionEffectType(String name, String namespace, String description, org.bukkit.potion.PotionEffectType color,
                            TriConsumer<PlayerStatistics, Integer, Integer> statsUpdate, boolean instant)
    {
        this(name, namespace, description, color, null, statsUpdate, instant);
    }

    public PotionEffectType(String name, String namespace, String description, org.bukkit.potion.PotionEffectType color,
                            BiConsumer<PotionEffect, Player> onDrink, boolean instant)
    {
        this(name, namespace, description, color, onDrink, null, instant);
    }

    public PotionEffectType(String name, String namespace, String description, org.bukkit.potion.PotionEffectType color, boolean instant)
    {
        this(name, namespace, description, color, null, null, instant);
    }

    public static PotionEffectType getByNamespace(String namespace)
    {
        return POTION_EFFECT_TYPE_CACHE.get(namespace.toLowerCase());
    }

    public String getDescription(Object... objects)
    {
        String description = this.description;
        for (Object object : objects)
            description = description.replaceFirst("%s", String.valueOf(object));
        return description;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof PotionEffectType)) return false;
        return ((PotionEffectType) o).namespace.equals(namespace);
    }
}
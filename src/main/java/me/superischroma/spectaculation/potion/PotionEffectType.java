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
            "Increases Strength by %s.", PotionColor.BLOOD_RED,
            ((statistics, slot, level) ->
                    statistics.getStrength().add(slot, SUtil.getOrDefault(Arrays.asList(5.0, 12.0, 20.0, 30.0, 40.0, 50.0, 60.0, 75.0), level - 1, level * 10.0))),
            false, true);

    public static final PotionEffectType RABBIT = new PotionEffectType(ChatColor.GREEN + "Rabbit", "rabbit",
            "Grants Jump Boost %s and +%s Speed.", PotionColor.DARK_GREEN,
            (((effect, player) ->
                PlayerUtils.replacePotionEffect(player, new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.JUMP, (int) effect.getDuration(),
                        effect.getLevel() % 2 == 0 ? effect.getLevel() / 2 : (effect.getLevel() + 1) / 2)))),
            ((statistics, slot, level) -> statistics.getSpeed().add(slot, level * 10.0 / 100.0)), false, true);

    public static final PotionEffectType HEALING = new PotionEffectType(ChatColor.RED + "Healing", "healing",
            "Grants an instant %s Health boost.", PotionColor.RED,
            ((effect, player) -> player.setHealth(Math.min(player.getMaxHealth(),
                    player.getHealth() + SUtil.getOrDefault(Arrays.asList(20, 50, 100, 150, 200, 300, 400, 500), effect.getLevel() - 1,
                            (effect.getLevel() - 3) * 100)))), true, true);

    public static final PotionEffectType JUMP_BOOST = new PotionEffectType(ChatColor.AQUA + "Jump Boost", "jump_boost",
            "Increases your jump height.", PotionColor.TWILIGHT_BLUE,
            (((effect, player) ->
                    PlayerUtils.replacePotionEffect(player, new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.JUMP, (int) effect.getDuration(),
                            effect.getLevel() - 1)))), false, true);

    public static final PotionEffectType SPEED = new PotionEffectType(ChatColor.BLUE + "Speed", "speed",
            "Grants a %s Speed boost.", PotionColor.LIGHT_BLUE,
            ((statistics, slot, level) -> statistics.getSpeed().add(slot, (level * 5.0) / 100.0)), false, true);

    public static final PotionEffectType ARCHERY = new PotionEffectType(ChatColor.AQUA + "Archery", "archery",
            "Increases Bow Damage by %s%.", PotionColor.LIGHT_BLUE, false, true);

    public static final PotionEffectType MANA = new PotionEffectType(ChatColor.BLUE + "Mana", "mana",
            "Grants an instant %s Mana boost.", PotionColor.DARK_BLUE,
            ((effect, player) ->
            {
                Repeater.MANA_MAP.put(player.getUniqueId(), Repeater.MANA_MAP.get(player.getUniqueId()) +
                        SUtil.getOrDefault(Arrays.asList(25, 50, 75, 100, 150, 200, 300, 400), effect.getLevel() - 1,
                                (effect.getLevel() - 4) * 100));
            }), true, true);

    public static final PotionEffectType ADRENALINE = new PotionEffectType(ChatColor.RED + "Adrenaline", "adrenaline",
            "Grants %s Absorption health and an increase of %s Speed.", PotionColor.PURPLE,
            (effect, player) ->
                    PlayerUtils.replacePotionEffect(player, new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.ABSORPTION, (int) effect.getDuration(),
                            (int) (SUtil.getOrDefault(Arrays.asList(20, 40, 60, 80, 100, 150, 200, 300), effect.getLevel() - 1,
                                    (effect.getLevel() - 5) * 100) * 0.25 - 1.0))),
            (statistics, slot, level) -> statistics.getSpeed().add(slot, (level * 5.0) / 100.0), false, true);

    public static final PotionEffectType CRITICAL = new PotionEffectType(ChatColor.DARK_RED + "Critical", "critical",
            "Gives a %s% increase in Crit Chance and a %s% increase in Crit Damage.", PotionColor.DARK_RED,
            ((statistics, slot, level) ->
            {
                statistics.getCritChance().add(slot, 0.05 + ((level * 5.0) / 100.0));
                statistics.getCritDamage().add(slot, (level * 10.0) / 100.0);
            }), false, true);

    public static final PotionEffectType ABSORPTION = new PotionEffectType(ChatColor.GOLD + "Absorption", "absorption",
            "Grants a boost of %s Absorption health.", PotionColor.ORANGE,
            (effect, player) ->
                    PlayerUtils.replacePotionEffect(player, new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.ABSORPTION, (int) effect.getDuration(),
                            (int) (SUtil.getOrDefault(Arrays.asList(20, 40, 60, 80, 100, 150, 200, 300), effect.getLevel() - 1,
                                    (effect.getLevel() - 5) * 100) * 0.25 - 1.0))), false, true);

    public static final PotionEffectType HASTE = new PotionEffectType(ChatColor.YELLOW + "Haste", "haste",
            "Increases your mining speed.", PotionColor.ORANGE,
            (((effect, player) ->
            {
                player.removePotionEffect(org.bukkit.potion.PotionEffectType.FAST_DIGGING);
                player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.FAST_DIGGING, (int) effect.getDuration(),
                        effect.getLevel() - 1));
            })), false, true);

    public static final PotionEffectType RESISTANCE = new PotionEffectType(ChatColor.GREEN + "Resistance", "resistance",
            "Increases Defense by %s.", PotionColor.DARK_GREEN,
            ((statistics, slot, level) -> statistics.getDefense().add(slot, SUtil.getOrDefault(Arrays.asList(5.0, 10.0, 15.0, 20.0, 30.0, 40.0, 50.0, 60.0),
                    level - 1, (level * 10.0) - 20.0))), false, true);

    public static final PotionEffectType TRUE_RESISTANCE = new PotionEffectType(ChatColor.WHITE + "True Resistance", "true_resistance",
            "Increases True Defense by %s.", PotionColor.TWILIGHT_BLUE,
            ((statistics, slot, level) -> statistics.getTrueDefense().add(slot, level * 5.0)), false, true);

    public static final PotionEffectType STAMINA = new PotionEffectType(ChatColor.GREEN + "Stamina", "stamina",
            "Grants an instant %s Health and %s Mana boost.", PotionColor.TWILIGHT_BLUE,
            ((effect, player) ->
            {
                player.setHealth(Math.min(player.getMaxHealth(),
                        player.getHealth() + SUtil.getOrDefault(Arrays.asList(150, 215, 315, 515), effect.getLevel() - 1,
                                ((effect.getLevel() + 1) * 100) + 15)));
                Repeater.MANA_MAP.put(player.getUniqueId(), Repeater.MANA_MAP.get(player.getUniqueId()) + (50 * effect.getLevel()));
            }), true, true);

    public static final PotionEffectType SPIRIT = new PotionEffectType(ChatColor.AQUA + "Spirit", "spirit",
            "Grants a %s increase in Speed and a %s% increase in Crit Damage.", PotionColor.LIGHT_BLUE,
            ((statistics, slot, level) ->
            {
                statistics.getSpeed().add(slot, (level * 10.0) / 100.0);
                statistics.getCritDamage().add(slot, (level * 10.0) / 100.0);
            }), false, true);

    @Getter
    private final String name;
    @Getter
    private final String namespace;
    private final String description;
    @Getter
    private final PotionColor color;
    @Getter
    private final BiConsumer<PotionEffect, Player> onDrink;
    @Getter
    private final TriConsumer<PlayerStatistics, Integer, Integer> statsUpdate;
    @Getter
    private final boolean instant;
    @Getter
    private final boolean buff;

    public PotionEffectType(String name, String namespace, String description, PotionColor color,
                            BiConsumer<PotionEffect, Player> onDrink, TriConsumer<PlayerStatistics, Integer, Integer> statsUpdate, boolean instant, boolean buff)
    {
        this.name = name;
        this.namespace = namespace;
        this.description = description;
        this.color = color;
        this.onDrink = onDrink;
        this.statsUpdate = statsUpdate;
        this.instant = instant;
        this.buff = buff;
        POTION_EFFECT_TYPE_CACHE.put(namespace, this);
    }

    public PotionEffectType(String name, String namespace, String description, PotionColor color,
                            TriConsumer<PlayerStatistics, Integer, Integer> statsUpdate, boolean instant, boolean buff)
    {
        this(name, namespace, description, color, null, statsUpdate, instant, buff);
    }

    public PotionEffectType(String name, String namespace, String description, PotionColor color,
                            BiConsumer<PotionEffect, Player> onDrink, boolean instant, boolean buff)
    {
        this(name, namespace, description, color, onDrink, null, instant, buff);
    }

    public PotionEffectType(String name, String namespace, String description, PotionColor color, boolean instant, boolean buff)
    {
        this(name, namespace, description, color, null, null, instant, buff);
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
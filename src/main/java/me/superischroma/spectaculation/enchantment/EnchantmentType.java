package me.superischroma.spectaculation.enchantment;

import lombok.Getter;
import me.superischroma.spectaculation.item.SpecificItemType;
import org.bukkit.enchantments.Enchantment;

import java.util.*;

public class EnchantmentType
{
    private static final Map<String, EnchantmentType> ENCHANTMENT_TYPE_CACHE = new HashMap<>();

    public static final EnchantmentType SHARPNESS = new EnchantmentType("Sharpness", "sharpness",
            "Increases damage by %s%.", SpecificItemType.SWORD, SpecificItemType.AXE);
    public static final EnchantmentType FIRE_ASPECT = new EnchantmentType("Fire Aspect", "fire_aspect",
            "Gives whoever this weapon hits %s seconds of fire.", SpecificItemType.SWORD, SpecificItemType.AXE);
    public static final EnchantmentType PROTECTION = new EnchantmentType("Protection", "protection",
            "Defends against damage!", SpecificItemType.HELMET, SpecificItemType.CHESTPLATE, SpecificItemType.LEGGINGS,
            SpecificItemType.BOOTS);
    public static final EnchantmentType GROWTH = new EnchantmentType("Growth", "growth",
            "Applies %s Health.", SpecificItemType.HELMET, SpecificItemType.CHESTPLATE, SpecificItemType.LEGGINGS,
            SpecificItemType.BOOTS);
    public static final EnchantmentType AIMING = new EnchantmentType("Aiming", "aiming",
            "Arrows home towards nearby mobs if they are within %s blocks.", SpecificItemType.BOW);
    public static final EnchantmentType POWER = new EnchantmentType("Power", "power",
            "Increases bow damage by %s%.", SpecificItemType.BOW);
    public static final EnchantmentType FLAME = new EnchantmentType("Flame", "flame",
            "Arrow ignites target for 3 seconds, dealing 5 damage every second.", SpecificItemType.BOW);
    public static final EnchantmentType SUPERS_BLESSING  = new EnchantmentType("Super's Blessing", "supers_blessing",
            "Blesses this item with incredible powers!");
    public static final EnchantmentType ULTIMATE_WISE  = new EnchantmentType("Ultimate Wise", "ultimate_wise",
            "Reduces the Mana Cost of this item's ability by %s%.", true, SpecificItemType.SWORD, SpecificItemType.SHOVEL,
            SpecificItemType.SHEARS, SpecificItemType.PICKAXE, SpecificItemType.BOW, SpecificItemType.AXE, SpecificItemType.ROD,
            SpecificItemType.HOE, SpecificItemType.HELMET, SpecificItemType.CHESTPLATE, SpecificItemType.LEGGINGS, SpecificItemType.BOOTS);
    public static final EnchantmentType EFFICIENCY = new EnchantmentType("Efficiency", "efficiency",
            "Reduces the time in takes to mine.", Enchantment.DIG_SPEED, SpecificItemType.AXE, SpecificItemType.PICKAXE,
            SpecificItemType.SHOVEL);
    public static final EnchantmentType SMITE = new EnchantmentType("Smite", "smite",
            "Increases damage dealt to Zombies, Zombie Pigmen, Withers, Wither Skeletons, and Skeletons by %s% per level.",
            SpecificItemType.SWORD, SpecificItemType.AXE);
    public static final EnchantmentType BANE_OF_ARTHROPODS = new EnchantmentType("Bane of Arthropods", "bane_of_arthropods",
            "Increases damage dealt to Cave Spiders, Spiders, and Silverfish by %s% per level.",
            SpecificItemType.SWORD, SpecificItemType.AXE);
    public static final EnchantmentType CRITICAL = new EnchantmentType("Critical", "critical",
            "Increases critical damage by %s%.", SpecificItemType.SWORD, SpecificItemType.AXE);
    public static final EnchantmentType HARVESTING = new EnchantmentType("Harvesting", "harvesting",
            "Increases the chance for crops to drop double the amount of items by %s%.",
            SpecificItemType.HOE);

    @Getter
    private final String name;
    @Getter
    private final String namespace;
    private final String description;
    @Getter
    private final boolean ultimate;
    @Getter
    private final Enchantment vanilla;
    @Getter
    private final List<SpecificItemType> compatibleTypes;

    public EnchantmentType(String name, String namespace, String description, boolean ultimate, Enchantment vanilla, SpecificItemType... compatibleTypes)
    {
        this.name = name;
        this.namespace = namespace;
        this.description = description;
        this.ultimate = ultimate;
        this.vanilla = vanilla;
        this.compatibleTypes = new ArrayList<>(Arrays.asList(compatibleTypes));
        ENCHANTMENT_TYPE_CACHE.put(namespace, this);
    }

    public EnchantmentType(String name, String namespace, String description, boolean ultimate, SpecificItemType... compatibleTypes)
    {
        this(name, namespace, description, ultimate, null, compatibleTypes);
    }

    public EnchantmentType(String name, String namespace, String description, Enchantment vanilla, SpecificItemType... compatibleTypes)
    {
        this(name, namespace, description, false, vanilla, compatibleTypes);
    }

    public EnchantmentType(String name, String namespace, String description, SpecificItemType... compatibleTypes)
    {
        this(name, namespace, description, false, compatibleTypes);
    }

    public static EnchantmentType getByNamespace(String namespace)
    {
        return ENCHANTMENT_TYPE_CACHE.get(namespace.toLowerCase());
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
        if (!(o instanceof EnchantmentType)) return false;
        return ((EnchantmentType) o).namespace.equals(namespace);
    }
}
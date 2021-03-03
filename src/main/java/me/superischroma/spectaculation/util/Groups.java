package me.superischroma.spectaculation.util;

import me.superischroma.spectaculation.region.RegionType;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.inventory.InventoryAction;

import java.util.Arrays;
import java.util.List;

public final class Groups
{
    public static final List<Material> SWORDS = Arrays.asList(
            Material.WOOD_SWORD,
            Material.STONE_SWORD,
            Material.IRON_SWORD,
            Material.GOLD_SWORD,
            Material.DIAMOND_SWORD
    );

    public static final List<Material> PICKAXES = Arrays.asList(
            Material.WOOD_PICKAXE,
            Material.STONE_PICKAXE,
            Material.IRON_PICKAXE,
            Material.GOLD_PICKAXE,
            Material.DIAMOND_PICKAXE
    );

    public static final List<Material> AXES = Arrays.asList(
            Material.WOOD_AXE,
            Material.STONE_AXE,
            Material.IRON_AXE,
            Material.GOLD_AXE,
            Material.DIAMOND_AXE
    );

    public static final List<Material> SHOVELS = Arrays.asList(
            Material.WOOD_SPADE,
            Material.STONE_SPADE,
            Material.IRON_SPADE,
            Material.GOLD_SPADE,
            Material.DIAMOND_SPADE
    );

    public static final List<Material> HOES = Arrays.asList(
            Material.WOOD_HOE,
            Material.STONE_HOE,
            Material.IRON_HOE,
            Material.GOLD_HOE,
            Material.DIAMOND_HOE
    );

    public static final List<Material> LEATHER_ARMOR = Arrays.asList(
            Material.LEATHER_HELMET,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS,
            Material.LEATHER_BOOTS
    );

    public static final List<Material> IRON_ARMOR = Arrays.asList(
            Material.IRON_HELMET,
            Material.IRON_CHESTPLATE,
            Material.IRON_LEGGINGS,
            Material.IRON_BOOTS
    );

    public static final List<Material> GOLD_ARMOR = Arrays.asList(
            Material.GOLD_HELMET,
            Material.GOLD_CHESTPLATE,
            Material.GOLD_LEGGINGS,
            Material.GOLD_BOOTS
    );

    public static final List<Material> DIAMOND_ARMOR = Arrays.asList(
            Material.DIAMOND_HELMET,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_LEGGINGS,
            Material.DIAMOND_BOOTS
    );

    public static final List<InventoryAction> PICKUP_FROM_INVENTORY = Arrays.asList(
            InventoryAction.PICKUP_ALL,
            InventoryAction.PICKUP_HALF,
            InventoryAction.PICKUP_SOME,
            InventoryAction.MOVE_TO_OTHER_INVENTORY,
            InventoryAction.HOTBAR_SWAP,
            InventoryAction.SWAP_WITH_CURSOR,
            InventoryAction.COLLECT_TO_CURSOR
    );

    public static final List<RegionType> MINING_REGIONS = Arrays.asList(
            RegionType.COAL_MINE,
            RegionType.GOLD_MINE,
            RegionType.DEEP_CAVERN,
            RegionType.GUNPOWDER_MINES,
            RegionType.LAPIS_QUARRY,
            RegionType.PIGMENS_DEN,
            RegionType.SLIMEHILL,
            RegionType.DIAMOND_RESERVE,
            RegionType.OBSIDIAN_SANCTUARY,
            RegionType.THE_END,
            RegionType.DRAGONS_NEST,
            RegionType.BLAZING_FORTRESS
    );

    public static final List<RegionType> DEEP_CAVERNS_REGIONS = Arrays.asList(
            RegionType.DEEP_CAVERN,
            RegionType.GUNPOWDER_MINES,
            RegionType.LAPIS_QUARRY,
            RegionType.PIGMENS_DEN,
            RegionType.SLIMEHILL,
            RegionType.DIAMOND_RESERVE,
            RegionType.OBSIDIAN_SANCTUARY
    );

    public static final List<EntityType> UNDEAD_MOBS = Arrays.asList(
            EntityType.ZOMBIE,
            EntityType.PIG_ZOMBIE,
            EntityType.WITHER,
            EntityType.WITHER_SKULL,
            EntityType.SKELETON
    );

    public static final List<EntityType> ARTHROPODS = Arrays.asList(
            EntityType.CAVE_SPIDER,
            EntityType.SPIDER,
            EntityType.SILVERFISH
    );

    public static final List<RegionType> FORAGING_REGIONS = Arrays.asList(
            RegionType.FOREST,
            RegionType.BIRCH_PARK,
            RegionType.SPRUCE_WOODS,
            RegionType.DARK_THICKET,
            RegionType.SAVANNA_WOODLAND,
            RegionType.JUNGLE_ISLAND
    );

    public static final List<RegionType> FARMING_REGIONS = Arrays.asList(
            RegionType.THE_BARN,
            RegionType.FARM,
            RegionType.MUSHROOM_DESERT
    );

    public static final List<Material> FARMING_MATERIALS = Arrays.asList(
            Material.CROPS,
            Material.POTATO,
            Material.CARROT,
            Material.MELON_BLOCK,
            Material.PUMPKIN,
            Material.SUGAR_CANE_BLOCK,
            Material.CACTUS,
            Material.BROWN_MUSHROOM,
            Material.RED_MUSHROOM,
            Material.COCOA
    );

    public static final List<Material> EXCHANGEABLE_RECIPE_RESULTS = Arrays.asList(Material.WORKBENCH,
            Material.CHEST, Material.WOOD_HOE, Material.WOOD_PICKAXE, Material.WOOD_AXE,
            Material.WOOD_SWORD, Material.WOOD_SPADE, Material.WOOD_PLATE, Material.STICK);
}
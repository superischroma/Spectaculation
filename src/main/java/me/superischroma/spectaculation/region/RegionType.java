package me.superischroma.spectaculation.region;

import lombok.Getter;
import org.bukkit.ChatColor;

@Getter
public enum RegionType
{
    LAUNCHER,
    SPAWN,
    BOOSTER,
    SAFE,
    GENERIC,
    REGENERATE,
    VILLAGE("Village", true),
    MOUNTAIN("Mountain", true),
    FOREST("Forest", true),
    FARM("Farm", true),
    RUINS("Ruins", true),
    COLOSSEUM("Colosseum", true),
    GRAVEYARD("Graveyard", true, ChatColor.RED),
    COAL_MINE("Coal Mine", true),
    COAL_MINE_CAVES("Coal Mine", true),
    WILDERNESS("Wilderness", true, ChatColor.DARK_GREEN),
    HIGH_LEVEL("High Level", true, ChatColor.RED),
    AUCTION_HOUSE("Auction House", true, ChatColor.GOLD),
    BAZAAR_ALLEY("Bazaar Alley", true, ChatColor.YELLOW),
    BANK("Bank", true, ChatColor.GOLD),
    BLACKSMITH("Blacksmith", true),
    LIBRARY("Library", true),
    THE_BARN("The Barn", true),
    MUSHROOM_DESERT("Mushroom Desert", true),
    GOLD_MINE("Gold Mine", true, ChatColor.GOLD),
    DEEP_CAVERN("Deep Caverns", true),
    GUNPOWDER_MINES("Gunpowder Mines", true),
    LAPIS_QUARRY("Lapis Quarry", true),
    PIGMENS_DEN("Pigmen's Den", true),
    SLIMEHILL("Slimehill", true),
    BIRCH_PARK("Birch Park", true, ChatColor.GREEN),
    SPRUCE_WOODS("Spruce Woods", true, ChatColor.GREEN),
    DARK_THICKET("Dark Thicket", true, ChatColor.GREEN),
    SAVANNA_WOODLAND("Savanna Woodland", true, ChatColor.GREEN),
    JUNGLE_ISLAND("Jungle Island", true, ChatColor.GREEN),
    HOWLING_CAVE("Howling Cave", true),
    DIAMOND_RESERVE("Diamond Reserve", true),
    OBSIDIAN_SANCTUARY("Obsidian Sanctuary", true),
    SPIDERS_DEN("Spider's Den", true, ChatColor.RED),
    SPIDERS_DEN_HIVE("Spider's Den", true, ChatColor.RED),
    BLAZING_FORTRESS("Blazing Fortress", true, ChatColor.RED),
    THE_END("The End", true, ChatColor.LIGHT_PURPLE),
    THE_END_NEST("The End", true, ChatColor.LIGHT_PURPLE),
    DRAGONS_NEST("Dragon's Nest", true, ChatColor.DARK_PURPLE)
    ;

    private final String name;
    private final boolean area;
    private final ChatColor color;

    RegionType(String name, boolean area, ChatColor color)
    {
        this.name = name;
        this.area = area;
        this.color = color;
    }

    RegionType(String name, boolean area)
    {
        this(name, area, ChatColor.AQUA);
    }

    RegionType()
    {
        this(null, false, ChatColor.GRAY);
    }

    public static RegionType getByID(int id)
    {
        return RegionType.values()[id];
    }
}
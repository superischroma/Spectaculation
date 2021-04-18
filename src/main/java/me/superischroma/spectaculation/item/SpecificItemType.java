package me.superischroma.spectaculation.item;

import lombok.Getter;

public enum SpecificItemType
{
    SWORD(false),
    HELMET(false),
    CHESTPLATE(false),
    LEGGINGS(false),
    BOOTS(false),
    BOW(false),
    COSMETIC(false),
    ACCESSORY(false),
    AXE(false),
    PICKAXE(false),
    SHOVEL(false),
    HOE(false),
    SHEARS(false),
    DUNGEON_ITEM,
    NONE,
    ROD(false),
    ARROW_POISON;

    @Getter
    private final boolean stackable;

    SpecificItemType(boolean stackable)
    {
        this.stackable = stackable;
    }

    SpecificItemType()
    {
        this(true);
    }

    public String getName()
    {
        return name().replaceAll("_", " ");
    }
}
package me.superischroma.spectaculation.item;

public enum SpecificItemType
{
    SWORD,
    HELMET,
    CHESTPLATE,
    LEGGINGS,
    BOOTS,
    BOW,
    COSMETIC,
    ACCESSORY,
    AXE,
    PICKAXE,
    SHOVEL,
    HOE,
    SHEARS,
    DUNGEON_ITEM,
    NONE,
    ROD,
    ARROW_POISON;

    public String getName()
    {
        return name().replaceAll("_", " ");
    }
}
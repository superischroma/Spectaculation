package me.superischroma.spectaculation.item;

import java.util.List;

public interface MaterialStatistics
{
    String getDisplayName();
    Rarity getRarity();
    default String getLore()
    {
        return null;
    }
    default List<String> getListLore()
    {
        return null;
    }
    GenericItemType getType();
    default SpecificItemType getSpecificType()
    {
        return SpecificItemType.NONE;
    }
    default boolean isStackable()
    {
        return true;
    }
    default boolean isEnchanted()
    {
        return false;
    }
    default boolean displayKills()
    {
        return false;
    }
    default boolean displayRarity()
    {
        return true;
    }
    default long getPrice()
    {
        return 1;
    }
    default long getValue()
    {
        return 1;
    }
    default ItemCategory getCategory()
    {
        return ItemCategory.TOOLS_MISC;
    }
    default void load() {}
}
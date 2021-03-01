package me.superischroma.spectaculation.item.enchanted;

import me.superischroma.spectaculation.item.*;

public class EnchantedObsidian implements EnchantedMaterialStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Enchanted Obsidian";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.UNCOMMON;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ITEM;
    }

    @Override
    public boolean isEnchanted()
    {
        return true;
    }

    @Override
    public SMaterial getCraftingMaterial()
    {
        return SMaterial.OBSIDIAN;
    }

    @Override
    public MaterialQuantifiable getResult()
    {
        return new MaterialQuantifiable(SMaterial.ENCHANTED_OBSIDIAN);
    }
}
package me.superischroma.spectaculation.item.enchanted;

import me.superischroma.spectaculation.item.*;

public class EnchantedSpruceWood implements EnchantedMaterialStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Enchanted Spruce Wood";
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
        return SMaterial.SPRUCE_WOOD;
    }

    @Override
    public MaterialQuantifiable getResult()
    {
        return new MaterialQuantifiable(SMaterial.ENCHANTED_SPRUCE_WOOD);
    }
}
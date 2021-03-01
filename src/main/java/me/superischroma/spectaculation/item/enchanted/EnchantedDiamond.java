package me.superischroma.spectaculation.item.enchanted;

import me.superischroma.spectaculation.item.*;

public class EnchantedDiamond implements EnchantedMaterialStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Enchanted Diamond";
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
        return SMaterial.DIAMOND;
    }

    @Override
    public MaterialQuantifiable getResult()
    {
        return new MaterialQuantifiable(SMaterial.ENCHANTED_DIAMOND);
    }

    @Override
    public SMaterial getBlockCraftingMaterial()
    {
        return SMaterial.DIAMOND_BLOCK;
    }

    @Override
    public MaterialQuantifiable getBlockResult()
    {
        return new MaterialQuantifiable(SMaterial.ENCHANTED_DIAMOND, 9);
    }
}
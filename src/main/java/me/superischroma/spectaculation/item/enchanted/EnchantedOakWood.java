package me.superischroma.spectaculation.item.enchanted;

import me.superischroma.spectaculation.item.*;

public class EnchantedOakWood implements EnchantedMaterialStatistics, MaterialFunction
{
    @Override
    public String getDisplayName()
    {
        return "Enchanted Oak Wood";
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
        return SMaterial.OAK_WOOD;
    }

    @Override
    public MaterialQuantifiable getResult()
    {
        return new MaterialQuantifiable(SMaterial.ENCHANTED_OAK_WOOD);
    }
}
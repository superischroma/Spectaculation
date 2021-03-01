package me.superischroma.spectaculation.item.enchanted;

import me.superischroma.spectaculation.item.*;

public interface EnchantedMaterialStatistics extends MaterialStatistics
{
    SMaterial getCraftingMaterial();
    MaterialQuantifiable getResult();
    default SMaterial getBlockCraftingMaterial()
    {
        return null;
    }
    default MaterialQuantifiable getBlockResult()
    {
        return null;
    }
    default int getCraftingRequiredAmount()
    {
        return 32;
    }
    @Override
    default void load()
    {
        if (getBlockCraftingMaterial() != null && getBlockResult() != null)
            createRecipe(new MaterialQuantifiable(getBlockCraftingMaterial(), getCraftingRequiredAmount()), getBlockResult());
        createRecipe(new MaterialQuantifiable(getCraftingMaterial(), getCraftingRequiredAmount()), getResult());
    }

    static void createRecipe(MaterialQuantifiable material, MaterialQuantifiable result)
    {
        new ShapelessRecipe(result.getMaterial(), result.getAmount())
                .add(material)
                .add(material)
                .add(material)
                .add(material)
                .add(material);
    }
}
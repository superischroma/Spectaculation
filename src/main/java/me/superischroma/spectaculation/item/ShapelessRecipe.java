package me.superischroma.spectaculation.item;

import lombok.Getter;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ShapelessRecipe extends Recipe<ShapelessRecipe>
{
    private static final List<ShapelessRecipe> CACHED_RECIPES = new ArrayList<>();

    @Getter
    private final List<MaterialQuantifiable> ingredientList;

    public ShapelessRecipe(SItem result, boolean usesExchangeables)
    {
        super(result, usesExchangeables);
        this.ingredientList = new ArrayList<>();
        CACHED_RECIPES.add(this);
    }

    public ShapelessRecipe(SItem result)
    {
        this(result, false);
    }

    public ShapelessRecipe(SMaterial material, int amount)
    {
        this(SUtil.setSItemAmount(SItem.of(material), amount));
    }

    public ShapelessRecipe(SMaterial material)
    {
        this(SItem.of(material));
    }

    @Override
    public ShapelessRecipe setResult(SItem result)
    {
        this.result = result;
        return this;
    }

    public ShapelessRecipe add(MaterialQuantifiable material)
    {
        ingredientList.add(material.clone());
        return this;
    }

    public ShapelessRecipe add(SMaterial material, int amount)
    {
        return add(new MaterialQuantifiable(material, amount));
    }

    @Override
    public List<MaterialQuantifiable> getIngredients()
    {
        return ingredientList;
    }

    protected static ShapelessRecipe parseShapelessRecipe(ItemStack[] stacks)
    {
        if (stacks.length != 9)
            throw new UnsupportedOperationException("Recipe parsing requires a 9 element array!");
        MaterialQuantifiable[] materials = SUtil.unnest(airless(new MaterialQuantifiable[][]
                {
                        MaterialQuantifiable.of(Arrays.copyOfRange(stacks, 0, 3)),
                        MaterialQuantifiable.of(Arrays.copyOfRange(stacks, 3, 6)),
                        MaterialQuantifiable.of(Arrays.copyOfRange(stacks, 6, 9))
                }), MaterialQuantifiable.class);
        for (ShapelessRecipe recipe : CACHED_RECIPES)
        {
            List<MaterialQuantifiable> ingredients = recipe.getIngredientList();
            if (materials.length != ingredients.size())
                continue;
            boolean found = true;
            for (MaterialQuantifiable ingredient : ingredients)
            {
                if (!contains(recipe.useExchangeables, materials, ingredient))
                {
                    found = false;
                    break;
                }
            }
            if (!found) continue;
            return recipe;
        }
        return null;
    }

    private static boolean contains(boolean usesExchangeables, MaterialQuantifiable[] grid, MaterialQuantifiable test)
    {
        List<SMaterial> exchangeables = getExchangeablesOf(test.getMaterial());
        for (MaterialQuantifiable material : grid)
        {
            if (usesExchangeables && exchangeables != null && exchangeables.contains(material.getMaterial()) && material.getAmount() >= test.getAmount())
                return true;
            if (material.getMaterial() == test.getMaterial() && material.getAmount() >= test.getAmount())
                return true;
        }
        return false;
    }
}

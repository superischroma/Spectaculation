package me.superischroma.spectaculation.item;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Recipe<T>
{
    protected static final List<List<SMaterial>> EXCHANGEABLES = new ArrayList<>(Arrays.asList(
            Arrays.asList(SMaterial.OAK_WOOD, SMaterial.SPRUCE_WOOD, SMaterial.BIRCH_WOOD, SMaterial.JUNGLE_WOOD,
                    SMaterial.ACACIA_WOOD, SMaterial.DARK_OAK_WOOD),
            Arrays.asList(SMaterial.OAK_WOOD_PLANKS, SMaterial.SPRUCE_WOOD_PLANKS, SMaterial.BIRCH_WOOD_PLANKS,
                    SMaterial.JUNGLE_WOOD_PLANKS, SMaterial.ACACIA_WOOD_PLANKS, SMaterial.DARK_OAK_WOOD_PLANKS)));

    @Getter
    protected SItem result;
    @Getter
    @Setter
    protected boolean useExchangeables;

    protected Recipe(SItem result, boolean useExchangeables)
    {
        this.result = result;
        this.useExchangeables = useExchangeables;
    }

    protected Recipe(SItem result)
    {
        this(result, false);
    }

    public abstract T setResult(SItem result);
    public abstract List<MaterialQuantifiable> getIngredients();

    public static Recipe<?> parseRecipe(ItemStack[] stacks)
    {
        ShapedRecipe shaped = ShapedRecipe.parseShapedRecipe(stacks);
        if (shaped != null)
            return shaped;
        return ShapelessRecipe.parseShapelessRecipe(stacks);
    }

    protected static MaterialQuantifiable[][] airless(MaterialQuantifiable[][] grid)
    {
        List<Integer> excluded = new ArrayList<>(0);
        for (int i = 0; i < grid.length; i++)
        {
            boolean ex = true;
            for (MaterialQuantifiable material : grid[i])
            {
                if (material.getMaterial() != SMaterial.AIR)
                {
                    ex = false;
                    break;
                }
            }
            if (!ex) continue;
            excluded.add(i);
        }
        int amountExcluded = excluded.size();
        MaterialQuantifiable[][] g = new MaterialQuantifiable[grid.length - amountExcluded][];
        for (int i = 0, b = 0; i < grid.length; i++)
        {
            if (excluded.contains(i))
            {
                b++;
                continue;
            }
            MaterialQuantifiable[] line = grid[i];
            int remaining = (int) Arrays.stream(line).filter((mat) -> mat.getMaterial() != SMaterial.AIR).count();
            g[i - b] = new MaterialQuantifiable[remaining];
            for (int j = 0, r = 0; j < line.length; j++)
            {
                if (line[j].getMaterial() != SMaterial.AIR)
                {
                    g[i - b][r] = line[j];
                    r++;
                }
            }
        }
        return g;
    }

    public static List<SMaterial> getExchangeablesOf(SMaterial material)
    {
        for (List<SMaterial> materials : EXCHANGEABLES)
        {
            int f = Collections.binarySearch(materials, material);
            if (f < 0) continue;
            return materials;
        }
        return null;
    }
}
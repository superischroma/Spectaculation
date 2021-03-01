package me.superischroma.spectaculation.item;

import lombok.Getter;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ShapedRecipe extends Recipe<ShapedRecipe>
{
    private static final List<ShapedRecipe> CACHED_RECIPES = new ArrayList<>();

    @Getter
    protected String[] shape;
    @Getter
    private final Map<Character, MaterialQuantifiable> ingredientMap;

    public ShapedRecipe(SItem result, boolean usesExchangeables)
    {
        super(result, usesExchangeables);
        this.ingredientMap = new HashMap<>();
        CACHED_RECIPES.add(this);
    }

    public ShapedRecipe(SItem result)
    {
        this(result, false);
    }

    public ShapedRecipe(SMaterial material, int amount)
    {
        this(SUtil.setSItemAmount(SItem.of(material), amount));
    }

    public ShapedRecipe(SMaterial material)
    {
        this(SItem.of(material));
    }

    public ShapedRecipe shape(String... lines)
    {
        this.shape = lines;
        return this;
    }

    @Override
    public ShapedRecipe setResult(SItem result)
    {
        this.result = result;
        return this;
    }

    @Override
    public List<MaterialQuantifiable> getIngredients()
    {
        return new ArrayList<>(ingredientMap.values());
    }

    public ShapedRecipe set(char k, MaterialQuantifiable material)
    {
        ingredientMap.put(k, material.clone());
        return this;
    }

    public ShapedRecipe set(char k, SMaterial material, int amount)
    {
        return set(k, new MaterialQuantifiable(material, amount));
    }

    public ShapedRecipe set(char k, SMaterial material)
    {
        return set(k, new MaterialQuantifiable(material));
    }

    private MaterialQuantifiable[][] toMQ2DArray()
    {
        MaterialQuantifiable[][] materials = new MaterialQuantifiable[3][3];
        String l1 = SUtil.pad(SUtil.getOrDefault(shape, 0, "   "), 3);
        String l2 = SUtil.pad(SUtil.getOrDefault(shape, 1, "   "), 3);
        String l3 = SUtil.pad(SUtil.getOrDefault(shape, 2, "   "), 3);
        String[] ls = {l1, l2, l3};
        for (int i = 0; i < ls.length; i++)
        {
            String[] lps = ls[i].split("");
            for (int j = 0; j < lps.length; j++)
                materials[i][j] = ingredientMap.getOrDefault(lps[j].charAt(0), new MaterialQuantifiable(SMaterial.AIR, 1));
        }
        return materials;
    }

    protected static ShapedRecipe parseShapedRecipe(ItemStack[] stacks)
    {
        if (stacks.length != 9)
            throw new UnsupportedOperationException("Recipe parsing requires a 9 element array!");
        MaterialQuantifiable[] l1 = MaterialQuantifiable.of(Arrays.copyOfRange(stacks, 0, 3));
        MaterialQuantifiable[] l2 = MaterialQuantifiable.of(Arrays.copyOfRange(stacks, 3, 6));
        MaterialQuantifiable[] l3 = MaterialQuantifiable.of(Arrays.copyOfRange(stacks, 6, 9));
        MaterialQuantifiable[][] grid = airless(new MaterialQuantifiable[][]{l1, l2, l3});
        MaterialQuantifiable[] seg = segment(MaterialQuantifiable.of(stacks));
        for (ShapedRecipe recipe : CACHED_RECIPES)
        {
            MaterialQuantifiable[][] airRecipeGrid = recipe.toMQ2DArray();
            MaterialQuantifiable[][] recipeGrid = airless(airRecipeGrid);
            MaterialQuantifiable[] recipeSeg = segment(SUtil.unnest(airRecipeGrid, MaterialQuantifiable.class));
            if (!recipeAccepted(recipe.useExchangeables, grid, recipeGrid) || !recipeAccepted(recipe.useExchangeables, seg, recipeSeg))
                continue;
            return recipe;
        }
        return null;
    }

    private static <T> boolean deepSameLength(T[][] a1, T[][] a2)
    {
        int c1 = 0, c2 = 0;
        for (T[] a : a1)
            c1 += a.length;
        for (T[] a : a2)
            c2 += a.length;
        return c1 == c2;
    }

    private static MaterialQuantifiable[] segment(MaterialQuantifiable[] materials)
    {
        int firstNonAir = -1, lastNonAir = -1;
        for (int i = 0; i < materials.length; i++)
        {
            MaterialQuantifiable material = materials[i];
            if (firstNonAir == -1 && material.getMaterial() != SMaterial.AIR)
                firstNonAir = i;
            if (material.getMaterial() != SMaterial.AIR)
                lastNonAir = i;
        }
        if (firstNonAir == -1 || lastNonAir == -1)
            return new MaterialQuantifiable[]{};
        return Arrays.copyOfRange(materials, firstNonAir, lastNonAir + 1);
    }

    private static boolean recipeAccepted(boolean usesExchangeables, MaterialQuantifiable[][] grid, MaterialQuantifiable[][] recipeGrid)
    {
        if (!deepSameLength(grid, recipeGrid))
            return false;
        boolean found = true;
        try
        {
            for (int i = 0; i < grid.length; i++)
            {
                for (int j = 0; j < grid[i].length; j++)
                {
                    MaterialQuantifiable m1 = grid[i][j], m2 = recipeGrid[i][j];
                    List<SMaterial> exchangeables = getExchangeablesOf(m2.getMaterial());
                    if (usesExchangeables && exchangeables != null && exchangeables.contains(m1.getMaterial()) && m1.getAmount() >= m2.getAmount())
                        continue;
                    if (m1.getMaterial() != m2.getMaterial() || m1.getAmount() < m2.getAmount())
                    {
                        found = false;
                        break;
                    }
                }
                if (!found)
                    break;
            }
        }
        catch (IndexOutOfBoundsException ex)
        {
            return false;
        }
        return found;
    }

    private static boolean recipeAccepted(boolean usesExchangeables, MaterialQuantifiable[] grid1d, MaterialQuantifiable[] recipeGrid1d)
    {
        if (grid1d.length != recipeGrid1d.length)
            return false;
        boolean found = true;
        for (int i = 0; i < grid1d.length; i++)
        {
            MaterialQuantifiable m1 = grid1d[i], m2 = recipeGrid1d[i];
            List<SMaterial> exchangeables = getExchangeablesOf(m2.getMaterial());
            if (usesExchangeables && exchangeables != null && exchangeables.contains(m1.getMaterial()) && m1.getAmount() >= m2.getAmount())
                continue;
            if (m1.getMaterial() != m2.getMaterial() || m1.getAmount() < m2.getAmount())
            {
                found = false;
                break;
            }
        }
        return found;
    }
}

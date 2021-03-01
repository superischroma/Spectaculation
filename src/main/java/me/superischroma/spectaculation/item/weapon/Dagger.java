package me.superischroma.spectaculation.item.weapon;

import me.superischroma.spectaculation.item.*;

public class Dagger implements ToolStatistics, MaterialFunction
{
    @Override
    public int getBaseDamage()
    {
        return 35;
    }

    @Override
    public String getDisplayName()
    {
        return "Dagger";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.UNCOMMON;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType()
    {
        return SpecificItemType.SWORD;
    }

    @Override
    public String getLore()
    {
        return "Slice through your enemies in a fierce way!";
    }

    @Override
    public void load()
    {
        ShapedRecipe recipe = new ShapedRecipe(SMaterial.DAGGER).shape("ab", "cd");
        recipe.set('a', SMaterial.IRON_SWORD);
        recipe.set('b', SMaterial.DIAMOND_SWORD);
        recipe.set('c', SMaterial.GOLD_SWORD);
        recipe.set('d', SMaterial.STONE_SWORD);
    }
}
package me.superischroma.spectaculation.item;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialQuantifiable
{
    @Getter
    private SMaterial material;
    @Getter
    private int amount;

    public MaterialQuantifiable(SMaterial material, int amount)
    {
        this.material = material;
        this.amount = amount;
    }

    public MaterialQuantifiable(SMaterial material)
    {
        this(material, 1);
    }

    public MaterialQuantifiable setMaterial(SMaterial material)
    {
        this.material = material;
        return this;
    }

    public MaterialQuantifiable setAmount(int amount)
    {
        this.amount = amount;
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof MaterialQuantifiable)) return false;
        MaterialQuantifiable material = (MaterialQuantifiable) o;
        return material.material == this.material && material.amount == this.amount;
    }

    public MaterialQuantifiable clone()
    {
        return new MaterialQuantifiable(material, amount);
    }

    public String toString()
    {
        return "MQ{material=" + (material != null ? material.name() : "?") + ", amount=" + amount + "}";
    }

    public static MaterialQuantifiable of(ItemStack stack)
    {
        if (stack == null || stack.getType() == Material.AIR)
            return new MaterialQuantifiable(SMaterial.AIR, (stack != null ? stack.getAmount() : 1));
        SItem found = SItem.find(stack);
        if (found == null)
            found = SItem.of(SMaterial.getSpecEquivalent(stack.getType(), stack.getDurability()));
        return new MaterialQuantifiable(found.getType(), stack.getAmount());
    }

    public static MaterialQuantifiable[] of(ItemStack[] stacks)
    {
        MaterialQuantifiable[] materials = new MaterialQuantifiable[stacks.length];
        for (int i = 0; i < stacks.length; i++)
            materials[i] = of(stacks[i]);
        return materials;
    }
}
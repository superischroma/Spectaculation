package me.superischroma.spectaculation.entity;

import lombok.Getter;
import me.superischroma.spectaculation.item.ItemOrigin;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class EntityDrop
{
    private final ItemStack stack;
    private final EntityDropType type;
    private final double dropChance;
    private final Player owner;

    public EntityDrop(ItemStack stack, EntityDropType type, double dropChance, Player owner)
    {
        this.stack = stack;
        this.type = type;
        this.dropChance = dropChance;
        this.owner = owner;
    }

    public EntityDrop(SMaterial material, byte variant, EntityDropType type, double dropChance, Player owner)
    {
        this.stack = SItem.of(material, variant).getStack();
        this.type = type;
        this.dropChance = dropChance;
        this.owner = owner;
    }

    public EntityDrop(SMaterial material, EntityDropType type, double dropChance, Player owner)
    {
        this(material, (byte) material.getData(), type, dropChance, owner);
    }

    public EntityDrop(SMaterial material, byte variant, EntityDropType type, double dropChance)
    {
        this(material, variant, type, dropChance, null);
    }

    public EntityDrop(ItemStack stack, EntityDropType type, double dropChance)
    {
        this(stack, type, dropChance, null);
    }

    public EntityDrop(SMaterial material, EntityDropType type, double dropChance)
    {
        this(material, type, dropChance, null);
    }

    public ItemStack getStack()
    {
        return stack;
    }

    public EntityDropType getType()
    {
        return type;
    }

    public double getDropChance()
    {
        return dropChance;
    }
}
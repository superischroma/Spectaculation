package me.superischroma.spectaculation.entity;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class SEntityEquipment
{
    private ItemStack itemInHand;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;

    public SEntityEquipment(ItemStack itemInHand, ItemStack helmet, ItemStack chestplate,
                            ItemStack leggings, ItemStack boots)
    {
        this.itemInHand = itemInHand;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }
}
package me.superischroma.spectaculation.entity.zombie;

import me.superischroma.spectaculation.entity.*;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class GoldenGhoul extends BaseZombie
{
    @Override
    public String getEntityName()
    {
        return "Golden Ghoul";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 45000.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 800.0;
    }

    @Override
    public SEntityEquipment getEntityEquipment()
    {
        return new SEntityEquipment(new ItemStack(Material.GOLD_SWORD),
                null,
                new ItemStack(Material.GOLD_CHESTPLATE),
                new ItemStack(Material.GOLD_LEGGINGS),
                new ItemStack(Material.GOLD_BOOTS));
    }

    @Override
    public List<EntityDrop> drops()
    {
        return Arrays.asList(new EntityDrop(new ItemStack(Material.ROTTEN_FLESH, SUtil.random(2, 4)), EntityDropType.GUARANTEED, 1.0),
                new EntityDrop(new ItemStack(Material.GOLD_INGOT, SUtil.random(1, 11)), EntityDropType.GUARANTEED, 1.0),
                new EntityDrop(SMaterial.GOLDEN_POWDER, EntityDropType.CRAZY_RARE, 0.006));
    }

    public double getXPDropped()
    {
        return 50.0;
    }

    @Override
    public boolean isBaby()
    {
        return false;
    }

    @Override
    public boolean isVillager()
    {
        return false;
    }
}
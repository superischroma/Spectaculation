package me.superischroma.spectaculation.entity.caverns;

import me.superischroma.spectaculation.entity.*;
import me.superischroma.spectaculation.entity.zombie.BaseZombie;
import me.superischroma.spectaculation.item.SMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class DiamondZombie extends BaseZombie
{
    @Override
    public String getEntityName()
    {
        return "Zombie";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 250.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 200.0;
    }

    @Override
    public SEntityEquipment getEntityEquipment()
    {
        return new SEntityEquipment(new ItemStack(Material.DIAMOND_SWORD),
                new ItemStack(Material.DIAMOND_HELMET),
                new ItemStack(Material.DIAMOND_CHESTPLATE),
                new ItemStack(Material.DIAMOND_LEGGINGS),
                new ItemStack(Material.DIAMOND_BOOTS));
    }

    @Override
    public List<EntityDrop> drops()
    {
        return Arrays.asList(new EntityDrop(SMaterial.ROTTEN_FLESH, EntityDropType.GUARANTEED, 1.0),
                new EntityDrop(SMaterial.MINER_HELMET, EntityDropType.RARE, 0.05),
                new EntityDrop(SMaterial.MINER_CHESTPLATE, EntityDropType.RARE, 0.05),
                new EntityDrop(SMaterial.MINER_LEGGINGS, EntityDropType.RARE, 0.05),
                new EntityDrop(SMaterial.MINER_BOOTS, EntityDropType.RARE, 0.05));
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

    @Override
    public double getXPDropped()
    {
        return 20.0;
    }
}
package me.superischroma.spectaculation.entity.end;

import me.superischroma.spectaculation.entity.*;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class Watcher implements EntityFunction, EntityStatistics
{
    @Override
    public String getEntityName()
    {
        return "Watcher";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 9500.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 475.0;
    }

    @Override
    public double getXPDropped()
    {
        return 40.0;
    }

    @Override
    public List<EntityDrop> drops()
    {
        return Arrays.asList(
                new EntityDrop(new ItemStack(Material.ENDER_PEARL, SUtil.random(2, 4)), EntityDropType.GUARANTEED, 1.0),
                new EntityDrop(new ItemStack(Material.ARROW, SUtil.random(2, 4)), EntityDropType.GUARANTEED, 1.0),
                new EntityDrop(SMaterial.ENCHANTED_BONE, EntityDropType.RARE, 0.05),
                new EntityDrop(SMaterial.END_STONE_BOW, EntityDropType.EXTRAORDINARILY_RARE, 0.01)
        );
    }

    @Override
    public SEntityEquipment getEntityEquipment()
    {
        return new SEntityEquipment(SItem.of(SMaterial.END_STONE_BOW).getStack(),
                SItem.of(SMaterial.SUMMONING_EYE).getStack(),
                SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE), Color.fromRGB(0x000000)),
                SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_LEGGINGS), Color.fromRGB(0x000000)),
                SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_BOOTS), Color.fromRGB(0x000000)));
    }
}
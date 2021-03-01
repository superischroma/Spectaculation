package me.superischroma.spectaculation.entity.zombie;

import me.superischroma.spectaculation.entity.EntityDrop;
import me.superischroma.spectaculation.entity.EntityDropType;
import me.superischroma.spectaculation.entity.SEntityEquipment;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class RevenantSycophant extends BaseZombie
{
    @Override
    public String getEntityName()
    {
        return "Revenant Sycophant";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 24000;
    }

    @Override
    public double getDamageDealt()
    {
        return 850.0;
    }

    @Override
    public double getXPDropped()
    {
        return 300.0;
    }

    @Override
    public SEntityEquipment getEntityEquipment()
    {
        return new SEntityEquipment(SUtil.enchant(new ItemStack(Material.DIAMOND_SWORD)), null,
                SUtil.enchant(new ItemStack(Material.DIAMOND_CHESTPLATE)), SUtil.enchant(new ItemStack(Material.CHAINMAIL_LEGGINGS)),
                        new ItemStack(Material.IRON_BOOTS));
    }

    @Override
    public List<EntityDrop> drops()
    {
        return Collections.singletonList(new EntityDrop(SMaterial.REVENANT_FLESH, EntityDropType.GUARANTEED, 1.0));
    }
}
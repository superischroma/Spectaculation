package me.superischroma.spectaculation.entity.end;

import me.superischroma.spectaculation.entity.*;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.util.SUtil;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSkeleton;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class ObsidianDefender implements EntityFunction, EntityStatistics
{
    @Override
    public String getEntityName()
    {
        return "Obsidian Defender";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 10000.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 200.0;
    }

    @Override
    public List<EntityDrop> drops()
    {
        return Arrays.asList(
                new EntityDrop(new ItemStack(Material.OBSIDIAN, SUtil.random(6, 7)), EntityDropType.GUARANTEED, 1.0),
                new EntityDrop(SItem.of(SMaterial.ENCHANTED_OBSIDIAN).getStack(), EntityDropType.RARE, 0.05),
                new EntityDrop(SItem.of(SMaterial.OBSIDIAN_CHESTPLATE).getStack(), EntityDropType.EXTRAORDINARILY_RARE, 0.001)
        );
    }

    @Override
    public SEntityEquipment getEntityEquipment()
    {
        return new SEntityEquipment(new ItemStack(Material.AIR),
                new ItemStack(Material.OBSIDIAN),
                SItem.of(SMaterial.OBSIDIAN_CHESTPLATE).getStack(),
                null, null);
    }

    @Override
    public void onSpawn(LivingEntity entity, SEntity sEntity)
    {
        ((CraftLivingEntity) entity).getHandle().getAttributeInstance(GenericAttributes.c).setValue(1.0D);
        ((CraftSkeleton) entity).getHandle().setSkeletonType(1);
        entity.getEquipment().setItemInHand(null);
    }

    @Override
    public double getMovementSpeed()
    {
        return 0.6;
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent e)
    {
        if (!(e.getEntity() instanceof LivingEntity)) return;
        if (e.getEntity() instanceof Player)
        {
            PlayerInventory inventory = ((Player) e.getEntity()).getInventory();
            SItem sItem = SItem.find(inventory.getChestplate());
            if (sItem != null)
            {
                if (sItem.getType() == SMaterial.OBSIDIAN_CHESTPLATE)
                    return;
            }
        }
        ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 80, 1));
    }

    @Override
    public double getXPDropped()
    {
        return 43.2;
    }
}
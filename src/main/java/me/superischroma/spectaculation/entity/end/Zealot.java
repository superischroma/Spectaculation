package me.superischroma.spectaculation.entity.end;

import com.google.common.util.concurrent.AtomicDouble;
import me.superischroma.spectaculation.entity.*;
import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.item.pet.Pet;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Zealot implements EndermanStatistics, EntityFunction
{
    @Override
    public String getEntityName()
    {
        return "Zealot";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 13000.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 1250.0;
    }

    @Override
    public List<EntityDrop> drops()
    {
        return Arrays.asList(new EntityDrop(new ItemStack(Material.ENDER_PEARL, SUtil.random(3, 5)), EntityDropType.GUARANTEED, 1.0),
                new EntityDrop(new ItemStack(Material.ENDER_PEARL, SUtil.random(3, 5)), EntityDropType.GUARANTEED, 0.05));
    }

    @Override
    public void onDeath(SEntity sEntity, Entity killed, Entity damager)
    {
        Player player = (Player) damager;
        User user = User.getUser(player.getUniqueId());
        Pet pet = user.getActivePetClass();
        AtomicDouble chance = new AtomicDouble(420.0);
        if (pet != null) pet.runAbilities(ability -> ability.onZealotAttempt(chance), user.getActivePet());
        if (SUtil.random(1.0, chance.get()) != 1) return;
        player.playSound(player.getLocation(), Sound.WITHER_SPAWN, 1f, 1f);
        SUtil.sendTitle(player, ChatColor.RED + "SPECIAL ZEALOT");
        player.sendMessage(ChatColor.GREEN + "A special " + ChatColor.LIGHT_PURPLE + "Zealot" + ChatColor.GREEN + " has spawned nearby!");
        new SEntity(killed, SEntityType.SPECIAL_ZEALOT);
    }

    @Override
    public double getXPDropped()
    {
        return 40.0;
    }

    public static class SpecialZealot implements EndermanStatistics, EntityFunction
    {
        @Override
        public String getEntityName()
        {
            return "Zealot";
        }

        @Override
        public double getEntityMaxHealth()
        {
            return 2000.0;
        }

        @Override
        public double getDamageDealt()
        {
            return 1250.0;
        }

        @Override
        public List<EntityDrop> drops()
        {
            return Collections.singletonList(new EntityDrop(SItem.of(SMaterial.SUMMONING_EYE).getStack(), EntityDropType.RARE, 1.0));
        }

        @Override
        public MaterialData getCarriedMaterial()
        {
            return new MaterialData(Material.ENDER_PORTAL_FRAME);
        }

        @Override
        public double getXPDropped()
        {
            return 40.0;
        }
    }

    public static class EnderChestZealot implements EndermanStatistics, EntityFunction
    {
        @Override
        public String getEntityName()
        {
            return "Zealot";
        }

        @Override
        public double getEntityMaxHealth()
        {
            return 13000.0;
        }

        @Override
        public double getDamageDealt()
        {
            return 1250.0;
        }

        @Override
        public List<EntityDrop> drops()
        {
            return Arrays.asList(new EntityDrop(new ItemStack(Material.ENDER_PEARL, SUtil.random(3, 5)), EntityDropType.GUARANTEED, 1.0),
                    SUtil.getRandom(Arrays.asList(
                            new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.ENCHANTED_ENDER_PEARL).getStack(),
                                    SUtil.random(1, 2)), EntityDropType.OCCASIONAL, 1.0),
                            new EntityDrop(SMaterial.CRYSTAL_FRAGMENT, EntityDropType.OCCASIONAL, 1.0),
                            new EntityDrop(SMaterial.ENCHANTED_END_STONE, EntityDropType.OCCASIONAL, 1.0),
                            new EntityDrop(SMaterial.ENCHANTED_OBSIDIAN, EntityDropType.OCCASIONAL, 1.0))));
        }

        @Override
        public MaterialData getCarriedMaterial()
        {
            return new MaterialData(Material.ENDER_CHEST);
        }

        @Override
        public void onDeath(SEntity sEntity, Entity killed, Entity damager)
        {
            if (SUtil.random(1, 420) != 1) return;
            Player player = (Player) damager;
            player.playSound(player.getLocation(), Sound.WITHER_SPAWN, 1f, 1f);
            SUtil.sendTitle(player, ChatColor.RED + "SPECIAL ZEALOT");
            player.sendMessage(ChatColor.GREEN + "A special " + ChatColor.LIGHT_PURPLE + "Zealot" + ChatColor.GREEN + " has spawned nearby!");
            new SEntity(killed, SEntityType.SPECIAL_ZEALOT);
        }

        @Override
        public double getXPDropped()
        {
            return 40.0;
        }
    }
}
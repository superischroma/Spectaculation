package me.superischroma.spectaculation.item.oddities;

import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.potion.PotionEffect;
import me.superischroma.spectaculation.potion.PotionEffectType;
import me.superischroma.spectaculation.util.SUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class WaterBottle implements MaterialStatistics, MaterialFunction, ItemData
{
    @Override
    public NBTTagCompound getData()
    {
        return new NBTTagCompound();
    }

    @Override
    public String getDisplayName()
    {
        return "Water Bottle";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.COMMON;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ITEM;
    }

    @Override
    public List<String> getDataLore(String key, Object value)
    {
        if (!key.equals("effects")) return null;
        NBTTagCompound compound = (NBTTagCompound) value;
        List<String> lore = new ArrayList<>();
        for (String k : compound.c())
        {
            lore.add(" ");
            NBTTagCompound effectData = compound.getCompound(k);
            PotionEffectType type = PotionEffectType.getByNamespace(k);
            int level = effectData.getInt("level");
            long duration = effectData.getLong("duration");
            PotionEffect effect = new PotionEffect(type, level, duration);
            lore.add(type.getName() + " " + SUtil.toRomanNumeral(effect.getLevel()) +
                    (!effect.getType().isInstant() ? ChatColor.WHITE + " (" + effect.getDurationDisplay() + ")" : ""));
            for (String line : SUtil.splitByWordAndLength(effect.getDescription(), 30, "\\s"))
                lore.add(ChatColor.GRAY + line);
        }
        return lore;
    }

    @Override
    public void onInstanceUpdate(SItem instance)
    {
        int max = 0;
        for (PotionEffect effect : instance.getPotionEffects())
        {
            if (effect.getLevel() > max)
                max = effect.getLevel();
        }
        switch (max)
        {
            case 0:
            case 1:
            case 2:
            {
                instance.setRarity(Rarity.COMMON, false);
                break;
            }
            case 3:
            case 4:
            {
                instance.setRarity(Rarity.UNCOMMON, false);
                break;
            }
            case 5:
            case 6:
            {
                instance.setRarity(Rarity.RARE, false);
                break;
            }
            case 7:
            case 8:
            {
                instance.setRarity(Rarity.EPIC, false);
                break;
            }
            case 9:
            case 10:
            {
                instance.setRarity(Rarity.LEGENDARY, false);
                break;
            }
            case 11:
            case 12:
            {
                instance.setRarity(Rarity.MYTHIC, false);
                break;
            }
            case 13:
            case 14:
            {
                instance.setRarity(Rarity.SUPREME, false);
                break;
            }
            case 15:
            case 16:
            {
                instance.setRarity(Rarity.SPECIAL, false);
                break;
            }
            default:
            {
                instance.setRarity(Rarity.VERY_SPECIAL, false);
                break;
            }
        }
        if (instance.getPotionEffects().size() == 1)
            instance.setDisplayName(ChatColor.stripColor(instance.getPotionEffects().get(0).getType().getName() + " " + SUtil.toRomanNumeral(instance.getPotionEffects().get(0).getLevel())) + " Potion");
        if (instance.getPotionEffects().size() > 1)
            instance.setDisplayName("Potion");
        if (instance.getPotionEffects().size() == 0)
            instance.setDisplayName("Water Bottle");
    }
}
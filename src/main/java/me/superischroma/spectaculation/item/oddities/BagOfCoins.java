package me.superischroma.spectaculation.item.oddities;

import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.util.SUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BagOfCoins implements SkullStatistics, MaterialFunction, ItemData
{
    @Override
    public String getDisplayName()
    {
        return "Bag of Coins";
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
    public void onInstanceUpdate(SItem instance)
    {
        long coins = instance.getDataLong("coins");
        if (coins < 10000)
            instance.setRarity(Rarity.COMMON, false);
        else if (coins < 100000)
            instance.setRarity(Rarity.UNCOMMON, false);
        else if (coins < 250000)
            instance.setRarity(Rarity.RARE, false);
        else if (coins < 4000000)
            instance.setRarity(Rarity.EPIC, false);
        else if (coins < 10000000)
            instance.setRarity(Rarity.LEGENDARY, false);
        else if (coins < 25000000)
            instance.setRarity(Rarity.MYTHIC, false);
        else if (coins < 100000000)
            instance.setRarity(Rarity.SUPREME, false);
        else if (coins < 500000000)
            instance.setRarity(Rarity.SPECIAL, false);
        else
            instance.setRarity(Rarity.VERY_SPECIAL, false);
    }

    @Override
    public NBTTagCompound getData()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setLong("coins", 1);
        return compound;
    }

    @Override
    public List<String> getDataLore(String key, Object value)
    {
        if (!key.equals("coins")) return null;
        return Collections.singletonList(ChatColor.GOLD + "Contents: " + ChatColor.YELLOW + SUtil.commaify((long) value) + " coins");
    }

    @Override
    public String getURL()
    {
        return "8381c529d52e03cd74c3bf38bb6ba3fde1337ae9bf50332faa889e0a28e8081f";
    }
}
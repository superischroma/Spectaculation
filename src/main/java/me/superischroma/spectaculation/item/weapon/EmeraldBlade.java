package me.superischroma.spectaculation.item.weapon;

import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class EmeraldBlade implements ToolStatistics, MaterialFunction, Ownable
{
    @Override
    public String getDisplayName()
    {
        return "Emerald Blade";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.EPIC;
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType()
    {
        return SpecificItemType.SWORD;
    }

    @Override
    public int getBaseDamage()
    {
        return 130;
    }

    @Override
    public List<String> getListLore()
    {
        return Arrays.asList("A powerful blade made from pure",
                ChatColor.DARK_GREEN + "Emeralds" + ChatColor.GRAY + ". This blade becomes",
                "stronger as you carry more", ChatColor.GOLD + "coins" + ChatColor.GRAY + " in your purse.");
    }

    @Override
    public List<String> getDataLore(String key, Object value)
    {
        if (!key.equals("owner")) return null;
        Player player = Bukkit.getPlayer(UUID.fromString(String.valueOf(value)));
        if (player == null) return null;
        User user = User.getUser(player.getUniqueId());
        return Collections.singletonList(ChatColor.GRAY + "Current Damage Bonus: " + ChatColor.GREEN + SUtil.roundTo(2.5 * SUtil.quadrt(user.getCoins()), 1));
    }

    @Override
    public NBTTagCompound getData()
    {
        return new NBTTagCompound();
    }
}
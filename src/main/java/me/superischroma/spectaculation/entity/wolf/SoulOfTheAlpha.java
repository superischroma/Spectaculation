package me.superischroma.spectaculation.entity.wolf;

import me.superischroma.spectaculation.entity.EntityDrop;
import me.superischroma.spectaculation.entity.EntityDropType;
import me.superischroma.spectaculation.item.SMaterial;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class SoulOfTheAlpha extends BaseWolf
{
    @Override
    public String getEntityName()
    {
        return ChatColor.DARK_AQUA + "Soul of the Alpha";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 31150.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 1282.0;
    }

    @Override
    public List<EntityDrop> drops()
    {
        return Arrays.asList(new EntityDrop(SMaterial.JUNGLE_WOOD, EntityDropType.GUARANTEED, 1.0),
                new EntityDrop(SMaterial.WEAK_WOLF_CATALYST, EntityDropType.VERY_RARE, 0.005));
    }

    public double getXPDropped()
    {
        return 50.0;
    }

    @Override
    public boolean isAngry()
    {
        return true;
    }
}
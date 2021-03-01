package me.superischroma.spectaculation.item.exclusive;

import me.superischroma.spectaculation.entity.SEntity;
import me.superischroma.spectaculation.entity.SEntityType;
import me.superischroma.spectaculation.item.MaterialFunction;
import me.superischroma.spectaculation.item.Rarity;
import me.superischroma.spectaculation.item.GenericItemType;
import me.superischroma.spectaculation.item.SkullStatistics;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class FloatingCrystal implements SkullStatistics, MaterialFunction
{
    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ITEM;
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.EXCLUSIVE;
    }

    @Override
    public void onInteraction(PlayerInteractEvent e)
    {
        if (e.getAction() == Action.LEFT_CLICK_AIR ||
            e.getAction() == Action.LEFT_CLICK_BLOCK) return;
        Player player = e.getPlayer();
        SEntity sEntity = new SEntity(player.getLocation().clone().add(player.getLocation().getDirection().multiply(1.5)), getCrystalType());
    }

    protected abstract SEntityType getCrystalType();
}

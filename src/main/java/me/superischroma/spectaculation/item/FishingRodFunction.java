package me.superischroma.spectaculation.item;

import org.bukkit.event.player.PlayerFishEvent;

public interface FishingRodFunction extends MaterialFunction
{
    void onFish(SItem instance, PlayerFishEvent e);
}

package me.superischroma.spectaculation.entity.caverns;

import me.superischroma.spectaculation.entity.EntityFunction;
import me.superischroma.spectaculation.entity.SEntity;
import me.superischroma.spectaculation.event.CreeperIgniteEvent;

public interface CreeperFunction extends EntityFunction
{
    default void onCreeperIgnite(CreeperIgniteEvent e, SEntity sEntity) {}
}
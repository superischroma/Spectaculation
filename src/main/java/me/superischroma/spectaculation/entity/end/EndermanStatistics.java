package me.superischroma.spectaculation.entity.end;

import me.superischroma.spectaculation.entity.EntityStatistics;
import org.bukkit.material.MaterialData;

public interface EndermanStatistics extends EntityStatistics
{
    default MaterialData getCarriedMaterial()
    {
        return null;
    }
}
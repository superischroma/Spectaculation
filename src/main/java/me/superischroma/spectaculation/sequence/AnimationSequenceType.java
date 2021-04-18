package me.superischroma.spectaculation.sequence;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class AnimationSequenceType
{
    private static final List<AnimationSequenceType> TYPES = new ArrayList<>();

    private final String namespace;
    private final AnimationSequence sequence;

    public AnimationSequenceType(String namespace, AnimationSequence sequence)
    {
        this.namespace = namespace;
        this.sequence = sequence;
        TYPES.add(this);
    }

    public String getNamespace()
    {
        return namespace;
    }

    public AnimationSequence getSequence()
    {
        return sequence;
    }

    public void play(Location location)
    {
        sequence.play(location);
    }

    public void play(Entity entity)
    {
        sequence.play(entity);
    }

    public static AnimationSequenceType getByNamespace(String namespace)
    {
        for (AnimationSequenceType type : TYPES)
        {
            if (namespace.toLowerCase().equals(type.namespace.toLowerCase()))
                return type;
        }
        return null;
    }
}
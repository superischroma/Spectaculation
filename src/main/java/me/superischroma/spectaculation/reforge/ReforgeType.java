package me.superischroma.spectaculation.reforge;

import lombok.Getter;

public enum ReforgeType
{
    OVERPOWERED(OverpoweredReforge.class, false),
    SUPERGENIUS(SupergeniusReforge.class, false),
    SPICY(SpicyReforge.class),
    FIERCE(FierceReforge.class),
    HEROIC(HeroicReforge.class),
    ODD(OddReforge.class),
    RAPID(RapidReforge.class);

    private final Class<? extends Reforge> clazz;
    @Getter
    private final boolean accessible;

    ReforgeType(Class<? extends Reforge> clazz, boolean accessible)
    {
        this.clazz = clazz;
        this.accessible = accessible;
    }

    ReforgeType(Class<? extends Reforge> clazz)
    {
        this(clazz, true);
    }

    public Reforge getReforge()
    {
        try
        {
            return clazz.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static ReforgeType getReforgeType(String name)
    {
        return valueOf(name.toUpperCase());
    }

    public static ReforgeType getByClass(Class<? extends Reforge> clazz)
    {
        for (ReforgeType type : values())
        {
            if (type.clazz == clazz)
                return type;
        }
        return null;
    }
}
package me.superischroma.spectaculation.potion;

import lombok.Getter;

public enum PotionColor
{
    BLUE((short) 0),
    PURPLE((short) 1),
    LIGHT_BLUE((short) 2),
    ORANGE((short) 3),
    DARK_GREEN((short) 4),
    RED((short) 5),
    DARK_BLUE((short) 6),
    GRAY((short) 8),
    DARK_RED((short) 9),
    DARK_GRAY((short) 10),
    GREEN((short) 11),
    BLOOD_RED((short) 12),
    TWILIGHT_BLUE((short) 13),
    LIGHT_GRAY((short) 14);

    public static final short SPLASH = 16384;

    @Getter
    private final short data;

    PotionColor(short data)
    {
        this.data = data;
    }

    public short getSplashData()
    {
        return (short) (data + SPLASH);
    }
}

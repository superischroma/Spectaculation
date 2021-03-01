package me.superischroma.spectaculation.potion;

import lombok.Getter;
import me.superischroma.spectaculation.util.SUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

import java.util.Arrays;

@Getter
public class PotionEffect
{
    private final PotionEffectType type;
    private final int level;
    private final long duration;

    public PotionEffect(PotionEffectType type, int level, long duration)
    {
        this.type = type;
        this.level = level;
        this.duration = duration;
    }

    public String getDescription()
    {
        if (type == PotionEffectType.STRENGTH)
            return type.getDescription(SUtil.getOrDefault(Arrays.asList(5, 12, 20, 30, 40, 50, 60, 75), level - 1, level * 10));
        if (type == PotionEffectType.RABBIT)
            return type.getDescription(SUtil.toRomanNumeral(level % 2 == 0 ? level / 2 : level / 2), level * 10);
        if (type == PotionEffectType.HEALING)
            return type.getDescription(SUtil.getOrDefault(Arrays.asList(20, 50, 100, 150, 200, 300, 400, 500), level - 1, (level - 3) * 100));
        if (type == PotionEffectType.SPEED)
            return type.getDescription(level * 5);
        if (type == PotionEffectType.ARCHERY)
            return type.getDescription(SUtil.getOrDefault(Arrays.asList(12.5, 25, 50, 75), level - 1, (level * 25) - 25));
        if (type == PotionEffectType.MANA)
            return type.getDescription(SUtil.getOrDefault(Arrays.asList(25, 50, 75, 100, 150, 200, 300, 400), level - 1, (level - 4) * 100));
        if (type == PotionEffectType.ADRENALINE)
            return type.getDescription(SUtil.getOrDefault(Arrays.asList(20, 40, 60, 80, 100, 150, 200, 300), level - 1, (level - 5) * 100), level * 5);
        if (type == PotionEffectType.CRITICAL)
            return type.getDescription(5 + (level * 5), level * 10);
        if (type == PotionEffectType.ABSORPTION)
            return type.getDescription(SUtil.getOrDefault(Arrays.asList(20, 40, 60, 80, 100, 150, 200, 300), level - 1, (level - 5) * 100));
        if (type == PotionEffectType.RESISTANCE)
            return type.getDescription(SUtil.getOrDefault(Arrays.asList(5, 10, 15, 20, 30, 40, 50, 60), level - 1, (level * 10) - 20));
        if (type == PotionEffectType.TRUE_RESISTANCE)
            return type.getDescription(level * 5);
        if (type == PotionEffectType.STAMINA)
            return type.getDescription(SUtil.getOrDefault(Arrays.asList(150, 215, 315, 515), level - 1,
                    ((level + 1) * 100) + 15), 50 * level);
        if (type == PotionEffectType.SPIRIT)
            return type.getDescription(level * 10, level * 10);
        return type.getDescription();
    }

    public String getDurationDisplay()
    {
        return SUtil.getFormattedTime(duration);
    }

    public NBTTagCompound toCompound()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInt("level", level);
        compound.setLong("duration", duration);
        return compound;
    }

    public static PotionEffect ofCompound(String namespace, NBTTagCompound compound)
    {
        PotionEffectType type = PotionEffectType.getByNamespace(namespace);
        int level = compound.getInt("level");
        long duration = compound.getLong("duration");
        return new PotionEffect(type, level, duration);
    }
}
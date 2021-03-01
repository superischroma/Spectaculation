package me.superischroma.spectaculation.user;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

public class DoublePlayerStatistic implements PlayerStatistic<Double>
{
    @Getter
    private final double defaultValue;
    private final ArrayList<Double> values;

    public DoublePlayerStatistic(double defaultValue)
    {
        this.defaultValue = defaultValue;
        this.values = new ArrayList<>(6);
        this.values.addAll(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
    }

    public DoublePlayerStatistic()
    {
        this(0.0);
    }

    public Double addAll()
    {
        double result = defaultValue;
        for (Double value : values)
            result += value;
        return result;
    }

    public void add(int slot, Double d)
    {
        set(slot, safeGet(slot) + d);
    }

    public void sub(int slot, Double d)
    {
        set(slot, safeGet(slot) - d);
    }

    public void zero(int slot)
    {
        set(slot, 0.0);
    }

    public boolean contains(int slot)
    {
        return slot >= 0 && slot < values.size();
    }

    public Double safeGet(int index)
    {
        if (index < 0 || index > values.size() - 1) set(index, 0.0);
        return values.get(index);
    }

    public void set(int slot, Double d)
    {
        values.ensureCapacity(slot + 1);
        while (values.size() < slot + 1)
            values.add(0.0);
        values.set(slot, d);
    }

    public int next()
    {
        return values.size();
    }

    public Double getFor(int slot)
    {
        return safeGet(slot);
    }

    public ArrayList<Double> forInventory()
    {
        ArrayList<Double> list = new ArrayList<>();
        for (int i = 6; i < values.size(); i++)
            list.add(safeGet(i));
        return list;
    }
}
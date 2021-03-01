package me.superischroma.spectaculation.user;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

public class IntegerPlayerStatistic implements PlayerStatistic<Integer>
{
    @Getter
    private final int defaultValue;
    private final ArrayList<Integer> values;

    public IntegerPlayerStatistic(int defaultValue)
    {
        this.defaultValue = defaultValue;
        this.values = new ArrayList<>(6);
        this.values.addAll(Arrays.asList(0, 0, 0, 0, 0, 0));
    }

    public IntegerPlayerStatistic()
    {
        this(0);
    }

    public Integer addAll()
    {
        int result = defaultValue;
        for (Integer value : new ArrayList<>(values))
            result += value;
        return result;
    }

    public void add(int slot, Integer i)
    {
        set(slot, safeGet(slot) + i);
    }

    public void sub(int slot, Integer i)
    {
        set(slot, safeGet(slot) - i);
    }

    public void zero(int slot)
    {
        set(slot, 0);
    }

    public boolean contains(int slot)
    {
        return slot >= 0 && slot < values.size();
    }

    public Integer safeGet(int index)
    {
        if (index < 0 || index > values.size() - 1) set(index, 0);
        return values.get(index);
    }

    public void set(int slot, Integer i)
    {
        values.ensureCapacity(slot + 1);
        while (values.size() < slot + 1)
            values.add(0);
        values.set(slot, i);
    }

    public int next()
    {
        return values.size();
    }

    public Integer getFor(int slot)
    {
        return safeGet(slot);
    }

    public ArrayList<Integer> forInventory()
    {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 6; i < values.size(); i++)
            list.add(safeGet(i));
        return list;
    }
}
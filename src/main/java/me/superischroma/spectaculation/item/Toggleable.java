package me.superischroma.spectaculation.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Toggleable<T>
{
    private boolean toggled;
    private T value;

    public Toggleable(boolean toggled, T value)
    {
        this.toggled = toggled;
        this.value = value;
    }
}
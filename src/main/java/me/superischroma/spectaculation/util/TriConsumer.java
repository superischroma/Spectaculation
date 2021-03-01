package me.superischroma.spectaculation.util;

public interface TriConsumer<T, U, V>
{
    void accept(T t, U u, V v);
    default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after)
    {
        return null;
    }
}
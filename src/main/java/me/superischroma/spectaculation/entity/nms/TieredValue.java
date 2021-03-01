package me.superischroma.spectaculation.entity.nms;

public class TieredValue<T>
{
    private final T i;
    private final T ii;
    private final T iii;
    private final T iv;

    public TieredValue(T i, T ii, T iii, T iv)
    {
        this.i = i;
        this.ii = ii;
        this.iii = iii;
        this.iv = iv;
    }

    public T getByNumber(int n)
    {
        switch (n)
        {
            case 2: return ii;
            case 3: return iii;
            case 4: return iv;
            default: return i;
        }
    }

    public T getTierI()
    {
        return i;
    }

    public T getTierII()
    {
        return ii;
    }

    public T getTierIII()
    {
        return iii;
    }

    public T getTierIV()
    {
        return iv;
    }
}
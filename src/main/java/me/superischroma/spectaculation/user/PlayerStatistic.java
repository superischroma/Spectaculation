package me.superischroma.spectaculation.user;

public interface PlayerStatistic<T>
{
    int HELMET = 0, CHESTPLATE = 1, LEGGINGS = 2, BOOTS = 3, HAND = 4, SET = 5, BOOST = 6, MINER_BUFF = 7, OBSIDIAN_CHESTPLATE = 8,
            FARMING = 9, MINING = 10, COMBAT = 11, FORAGING = 12, ADD_FOR_INVENTORY = 13, ADD_FOR_POTION_EFFECTS = 50;

    T addAll();
    void add(int slot, T t);
    void sub(int slot, T t);
    void zero(int slot);
    boolean contains(int slot);
    T safeGet(int index);
    void set(int slot, T t);
    int next();
    T getFor(int slot);
}
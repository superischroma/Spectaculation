package me.superischroma.spectaculation.slayer;

import lombok.Getter;

@Getter
class SlayerAbility
{
    private final String name;
    private final String[] description;

    public SlayerAbility(String name, String... description)
    {
        this.name = name;
        this.description = description;
    }
}

package me.superischroma.spectaculation.item.dragon.wise;

import me.superischroma.spectaculation.item.MaterialStatistics;
import me.superischroma.spectaculation.item.armor.ArmorSet;

public class WiseDragonSet implements ArmorSet
{
    @Override
    public String getName()
    {
        return "Wise Blood";
    }

    @Override
    public String getDescription()
    {
        return "All abilities cost 50% less Mana.";
    }

    @Override
    public Class<? extends MaterialStatistics> getHelmet()
    {
        return WiseDragonHelmet.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getChestplate()
    {
        return WiseDragonChestplate.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getLeggings()
    {
        return WiseDragonLeggings.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getBoots()
    {
        return WiseDragonBoots.class;
    }
}